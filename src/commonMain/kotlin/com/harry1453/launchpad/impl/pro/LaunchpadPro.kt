package com.harry1453.launchpad.impl.pro

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.Pad
import com.harry1453.launchpad.impl.toVelocity
import com.harry1453.launchpad.api.MidiDevice
import com.harry1453.launchpad.api.openMidiDevice
import com.harry1453.launchpad.impl.util.parseHexString
import com.harry1453.launchpad.impl.util.plus
import kotlinx.coroutines.*

/**
 * NOTE: Launchpad Pro support is untested as I do not have a Launchpad Pro.
 * This class was just written by reading the documentation.
 *
 * Only supports programmer layout and fader layout; there is no support for note layout or drum layout.
 */
internal class LaunchpadPro : Launchpad {
    override val gridColumnCount = 10
    override val gridColumnStart = -1
    override val gridRowCount = 10
    override val gridRowStart = -1

    private val midiDevice = openMidiDevice {
            it.name.toLowerCase().contains("launchpad pro") // TODO untested
        }
        .apply { setMessageListener(this@LaunchpadPro::onMidiMessage) }

    init {
        // Initialize Launchpad
        midiDevice.sendSysEx(sysExMessageSelectStandaloneMode)
        enterNormalMode()
        stopScrollingText()
        clearAllPadsLights()
    }

    private var autoClockJob: Job? = null
    override var autoClockEnabled: Boolean = false
        set(newSetting) {
            if (newSetting) {
                startAutoClock()
            } else {
                autoClockJob?.cancel()
            }
            field = newSetting
        }

    private fun startAutoClock() {
        autoClockJob = GlobalScope.launch {
            while(isActive) {
                delay(60000.toLong() / 24 / autoClockTempo)
                midiDevice.clock()
            }
        }
    }

    private var padUpdateListener: ((pad: Pad, pressed: Boolean, velocity: Byte) -> Unit)? = null
    private var scrollTextFinishedListener: (() -> Unit)? = null
    private var faderUpdateListener: ((Int, Byte) -> Unit)? = null

    private var bipolarFaders: Boolean = false
    private var faderLayout: Boolean = false

    override val isClosed: Boolean
        get() = !(autoClockJob?.isActive ?: false) && midiDevice.isClosed

    private fun onMidiMessage(midiMessage: ByteArray) {
        if (midiMessage.size == 3) {
            val command = midiMessage[0].toUByte().toInt()
            val controlChange = command in 0xB0..0xBF
            val padCode = midiMessage[1].toUByte().toInt()
            val value = midiMessage[2]
            if (controlChange && padCode in 0x15..0x1C) {
                // It's not a pad, it's a fader!
                val faderValue = if (bipolarFaders) (midiMessage[2] - 63).toByte() else midiMessage[2]
                faderUpdateListener?.invoke(padCode - 0x15, faderValue)
            } else {
                val map = when {
                    controlChange -> LaunchpadProPad.EDGE_PADS
                    else -> LaunchpadProPad.NON_EDGE_PADS
                }
                val pad = map[padCode] ?: return
                val pressed = midiMessage[2] != 0.toByte()
                padUpdateListener?.invoke(pad, pressed, value)
            }
        } else if (midiMessage.contentEquals(sysExMessageScrollTextComplete)) {
            scrollTextFinishedListener?.invoke()
        }
    }

    override fun setPadButtonListener(listener: (pad: Pad, pressed: Boolean, velocity: Byte) -> Unit) {
        this.padUpdateListener = listener
    }

    private fun setPadLightColor(pad: Pad, color: Color, channel: Int) {
        require(pad is LaunchpadProPad)
        // Don't update non-edge pads in fader mode
        if (faderLayout && !pad.isEdgePad) return
        midiDevice.sendMessage(channel, pad.midiCode, color.toVelocity(), if (pad.isEdgePad) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
    }

    override fun setPadLight(pad: Pad, color: Color) {
        setPadLightColor(pad, color, 0)
    }

    override fun flashPadLight(pad: Pad, color1: Color, color2: Color) {
        setPadLightColor(pad, color1, 0)
        setPadLightColor(pad, color2, 1)
    }

    override fun pulsePadLight(pad: Pad, color: Color) {
        setPadLightColor(pad, color, 2)
    }

    private fun Pad.getMidiCode(faderLayout: Boolean): Int? {
        require(this is LaunchpadProPad)
        return this.getMidiCode(faderLayout)
    }

    override fun batchSetPadLights(padsAndColors: Iterable<Pair<Pad, Color>>) {
        midiDevice.sendSysEx(padsAndColors.mapNotNull {
                val pad = it.first
                require(pad is LaunchpadProPad)
                // Don't update non-edge pads in fader mode
                if (faderLayout && !pad.isEdgePad) return@mapNotNull null
                sysExMessageSetPad + (pad.midiCode) + it.second.toVelocity() + 0xF7
            }
            .reduce { acc, bytes -> acc + bytes })
    }

    override fun batchSetRowLights(rowsAndColors: Iterable<Pair<Int, Color>>) {
        // TODO check sizes and indexes
        midiDevice.sendSysEx(rowsAndColors.map {
            sysExMessageSetRow + it.first.toByte() + it.second.toVelocity() + 0xF7
        }.reduce { acc, bytes -> acc + bytes })
    }

    override fun batchSetColumnLights(columnsAndColors: Iterable<Pair<Int, Color>>) {
        // TODO check sizes and indexes
        midiDevice.sendSysEx(columnsAndColors.map {
            sysExMessageSetColumn + it.first.toByte() + it.second.toVelocity() + 0xF7
        }.reduce { acc, bytes -> acc + bytes })
    }

    override fun setAllPadLights(color: Color) {
        midiDevice.sendSysEx(sysExMessageSetAllPads + color.toVelocity() + 0xF7)
    }

    override fun scrollText(message: String, color: Color, loop: Boolean) {
        // FIXME non-ascii characters will break this
        val encodedMessage = message.replace(speedCommandRegex) { matchResult ->
            val speed = matchResult.groupValues[1].toInt()
            speed.toChar().toString()
        }.encodeToByteArray()
        midiDevice.sendSysEx(sysExMessageScrollText + byteArrayOf(color.toVelocity().toByte(), (if (loop) 0x01 else 0x00).toByte()) + encodedMessage + 0xF7)
    }

    override fun stopScrollingText() {
        midiDevice.sendSysEx(sysExMessageStopScrollingText)
        // The launchpad has a bug where it doesn't properly revert to session layout and some things eg. Flashing, Pulsing, do not work after a text scroll is stopped.
        // This doesn't seem to happen when the launchpad finishes scrolling by itself eg. when not looping and the whole message has been displayed.
        // So, we only need to do this when manually stopping scrolling.
        enterNormalMode()
    }

    private fun enterNormalMode() {
        midiDevice.sendSysEx(sysExMessageChangeLayoutToProgrammer)
        faderLayout = false
    }

    private fun enterFaderMode() {
        midiDevice.sendSysEx(sysExMessageChangeLayoutToFader)
        faderLayout = true
    }

    override fun setTextScrollFinishedListener(listener: () -> Unit) {
        this.scrollTextFinishedListener = listener
    }

    override var autoClockTempo: Int = 120 // Launchpad default
        set(newBpm) {
            require(newBpm in 40..240) { "Launchpad MK2 only supports BPM between 40 and 240"}
            field = newBpm
        }

    override val autoClockTempoRange = 40..240

    override fun clock() {
        if (!autoClockEnabled) {
            midiDevice.clock()
        }
    }

    override fun enterBootloader() {
        midiDevice.sendSysEx(sysExMessageEnterBootloader)
    }

    override fun getPad(x: Int, y: Int): Pad? {
        return LaunchpadProPad.findPad(x, y)
    }

    override val maxNumberOfFaders = 8

    override fun setupFaderView(faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean) {
        // TODO support for mixing fader types
        // TODO check sizes and indexes
        bipolarFaders = bipolar
        enterFaderMode()
        midiDevice.sendSysEx(sysExMessageSetupFader + faders.map { (faderIndex, pair) ->
            val color = pair.first
            val initialValue = if (bipolar) pair.second + 63 else pair.second.toInt()
            require(initialValue >= 0) { "Fader value must be 0-127" }
            faderIndex + (if (bipolar) 0x01 else 0x00) + color.toVelocity() + initialValue
        }.reduce { acc, bytes -> acc + bytes } + 0xF7)
    }

    override fun updateFader(faderIndex: Int, value: Byte) {
        val faderValue = if (bipolarFaders) value + 63 else value.toInt()
        require(faderIndex in 0..7) { "Fader index must be 0-7" }
        require(faderValue >= 0) { "Fader value out of range" }
        midiDevice.sendMessage(0, faderIndex + 0x15, faderValue, MidiDevice.MessageType.ControlChange)
    }

    override fun setFaderUpdateListener(listener: (Int, Byte) -> Unit) {
        this.faderUpdateListener = listener
    }

    override fun exitFaderView() {
        enterNormalMode()
    }

    override fun close() {
        autoClockJob?.cancel()
        midiDevice.close()
    }

    companion object {
        private val speedCommandRegex = Regex("\\{s([1-7])}")

        private val sysExHeader = "F00020290210".parseHexString()

        private val sysExMessageSetPad = sysExHeader + 0x0A
        private val sysExMessageSetPadRgb = sysExHeader + 0x0B // TODO this doesn't work...
        private val sysExMessageSetColumn = sysExHeader + 0x0C
        private val sysExMessageSetRow = sysExHeader + 0x0D
        private val sysExMessageSetAllPads = sysExHeader + 0x0E
        private val sysExMessageSetPadsInGrid = sysExHeader + 0x0F // TODO unimplemented
        private val sysExMessageFlashPad = sysExHeader + 0x23 // TODO this doesn't work...
        private val sysExMessagePulsePad = sysExHeader + 0x28 // TODO this doesn't work...

        private val sysExMessageSetupFader = sysExHeader + 0x2B

        private val sysExMessageSelectMode = sysExHeader + 0x21
        private val sysExMessageSelectAbletonMode = sysExMessageSelectMode + 0x00 + 0xF7 // TODO unimplemented
        private val sysExMessageSelectStandaloneMode = sysExMessageSelectMode + 0x01 + 0xF7

        // All in standalone mode
        private val sysExMessageChangeLayout = sysExHeader + 0x2C
        private val sysExMessageChangeLayoutToNote = sysExMessageChangeLayout + 0x00 + 0xF7 // TODO unimplemented
        private val sysExMessageChangeLayoutToDrum = sysExMessageChangeLayout + 0x01 + 0xF7 // TODO unimplemented
        private val sysExMessageChangeLayoutToFader = sysExMessageChangeLayout + 0x02 + 0xF7
        private val sysExMessageChangeLayoutToProgrammer = sysExMessageChangeLayout + 0x03 + 0xF7

        private val sysExMessageScrollText = sysExHeader + 0x14
        private val sysExMessageStopScrollingText = sysExMessageScrollText + 0x00 + 0xF7
        private val sysExMessageScrollTextComplete = "F0002029021815F7".parseHexString()

        private val sysExMessageEnterBootloader = "F000202900710069F7".parseHexString()
    }
}

package com.harry1453.launchpad.impl.mk2

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.Pad
import com.harry1453.launchpad.impl.toVelocity
import com.harry1453.launchpad.api.MidiDevice
import com.harry1453.launchpad.api.openMidiDevice
import com.harry1453.launchpad.impl.util.parseHexString
import com.harry1453.launchpad.impl.util.plus
import kotlinx.coroutines.*

internal class LaunchpadMk2(private val userMode: Boolean = false) :
    Launchpad {
    override val gridColumnCount = 9
    override val gridColumnStart = 0
    override val gridRowCount = 9
    override val gridRowStart = 0

    private val midiDevice = openMidiDevice {
            it.name.toLowerCase().contains("launchpad mk2")
        }
        .apply { setMessageListener(this@LaunchpadMk2::onMidiMessage) }

    init {
        // Initialize Launchpad
        if (userMode) enterUserMode() else enterSessionMode()
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

    override val isClosed: Boolean
        get() = !(autoClockJob?.isActive ?: false) && midiDevice.isClosed

    private fun onMidiMessage(midiMessage: ByteArray) {
        if (midiMessage.size == 3) {
            val padCode = midiMessage[1].toInt()
            val pad = when {
                midiMessage[0].toUByte().toInt() in 0xB0..0xBF -> LaunchpadMk2Pad.CONTROL_CHANGE_PADS[padCode]
                userMode -> LaunchpadMk2Pad.USER_MODE_PADS[padCode]
                else -> LaunchpadMk2Pad.SESSION_MODE_PADS[padCode]
            } ?: return
            val pressed = midiMessage[2] != 0.toByte()
            padUpdateListener?.invoke(pad, pressed, midiMessage[2])
        } else if (midiMessage.contentEquals(sysExMessageScrollTextComplete)) {
            scrollTextFinishedListener?.invoke()
        }
    }

    private val Pad.sessionMidiCode: Int
        get() {
            require(this is LaunchpadMk2Pad)
            return this.sessionMidiCode
        }

    override fun setPadButtonListener(listener: (pad: Pad, pressed: Boolean, velocity: Byte) -> Unit) {
        this.padUpdateListener = listener
    }

    private fun setPadLightColor(pad: Pad, color: Color, channel: LaunchpadMk2Channel) {
        require(pad is LaunchpadMk2Pad)
        midiDevice.sendMessage(channel.channelId, if (userMode) pad.userMidiCode else pad.sessionMidiCode, color.toVelocity(), if (pad.isControlChange) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
    }

    override fun setPadLight(pad: Pad, color: Color) {
        setPadLightColor(pad, color, LaunchpadMk2Channel.Channel1)
    }

    override fun flashPadLight(pad: Pad, color1: Color, color2: Color) {
        setPadLightColor(pad, color1, LaunchpadMk2Channel.Channel1)
        setPadLightColor(pad, color2, LaunchpadMk2Channel.Channel2)
    }

    override fun pulsePadLight(pad: Pad, color: Color) {
        setPadLightColor(pad, color, LaunchpadMk2Channel.Channel3)
    }

    override fun batchSetPadLights(padsAndColors: Iterable<Pair<Pad, Color>>) {
        midiDevice.sendSysEx(padsAndColors.map { sysExMessageSetPad + it.first.sessionMidiCode + it.second.toVelocity() + 0xF7 }
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
        // The launchpad has a bug where it doesn't properly revert to session layout and some things eg. Flashing, Pulsing, do not work after a text scroll completes.
        // This doesn't seem to happen when the launchpad finishes scrolling by itself eg. when not looping and the whole message has been displayed.
        // So, we only need to do this when manually stopping scrolling.
        if (userMode) {
            enterUserMode()
        } else {
            enterSessionMode()
        }
    }

    private fun enterSessionMode() {
        midiDevice.sendSysEx(sysExMessageChangeLayoutToSession)
    }

    private fun enterUserMode() {
        midiDevice.sendSysEx(sysExMessageChangeLayoutToUser)
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
        return LaunchpadMk2Pad.findPad(x, y)
    }

    override fun close() {
        autoClockJob?.cancel()
        midiDevice.close()
    }

    companion object {
        val speedCommandRegex = Regex("\\{s([1-7])}")

        private val sysExHeader = "F00020290218".parseHexString()

        private val sysExMessageSetPad = sysExHeader + 0x0A
        private val sysExMessageSetPadRgb = sysExHeader + 0x0B // TODO this doesn't work...
        private val sysExMessageSetColumn = sysExHeader + 0x0C
        private val sysExMessageSetRow = sysExHeader + 0x0D
        private val sysExMessageSetAllPads = sysExHeader + 0x0E
        private val sysExMessageFlashPad = sysExHeader + 0x23 // TODO this doesn't work...
        private val sysExMessagePulsePad = sysExHeader + 0x28 // TODO this doesn't work...

        private val sysExMessageChangeLayout = sysExHeader + 0x22
        private val sysExMessageChangeLayoutToSession = sysExMessageChangeLayout + 0x00 + 0xF7
        private val sysExMessageChangeLayoutToUser = sysExMessageChangeLayout + 0x01 + 0xF7

        private val sysExMessageScrollText = sysExHeader + 0x14
        private val sysExMessageStopScrollingText = sysExMessageScrollText + 0x00 + 0xF7
        private val sysExMessageScrollTextComplete = "F0002029021815F7".parseHexString()

        private val sysExMessageEnterBootloader = "F000202900710069F7".parseHexString()
    }
}

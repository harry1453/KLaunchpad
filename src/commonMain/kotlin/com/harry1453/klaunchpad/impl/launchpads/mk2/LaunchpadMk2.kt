package com.harry1453.klaunchpad.impl.launchpads.mk2

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.MidiDevice
import com.harry1453.klaunchpad.api.Pad
import com.harry1453.klaunchpad.impl.AbstractLaunchpad
import com.harry1453.klaunchpad.impl.toVelocity
import com.harry1453.klaunchpad.impl.util.parseHexString
import com.harry1453.klaunchpad.impl.util.plus
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Supports using session layout or user layout on the Launchpad, configured by [userMode], as well as fader layout.
 *
 * TODO find a use for [userMode]
 */
internal class LaunchpadMk2(midiDevice: MidiDevice, private val userMode: Boolean) : AbstractLaunchpad(midiDevice) {
    override val gridColumnCount = 9
    override val gridColumnStart = 0
    override val gridRowCount = 9
    override val gridRowStart = 0

    init {
        // Initialize Launchpad
        enterNormalMode()
        stopScrollingText()
        clearAllPadsLights()
    }

    private var padUpdateListener: ((pad: Pad, pressed: Boolean, velocity: Byte) -> Unit)? = null
    private var scrollTextFinishedListener: (() -> Unit)? = null
    private var faderUpdateListener: ((Int, Byte) -> Unit)? = null

    private var bipolarFaders: Boolean = false

    override fun onMidiMessage(message: ByteArray) {
        if (message.size == 3) {
            val command = message[0].toUByte().toInt()
            val controlChange = command in 0xB0..0xBF
            val padCode = message[1].toUByte().toInt()
            val value = message[2]
            if (controlChange && padCode in 0x15..0x1C) {
                // It's not a pad, it's a fader!
                val faderValue = if (bipolarFaders) (message[2] - 63).toByte() else message[2]
                faderUpdateListener?.invoke(padCode - 0x15, faderValue)
            } else {
                val map = when {
                    controlChange -> LaunchpadMk2Pad.CONTROL_CHANGE_PADS
                    userMode -> LaunchpadMk2Pad.USER_MODE_PADS
                    else -> LaunchpadMk2Pad.SESSION_MODE_PADS
                }
                val pad = map[padCode] ?: return
                val pressed = message[2] != 0.toByte()
                padUpdateListener?.invoke(pad, pressed, value)
            }
        } else if (message.contentEquals(sysExMessageScrollTextComplete)) {
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

    private fun setPadLightColor(pad: Pad, color: Color, channel: Int) {
        require(pad is LaunchpadMk2Pad)
        midiDevice.sendMessage(channel, if (userMode) pad.userMidiCode else pad.sessionMidiCode, color.toVelocity(), if (pad.isControlChange) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
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
        // The launchpad has a bug where it doesn't properly revert to session layout and some things eg. Flashing, Pulsing, do not work after a text scroll is stopped.
        // This doesn't seem to happen when the launchpad finishes scrolling by itself eg. when not looping and the whole message has been displayed.
        // So, we only need to do this when manually stopping scrolling.
        enterNormalMode()
    }

    private fun enterNormalMode() {
        midiDevice.sendSysEx(if (userMode) sysExMessageChangeLayoutToUser else sysExMessageChangeLayoutToSession)
    }

    private fun enterFaderMode(bipolar: Boolean) {
        midiDevice.sendSysEx(if (bipolar) sysExMessageChangeLayoutToBipolarFader else sysExMessageChangeLayoutToUnipolarFader)
    }

    override fun setTextScrollFinishedListener(listener: () -> Unit) {
        this.scrollTextFinishedListener = listener
    }

    override val autoClockTempoRange = 40..240

    override fun enterBootloader() {
        midiDevice.sendSysEx(sysExMessageEnterBootloader)
    }

    override fun getPad(x: Int, y: Int): Pad? {
        return LaunchpadMk2Pad.findPad(x, y)
    }

    override val maxNumberOfFaders = 8

    override fun setupFaderView(faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean) {
        // TODO check sizes and indexes
        bipolarFaders = bipolar
        enterFaderMode(bipolar)
        midiDevice.sendSysEx(faders.map { (faderIndex, pair) ->
            val color = pair.first
            val initialValue = if (bipolar) pair.second + 63 else pair.second.toInt()
            require(initialValue >= 0) { "Fader value must be 0-127" }
            sysExMessageSetupFader + faderIndex + (if (bipolar) 0x01 else 0x00) + color.toVelocity() + initialValue + 0xF7
        }.reduce { acc, bytes -> acc + bytes })
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

    companion object {
        private val speedCommandRegex = Regex("\\{s([1-7])}")

        private val sysExHeader = "F00020290218".parseHexString()

        private val sysExMessageSetPad = sysExHeader + 0x0A
        private val sysExMessageSetPadRgb = sysExHeader + 0x0B // TODO this doesn't work...
        private val sysExMessageSetColumn = sysExHeader + 0x0C
        private val sysExMessageSetRow = sysExHeader + 0x0D
        private val sysExMessageSetAllPads = sysExHeader + 0x0E
        private val sysExMessageFlashPad = sysExHeader + 0x23 // TODO this doesn't work...
        private val sysExMessagePulsePad = sysExHeader + 0x28 // TODO this doesn't work...

        private val sysExMessageSetupFader = sysExHeader + 0x2B

        private val sysExMessageChangeLayout = sysExHeader + 0x22
        private val sysExMessageChangeLayoutToSession = sysExMessageChangeLayout + 0x00 + 0xF7
        private val sysExMessageChangeLayoutToUser = sysExMessageChangeLayout + 0x01 + 0xF7
        private val sysExMessageChangeLayoutToUnipolarFader = sysExMessageChangeLayout + 0x04 + 0xF7
        private val sysExMessageChangeLayoutToBipolarFader = sysExMessageChangeLayout + 0x05 + 0xF7

        private val sysExMessageScrollText = sysExHeader + 0x14
        private val sysExMessageStopScrollingText = sysExMessageScrollText + 0x00 + 0xF7
        private val sysExMessageScrollTextComplete = "F0002029021815F7".parseHexString()

        private val sysExMessageEnterBootloader = "F000202900710069F7".parseHexString()
    }
}

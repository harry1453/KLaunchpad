package com.harry1453.launchpad.impl

import com.harry1453.launchpad.Channel
import com.harry1453.launchpad.Launchpad
import com.harry1453.launchpad.Pad
import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.colour.rgbToVelocity
import com.harry1453.launchpad.midi.MidiDevice
import com.harry1453.launchpad.midi.openMidiDevice
import com.harry1453.launchpad.util.parseHexString
import com.harry1453.launchpad.util.plus

class LaunchpadMk2 : Launchpad {
    private val midiDevice = openMidiDevice { it.name.toLowerCase().contains("launchpad mk2") }
        .apply { setMessageListener(this@LaunchpadMk2::onMidiMessage) }

    init {
        enterSessionMode()
    }

    private var padUpdateListener: ((pad: Pad, pressed: Boolean, velocity: UByte) -> Unit)? = null
    private var scrollTextFinishedListener: (() -> Unit)? = null

    override val isClosed: Boolean
        get() = midiDevice.isClosed

    private fun onMidiMessage(midiMessage: ByteArray) {
        if (midiMessage.size == 3) {
            val pad = LaunchpadMk2Pads.findPad(midiMessage[1].toInt(), midiMessage[0] == 176.toByte()) ?: return
            val pressed = midiMessage[2] != 0.toByte()
            padUpdateListener?.invoke(pad, pressed, midiMessage[2].toUByte())
        } else if (midiMessage.contentEquals(sysExMessageScrollTextComplete)) {
            scrollTextFinishedListener?.invoke()
        }
    }

    override fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit) {
        this.padUpdateListener = listener
    }

    override fun setPadLightColour(pad: Pad, colour: Colour, channel: Channel) {
        midiDevice.sendMessage(channel.channelId, pad.midiCode, rgbToVelocity(colour), if (pad.isControlChange) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
    }

    override fun scrollText(message: String, colour: Colour, loop: Boolean) {
        // FIXME non-ascii characters will break this
        val encodedMessage = message.replace(speedCommandRegex) { matchResult ->
            val speed = matchResult.groupValues[1].toInt()
            speed.toChar().toString()
        }.encodeToByteArray()
        midiDevice.sendSysEx(sysExMessageScrollText + byteArrayOf(rgbToVelocity(colour).toByte(), (if (loop) 0x01 else 0x00).toByte()) + encodedMessage + 0xF7)
    }

    override fun stopScrollingText() {
        midiDevice.sendSysEx(sysExMessageStopScrollingText)
        // The launchpad has a bug where it doesn't properly revert to session layout and some things eg. Flashing, Pulsing, do not work after a text scroll completes.
        // This doesn't seem to happen when the launchpad finishes scrolling by itself eg. when not looping and the whole message has been displayed.
        // So, we only need to do this when manually stopping scrolling.
        enterSessionMode()
    }

    private fun enterSessionMode() {
        midiDevice.sendSysEx(sysExMessageChangeLayoutToSession)
    }

    override fun setTextScrollFinishedListener(listener: () -> Unit) {
        this.scrollTextFinishedListener = listener
    }

    override fun clock() {
        TODO("Not yet implemented")
    }

    override fun enterBootloader() {
        midiDevice.sendSysEx(sysExMessageEnterBootloader)
    }

    override fun findPad(gridX: Int, gridY: Int): Pad? {
        return LaunchpadMk2Pads.findPad(gridX, gridY)
    }

    override fun close() {
        midiDevice.close()
    }

    companion object {
        val speedCommandRegex = Regex("\\{s([1-7])}")

        private val sysExMessageChangeLayout = "F0002029021822".parseHexString()
        private val sysExMessageChangeLayoutToSession = sysExMessageChangeLayout + 0x00 + 0xF7
        private val sysExMessageScrollText = "F0002029021814".parseHexString()
        private val sysExMessageStopScrollingText = sysExMessageScrollText + 0x00 + 0xF7
        private val sysExMessageScrollTextComplete = "F0002029021815F7".parseHexString()
        private val sysExMessageEnterBootloader = "F000202900710069F7".parseHexString()
    }
}

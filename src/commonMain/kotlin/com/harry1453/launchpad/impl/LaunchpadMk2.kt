package com.harry1453.launchpad.impl

import com.harry1453.launchpad.Channel
import com.harry1453.launchpad.Launchpad
import com.harry1453.launchpad.Pad
import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.colour.rgbToVelocity
import com.harry1453.launchpad.midi.MidiDevice
import com.harry1453.launchpad.midi.openMidiDevice
import com.harry1453.launchpad.util.parseHexString

class LaunchpadMk2 : Launchpad {
    private val midiDevice = openMidiDevice { it.name.toLowerCase().contains("launchpad") }
        .apply { setMessageListener(this@LaunchpadMk2::onMidiMessage) }

    private var padUpdateListener: ((pad: Pad, pressed: Boolean, velocity: UByte) -> Unit)? = null

    override val isClosed: Boolean
        get() = midiDevice.isClosed

    private fun onMidiMessage(midiMessage: ByteArray) {
        if (midiMessage.size == 3) {
            val pad = LaunchpadMk2Pads.findPad(midiMessage[1].toInt(), midiMessage[0] == 176.toByte()) ?: return
            val pressed = midiMessage[2] != 0.toByte()
            padUpdateListener?.invoke(pad, pressed, midiMessage[2].toUByte())
        }
    }

    override fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit) {
        this.padUpdateListener = listener
    }

    override fun setPadLightColour(pad: Pad, colour: Colour, accurateColour: Boolean, channel: Channel) {
        if (accurateColour) {
            TODO("Not yet implemented")
        } else {
            midiDevice.sendMessage(channel.channelId, pad.midiCode, rgbToVelocity(colour), if (pad.isControlChange) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
        }
    }

    override fun clock() {
        TODO("Not yet implemented")
    }

    override fun enterBootloader() {
        midiDevice.sendSysEx("F000202900710069F7".parseHexString())
    }

    override fun findPad(gridX: Int, gridY: Int): Pad? {
        return LaunchpadMk2Pads.findPad(gridX, gridY)
    }

    override fun close() {
        midiDevice.close()
    }
}

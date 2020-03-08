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

    private fun Colour.toVelocity(): Int {
        return rgbToVelocity(this)
    }

    override fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit) {
        this.padUpdateListener = listener
    }

    override fun setPadLightColour(pad: Pad, colour: Colour, channel: Channel) {
        midiDevice.sendMessage(channel.channelId, pad.midiCode, colour.toVelocity(), if (pad.isControlChange) MidiDevice.MessageType.ControlChange else MidiDevice.MessageType.NoteOn)
    }

    override fun setPadLightColourBulk(padsAndColours: Iterable<Pair<Pad, Colour>>, mode: Launchpad.BulkUpdateMode) {
        midiDevice.sendSysEx(when(mode) {
            Launchpad.BulkUpdateMode.SET -> padsAndColours.map { sysExMessageSetPad + it.first.midiCode + it.second.toVelocity() + 0xF7 }
            Launchpad.BulkUpdateMode.FLASH -> padsAndColours.map { sysExMessageFlashPad + it.first.midiCode + it.second.toVelocity() + 0xF7 }
            Launchpad.BulkUpdateMode.PULSE -> padsAndColours.map { sysExMessagePulsePad + it.first.midiCode + it.second.toVelocity() + 0xF7 }
            Launchpad.BulkUpdateMode.SET_RGB -> padsAndColours.map {
                val rgb = (it.second.r / 4.toUByte()).toByte() + (it.second.g / 4.toUByte()).toByte() + (it.second.b / 4.toUByte()).toByte()
                sysExMessageSetPadRgb + it.first.midiCode + rgb + 0xF7
            }
        }.reduce { acc, bytes -> acc + bytes })
    }

    override fun setRowColourBulk(rowsAndColours: Iterable<Pair<Int, Colour>>) {
        // TODO check sizes and indexes
        midiDevice.sendSysEx(rowsAndColours.map {
            sysExMessageSetRow + it.first.toByte() + it.second.toVelocity() + 0xF7
        }.reduce { acc, bytes -> acc + bytes })
    }

    override fun setColumnColourBulk(columnsAndColours: Iterable<Pair<Int, Colour>>) {
        // TODO check sizes and indexes
        midiDevice.sendSysEx(columnsAndColours.map {
            sysExMessageSetColumn + it.first.toByte() + it.second.toVelocity() + 0xF7
        }.reduce { acc, bytes -> acc + bytes })
    }

    override fun setAllPads(colour: Colour) {
        midiDevice.sendSysEx(sysExMessageSetAllPads + colour.toVelocity() + 0xF7)
    }

    override fun scrollText(message: String, colour: Colour, loop: Boolean) {
        // FIXME non-ascii characters will break this
        val encodedMessage = message.replace(speedCommandRegex) { matchResult ->
            val speed = matchResult.groupValues[1].toInt()
            speed.toChar().toString()
        }.encodeToByteArray()
        midiDevice.sendSysEx(sysExMessageScrollText + byteArrayOf(colour.toVelocity().toByte(), (if (loop) 0x01 else 0x00).toByte()) + encodedMessage + 0xF7)
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

        private val sysExHeader = "F00020290218".parseHexString()
        private val sysExMessageSetPad = sysExHeader + 0x0A
        private val sysExMessageSetPadRgb = sysExHeader + 0x0B
        private val sysExMessageSetColumn = sysExHeader + 0x0C
        private val sysExMessageSetRow = sysExHeader + 0x0D
        private val sysExMessageSetAllPads = sysExHeader + 0x0E
        private val sysExMessageFlashPad = sysExHeader + 0x23
        private val sysExMessagePulsePad = sysExHeader + 0x28
        private val sysExMessageChangeLayout = sysExHeader + 0x22
        private val sysExMessageChangeLayoutToSession = sysExMessageChangeLayout + 0x00 + 0xF7
        private val sysExMessageScrollText = sysExHeader + 0x14
        private val sysExMessageStopScrollingText = sysExMessageScrollText + 0x00 + 0xF7
        private val sysExMessageScrollTextComplete = "F0002029021815F7".parseHexString()
        private val sysExMessageEnterBootloader = "F000202900710069F7".parseHexString()
    }
}

package com.harry1453.launchpad.midi

import kotlinx.cinterop.*
import platform.windows.*

actual inline fun openMidiDevice(deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice {
    val inputDeviceCount = midiInGetNumDevs().toInt()
    val outputDeviceCount = midiOutGetNumDevs().toInt()
    for (i in 0 until inputDeviceCount) {
        memScoped {
            val capabilities: CValue<MIDIINCAPS> = cValue()
            val cap = midiInGetDevCapsW(i.toULong(), capabilities.getPointer(this), sizeOf<MIDIINCAPS>().toUInt())
            println(cap)
        }
    }
    for (i in 0 until outputDeviceCount) {
        memScoped {
            val capabilities: CValue<MIDIOUTCAPS> = cValue()
            val cap = midiOutGetDevCapsW(i.toULong(), capabilities.getPointer(this), sizeOf<MIDIOUTCAPS>().toUInt())
            println(cap)
        }
    }
    return MidiDeviceImpl()
}

class MidiDeviceImpl : MidiDevice {
    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        TODO("Not yet implemented")
    }

    override fun sendSysEx(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun setMessageListener(messageListener: (ByteArray) -> Unit) {
        TODO("Not yet implemented")
    }

    override val isClosed: Boolean
        get() = TODO("Not yet implemented")

    override fun close() {
        TODO("Not yet implemented")
    }
}

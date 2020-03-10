package com.harry1453.klaunchpad.api

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import platform.windows.*

actual suspend inline fun openMidiDeviceAsync(crossinline deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice {
    // TODO calling any of the midi functions breaks the linker...
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
    TODO()
}

class MidiDeviceImpl : MidiDevice {
    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        TODO("Not yet implemented")
    }

    override fun sendSysEx(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun clock() {
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

package com.harry1453.launchpad.api

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import platform.windows.*

actual inline fun openMidiDeviceAsync(crossinline deviceFilter: (MidiDeviceInfo) -> Boolean): Deferred<MidiDevice> {
    return GlobalScope.async {
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
        return@async MidiDeviceImpl()
    }
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

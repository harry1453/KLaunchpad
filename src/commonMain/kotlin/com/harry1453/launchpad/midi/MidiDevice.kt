package com.harry1453.launchpad.midi

import com.harry1453.launchpad.util.Closeable

data class MidiDeviceInfo(val name: String, val description: String, val vendor: String, val version: String)

interface MidiDevice : Closeable {
    fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MessageType)
    fun sendSysEx(bytes: ByteArray)
    fun clock()
    fun setMessageListener(messageListener: (ByteArray) -> Unit)

    enum class MessageType {
        NoteOn,
        NoteOff,
        ControlChange,
    }
}

expect inline fun openMidiDevice(deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice

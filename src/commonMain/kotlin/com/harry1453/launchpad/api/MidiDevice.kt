package com.harry1453.launchpad.api

data class MidiDeviceInfo(val name: String, val description: String, val vendor: String, val version: String)

interface MidiDevice : Closable {
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

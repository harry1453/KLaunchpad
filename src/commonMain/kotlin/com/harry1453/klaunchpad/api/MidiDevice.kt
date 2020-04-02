package com.harry1453.klaunchpad.api

data class MidiDeviceInfo(
    val name: String,
    val version: String,
    val index: Int
)

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

/**
 * A function to open a MIDI device, which is the first available device for which [deviceFilter] returns true.
 *
 * [deviceFilter] may be called on any thread.
 */
expect suspend inline fun openMidiDeviceAsync(deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice

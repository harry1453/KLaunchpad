package com.harry1453.launchpad.api

import kotlinx.coroutines.Deferred

data class MidiDeviceInfo(val name: String, val vendor: String, val version: String)

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
expect inline fun openMidiDeviceAsync(crossinline deviceFilter: (MidiDeviceInfo) -> Boolean): Deferred<MidiDevice>

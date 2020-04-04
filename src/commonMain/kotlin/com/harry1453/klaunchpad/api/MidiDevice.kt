package com.harry1453.klaunchpad.api

interface MidiDeviceInfo {
    val name: String
    val version: String
}

@Deprecated("Use separate MidiInputDevice and MidiOutputDevice")
internal interface MidiDevice : Closable {
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

@Deprecated("Use separate MidiInputDevice and MidiOutputDevice")
internal class MidiDeviceWrapper(private val inputDevice: MidiInputDevice, private val outputDevice: MidiOutputDevice) : MidiDevice { // TODO remove this class once the launchpads can use the individual devices
    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        val firstByte = (when(messageType) {
            MidiDevice.MessageType.NoteOff -> 0x80
            MidiDevice.MessageType.NoteOn -> 0x90
            MidiDevice.MessageType.ControlChange -> 0xB0
        } or (channel and 0xF)).toByte()
        val message = byteArrayOf(firstByte, data1.toByte(), data2.toByte())
        outputDevice.sendMessage(message)
    }

    override fun sendSysEx(bytes: ByteArray) {
        outputDevice.sendMessage(bytes)
    }

    override fun clock() {
        outputDevice.sendMessage(byteArrayOf(0xF8.toByte()))
    }

    override fun setMessageListener(messageListener: (ByteArray) -> Unit) {
        inputDevice.setMessageListener(messageListener)
    }

    override val isClosed: Boolean
        get() = inputDevice.isClosed && outputDevice.isClosed

    override fun close() {
        if (!inputDevice.isClosed) inputDevice.close()
        if (!outputDevice.isClosed) outputDevice.close()
    }
}

interface MidiInputDevice: Closable {
    fun setMessageListener(messageListener: (message: ByteArray) -> Unit)
}

interface MidiOutputDevice: Closable {
    fun sendMessage(message: ByteArray)
}

/**
 * Get a list of currently connected MIDI output devices
 */
expect suspend fun listMidiInputDevicesAsync(): List<MidiDeviceInfo>

/**
 * Open the MIDI device described by [deviceInfo], which must have been returned by [listMidiOutputDevicesAsync]
 */
expect suspend fun openMidiInputDeviceAsync(deviceInfo: MidiDeviceInfo): MidiInputDevice

/**
 * Get a list of currently connected MIDI output devices
 */
expect suspend fun listMidiOutputDevicesAsync(): List<MidiDeviceInfo>

/**
 * Open the MIDI device described by [deviceInfo], which must have been returned by [listMidiOutputDevicesAsync]
 */
expect suspend fun openMidiOutputDeviceAsync(deviceInfo: MidiDeviceInfo): MidiOutputDevice

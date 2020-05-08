package com.harry1453.klaunchpad.api

import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.MidiUnavailableException
import javax.sound.midi.Receiver
import javax.sound.midi.MidiDevice as JvmMidiDevice

private data class MidiInputDeviceInfoImpl(
    override val name: String,
    override val version: String,
    internal val device: JvmMidiDevice
) : MidiInputDeviceInfo

actual suspend fun listMidiInputDevicesImpl(): List<MidiInputDeviceInfo> {
    return MidiSystem.getMidiDeviceInfo()
        .map { MidiSystem.getMidiDevice(it) }
        .filter { it.maxTransmitters != 0 }
        .map { MidiInputDeviceInfoImpl(it.deviceInfo.name, it.deviceInfo.version, it) }
}

actual suspend fun openMidiInputDeviceImpl(deviceInfo: MidiInputDeviceInfo): MidiInputDevice {
    require(deviceInfo is MidiInputDeviceInfoImpl)

    try {
        deviceInfo.device.open()
    } catch (e: MidiUnavailableException) {
        error("Another app is using the device.")
    }

    return MidiInputDeviceImpl(deviceInfo.device)
}

private data class MidiOutputDeviceInfoImpl(
    override val name: String,
    override val version: String,
    internal val device: JvmMidiDevice
) : MidiOutputDeviceInfo

actual suspend fun listMidiOutputDevicesImpl(): List<MidiOutputDeviceInfo> {
    return MidiSystem.getMidiDeviceInfo()
        .map { MidiSystem.getMidiDevice(it) }
        .filter { it.maxReceivers != 0 }
        .map { MidiOutputDeviceInfoImpl(it.deviceInfo.name, it.deviceInfo.version, it) }
}

actual suspend fun openMidiOutputDeviceImpl(deviceInfo: MidiOutputDeviceInfo): MidiOutputDevice {
    require(deviceInfo is MidiOutputDeviceInfoImpl)

    try {
        deviceInfo.device.open()
    } catch (e: MidiUnavailableException) {
        error("Another app is using the device.")
    }

    return MidiOutputDeviceImpl(deviceInfo.device)
}

private class MidiInputDeviceImpl(private val device: JvmMidiDevice) : Receiver, MidiInputDevice {
    override var isClosed: Boolean = false

    private var messageListener: ((ByteArray) -> Unit)? = null
    private val input = device.transmitter

    init {
        input.receiver = this
    }

    override fun send(message: MidiMessage, timeStamp: Long) {
        messageListener?.invoke(message.message)
    }

    override fun setMessageListener(messageListener: (message: ByteArray) -> Unit) {
        this.messageListener = messageListener
    }

    override fun close() {
        if (!isClosed) { // TODO consistent close behaviour across implementations
            isClosed = true
            input.close()
            device.close()
        }
    }
}

private class MidiOutputDeviceImpl(private val device: JvmMidiDevice) : MidiOutputDevice {
    override var isClosed: Boolean = false

    private val output = device.receiver

    override fun sendMessage(message: ByteArray) {
        output.send(ArbitraryMidiMessage(message), -1)
    }

    override fun close() {
        if (!isClosed) { // TODO consistent close behaviour across implementations
            isClosed = true
            output.close()
            device.close()
        }
    }
}

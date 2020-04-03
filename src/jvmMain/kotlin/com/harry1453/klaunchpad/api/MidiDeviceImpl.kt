package com.harry1453.klaunchpad.api

import javax.sound.midi.*
import javax.sound.midi.MidiDevice as JvmMidiDevice

internal actual suspend inline fun openMidiDeviceAsync(deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice {
    // TODO take advantage of parallelism
    val firstDeviceInfo = MidiSystem.getMidiDeviceInfo().mapIndexed { index, device ->
        Pair(device, MidiDeviceInfo(device.name, device.version, index))
    }
        .firstOrNull { deviceFilter(it.second) }?.first ?: error("Could not find first device")
    val secondDeviceInfo = MidiSystem.getMidiDeviceInfo().mapIndexed { index, device ->
        Pair(device, MidiDeviceInfo(device.name, device.version, index))
    }
        .filter { deviceFilter(it.second) }.getOrNull(1)?.first ?: error("Could not find second device")
    val firstDevice: JvmMidiDevice = MidiSystem.getMidiDevice(firstDeviceInfo)
    val secondDevice: JvmMidiDevice = MidiSystem.getMidiDevice(secondDeviceInfo)
    val outputDevice = when {
        firstDevice.maxReceivers != 0 -> firstDevice
        secondDevice.maxReceivers != 0 -> secondDevice
        else -> error("Could not find an input device")
    }
    val inputDevice = when {
        firstDevice.maxTransmitters != 0 -> firstDevice
        secondDevice.maxTransmitters != 0 -> secondDevice
        else -> error("Could not find an input device")
    }
    try {
        firstDevice.open()
        secondDevice.open()
    } catch (e: MidiUnavailableException) {
        throw IllegalStateException("Another app is using the Launchpad.")
    }
    return MidiDeviceImpl(inputDevice, outputDevice)
}

class MidiDeviceImpl(private val inputDevice: JvmMidiDevice, private val outputDevice: JvmMidiDevice) : Receiver,
    MidiDevice {
    private val output = outputDevice.receiver
    private val input = inputDevice.transmitter

    private var messageListener: ((ByteArray) -> Unit)? = null

    init {
        input.receiver = this
    }

    override var isClosed: Boolean = false

    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        val command = when(messageType) {
            MidiDevice.MessageType.NoteOn -> ShortMessage.NOTE_ON
            MidiDevice.MessageType.NoteOff -> ShortMessage.NOTE_OFF
            MidiDevice.MessageType.ControlChange -> ShortMessage.CONTROL_CHANGE
        }
        output.send(ShortMessage(command, channel, data1, data2), -1)
    }

    override fun sendSysEx(bytes: ByteArray) {
        output.send(SysexMessage(bytes, bytes.size), -1)
    }

    override fun clock() {
        output.send(ShortMessage(ShortMessage.TIMING_CLOCK), -1)
    }

    override fun setMessageListener(messageListener: (ByteArray) -> Unit) {
        this.messageListener = messageListener
    }

    override fun send(message: MidiMessage, timeStamp: Long) {
        try {
            messageListener?.invoke(message.message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun close() {
        if (!isClosed) {
            isClosed = true
            input.close()
            output.close()
            inputDevice.close()
            outputDevice.close()
        }
    }
}

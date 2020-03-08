package com.harry1453.launchpad.midi

import java.lang.Exception
import javax.sound.midi.*
import javax.sound.midi.MidiDevice as JvmMidiDevice

actual inline fun openMidiDevice(deviceFilter: (MidiDeviceInfo) -> Boolean, noinline messageCallback: (ByteArray) -> Unit): MidiDevice {
    val firstDeviceInfo = MidiSystem.getMidiDeviceInfo().map { Pair(it, MidiDeviceInfo(it.name, it.description, it.vendor, it.version)) }
        .filter { deviceFilter(it.second) }.getOrNull(0)?.first ?: error("Could not find device")
    val secondDeviceInfo = MidiSystem.getMidiDeviceInfo().map { Pair(it, MidiDeviceInfo(it.name, it.description, it.vendor, it.version)) }
        .filter { deviceFilter(it.second) }.getOrNull(1)?.first ?: error("Could not find device")
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
    firstDevice.open()
    secondDevice.open()
    return MidiDeviceImpl(inputDevice, outputDevice, messageCallback)
}

class MidiDeviceImpl(private val inputDevice: JvmMidiDevice, private val outputDevice: JvmMidiDevice, private val messageCallback: (ByteArray) -> Unit) : Receiver, MidiDevice {
    private val output = outputDevice.receiver
    private val input = inputDevice.transmitter

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

    override fun send(message: MidiMessage, timeStamp: Long) {
        try {
            messageCallback(message.message)
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

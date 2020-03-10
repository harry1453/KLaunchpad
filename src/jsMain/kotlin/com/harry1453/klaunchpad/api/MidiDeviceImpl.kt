package com.harry1453.klaunchpad.api

import jsExternal.MIDIInput
import jsExternal.MIDIOutput
import jsExternal.midiOptions
import jsExternal.requestMIDIAccess
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import toMap
import kotlin.browser.window

actual suspend inline fun openMidiDeviceAsync(crossinline deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice {
    val midiAccess = window.navigator.requestMIDIAccess(midiOptions(sysex = true)).await()

    val midiInput = midiAccess.inputs.toMap().map { (deviceId, device) ->
        device to MidiDeviceInfo(device.name.orEmpty(), device.manufacturer.orEmpty(), device.version.orEmpty())
    }.firstOrNull { deviceFilter(it.second) }?.first ?: error("Could not find device")

    val midiOutput = midiAccess.outputs.toMap().map { (deviceId, device) ->
        device to MidiDeviceInfo(device.name.orEmpty(), device.manufacturer.orEmpty(), device.version.orEmpty())
    }.firstOrNull { deviceFilter(it.second) }?.first ?: error("Could not find device")

    return MidiDeviceImpl(midiInput.open().await(), midiOutput.open().await())
}

class MidiDeviceImpl(private val midiInput: MIDIInput, private val midiOutput: MIDIOutput) : MidiDevice {
    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        val command = when(messageType) {
            MidiDevice.MessageType.NoteOn -> 0x90
            MidiDevice.MessageType.NoteOff -> 0x80
            MidiDevice.MessageType.ControlChange -> 0xB0
        }
        midiOutput.send(Uint8Array(byteArrayOf(((command and 0xF0) or (channel and 0x0F) and 0xFF).toByte(), (data1 and 0xFF).toByte(), (data2 and 0xFF).toByte()).toTypedArray()))
    }

    override fun sendSysEx(bytes: ByteArray) {
        midiOutput.send(Uint8Array(bytes.toTypedArray()))
    }

    override fun clock() {
        midiOutput.send(Uint8Array(byteArrayOf(0xF8.toByte()).toTypedArray()))
    }

    private fun Uint8Array.toByteArray(): ByteArray {
        val byteArray = ByteArray(this.length)
        for (i in 0 until length) {
            byteArray[i] = this[i]
        }
        return byteArray
    }

    override fun setMessageListener(messageListener: (ByteArray) -> Unit) {
        midiInput.onmidimessage = {
            messageListener(it.data.toByteArray())
        }
    }

    override val isClosed: Boolean
        get() = midiInput.state == "connected" && midiOutput.state == "connected"

    override fun close() {
        midiOutput.clear()
        midiInput.close()
        midiOutput.close()
    }
}

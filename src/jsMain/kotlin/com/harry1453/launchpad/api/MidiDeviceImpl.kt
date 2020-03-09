package com.harry1453.launchpad.api

import com.harry1453.launchpad.api.webmidi.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.events.EventListener
import kotlin.browser.window
import kotlin.js.json

actual inline fun openMidiDeviceAsync(crossinline deviceFilter: (MidiDeviceInfo) -> Boolean): Deferred<MidiDevice> {
    return GlobalScope.async {
        val midiAccess = window.navigator.requestMIDIAccess(json(Pair("sysex", true)).unsafeCast<MIDIOptions>()).await()

        val midiInput = midiAccess.inputs.values.map {
            it to MidiDeviceInfo(it.name.orEmpty(), it.manufacturer.orEmpty(), it.version.orEmpty())
        }.firstOrNull { deviceFilter(it.second) }?.first ?: error("Could not find device")

        val midiOutput = midiAccess.outputs.values.map {
            it to MidiDeviceInfo(it.name.orEmpty(), it.manufacturer.orEmpty(), it.version.orEmpty())
        }.firstOrNull { deviceFilter(it.second) }?.first ?: error("Could not find device")

        return@async MidiDeviceImpl(midiInput.open().await(), midiOutput.open().await())
    }
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
        midiInput.onmidimessage = EventListener {
            val message = it.unsafeCast<MIDIMessageEvent>()
            messageListener(message.data.toByteArray())
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

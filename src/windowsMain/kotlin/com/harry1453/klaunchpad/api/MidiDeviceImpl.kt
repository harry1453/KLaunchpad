package com.harry1453.klaunchpad.api

import kotlinx.cinterop.*
import platform.windows.*

actual suspend inline fun openMidiDeviceAsync(deviceFilter: (MidiDeviceInfo) -> Boolean): MidiDevice {
    val inputDeviceCount = WindowsMidiApi.midiInGetNumDevs!!().toInt()
    val outputDeviceCount = WindowsMidiApi.midiOutGetNumDevs!!().toInt()

    var inputDeviceID: UInt? = null
    for (i in 0 until inputDeviceCount) {
        if (inputDeviceID != null) break
        memScoped {
            val capabilities = alloc<MIDIINCAPS>()
            val retVal = WindowsMidiApi.midiInGetDevCaps!!(i.toUInt(), capabilities.ptr, sizeOf<MIDIINCAPS>().toUInt())
            WindowsMidiApi.throwIfError(retVal)
            if (deviceFilter(MidiDeviceInfo(capabilities.szPname.toKString(), capabilities.vDriverVersion.toString(16), i))) {
                inputDeviceID = i.toUInt()
                // TODO we can't break because memScoped does not have a contract...
            }
        }
    }
    if (inputDeviceID == null) error("Could not find input device")

    var outputDeviceID: UInt? = null
    for (i in 0 until outputDeviceCount) {
        if (outputDeviceID != null) break
        memScoped {
            val capabilities = alloc<MIDIOUTCAPS>()
            val retVal = WindowsMidiApi.midiOutGetDevCaps!!(i.toUInt(), capabilities.ptr, sizeOf<MIDIOUTCAPS>().toUInt())
            WindowsMidiApi.throwIfError(retVal)
            if (deviceFilter(MidiDeviceInfo(capabilities.szPname.toKString(), capabilities.vDriverVersion.toString(16), i + inputDeviceCount))) {
                outputDeviceID = i.toUInt()
                // TODO we can't break because memScoped does not have a contract...
            }
        }
    }
    if (outputDeviceID == null) error("Could not find output device")

    val outputDevice = nativeHeap.alloc<HMIDIOUTVar>()
    var retVal = WindowsMidiApi.midiOutOpen!!(outputDevice.ptr, outputDeviceID!!, 0u, 0u, CALLBACK_NULL.toUInt())
    WindowsMidiApi.throwIfError(retVal)

    val midiDeviceHolder = Holder<MidiDeviceImpl>()
    val midiDeviceHolderRef = StableRef.create(midiDeviceHolder)

    val inputDevice = nativeHeap.alloc<HMIDIINVar>()
    retVal = WindowsMidiApi.midiInOpen!!(inputDevice.ptr, inputDeviceID!!, staticCFunction(::midiInCallback).rawValue.toLong().toULong(), midiDeviceHolderRef.asCPointer().rawValue.toLong().toULong(), CALLBACK_FUNCTION.toUInt())
    WindowsMidiApi.throwIfError(retVal)
    retVal = WindowsMidiApi.midiInStart!!(inputDevice.value!!)
    WindowsMidiApi.throwIfError(retVal)

    val midiDevice = MidiDeviceImpl(inputDevice, outputDevice, midiDeviceHolderRef)
    midiDeviceHolder.value = midiDevice
    return midiDevice
}

fun midiInCallback(hmi: HMIDIIN, wMsg: UINT, dwInstance: COpaquePointer, dwParam1: DWORD_PTR, dwParam2: DWORD_PTR) {
    initRuntimeIfNeeded()
    val midiDeviceHolder = dwInstance.asStableRef<Holder<MidiDeviceImpl>>().get()
    if (midiDeviceHolder.value == null) return
    when(wMsg.toInt()) {
        MIM_DATA -> {
            val decodedInt = dwParam1.toInt().toBytesLE()
            val messageBytes = ByteArray(3)
            decodedInt.copyInto(messageBytes, endIndex = messageBytes.size)
            midiDeviceHolder.value?.messageListener?.invoke(messageBytes)
        }
        MIM_LONGDATA -> {
            // TODO
        }
    }
}

class MidiDeviceImpl(private val inputDevice: HMIDIINVar, private val outputDevice: HMIDIOUTVar, private val midiDeviceHolderRef: StableRef<Holder<MidiDeviceImpl>>) : MidiDevice {
    override var isClosed: Boolean = false

    var messageListener: ((ByteArray) -> Unit)? = null

    override fun sendMessage(channel: Int, data1: Int, data2: Int, messageType: MidiDevice.MessageType) {
        val firstByte = (when(messageType) {
            MidiDevice.MessageType.NoteOff -> 0x80
            MidiDevice.MessageType.NoteOn -> 0x90
            MidiDevice.MessageType.ControlChange -> 0xB0
        } or (channel and 0xF)).toByte()
        val data = byteArrayOf(firstByte, data1.toByte(), data2.toByte(), 0x00)
        val retVal = WindowsMidiApi.midiOutShortMsg!!(outputDevice.value!!, data.toIntLE().toUInt())
        WindowsMidiApi.throwIfError(retVal)
    }

    override fun sendSysEx(bytes: ByteArray) {
        memScoped {
            val buffer = allocArray<ByteVar>(bytes.size)
            bytes.forEachIndexed { index, byte ->
                buffer[index] = byte
            }
            val midiHdr = alloc<MIDIHDR>()
            midiHdr.lpData = buffer
            midiHdr.dwBufferLength = bytes.size.toUInt()
            midiHdr.dwBytesRecorded = bytes.size.toUInt()

            var retVal = WindowsMidiApi.midiOutPrepareHeader!!(outputDevice.value!!, midiHdr.ptr, sizeOf<MIDIHDR>().toUInt())
            WindowsMidiApi.throwIfError(retVal)
            retVal = WindowsMidiApi.midiOutLongMsg!!(outputDevice.value!!, midiHdr.ptr, sizeOf<MIDIHDR>().toUInt())
            WindowsMidiApi.throwIfError(retVal)
            retVal = WindowsMidiApi.midiOutUnprepareHeader!!(outputDevice.value!!, midiHdr.ptr, sizeOf<MIDIHDR>().toUInt())
            WindowsMidiApi.throwIfError(retVal)
        }
    }

    override fun clock() {
        val retVal = WindowsMidiApi.midiOutShortMsg!!(outputDevice.value!!, byteArrayOf(0xF8.toByte(), 0x00, 0x00, 0x00).toIntLE().toUInt())
        WindowsMidiApi.throwIfError(retVal)
    }

    override fun setMessageListener(messageListener: (ByteArray) -> Unit) {
        this.messageListener = messageListener
    }

    override fun close() {
        var retVal = WindowsMidiApi.midiInStop!!(inputDevice.value!!)
        WindowsMidiApi.throwIfError(retVal)

        retVal = WindowsMidiApi.midiInClose!!(inputDevice.value!!)
        WindowsMidiApi.throwIfError(retVal)
        retVal = WindowsMidiApi.midiOutClose!!(outputDevice.value!!)
        WindowsMidiApi.throwIfError(retVal)

        midiDeviceHolderRef.dispose()

        nativeHeap.free(inputDevice)
        nativeHeap.free(outputDevice)

        isClosed = true
    }
}

class Holder<T> {
    var value: T? = null
}

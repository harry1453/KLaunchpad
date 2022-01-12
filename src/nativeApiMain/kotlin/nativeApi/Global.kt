package nativeApi

import com.harry1453.klaunchpad.api.*
import com.harry1453.klaunchpad.api.Launchpad.Companion.listMidiInputDevices
import com.harry1453.klaunchpad.api.Launchpad.Companion.listMidiOutputDevices
import com.harry1453.klaunchpad.api.Launchpad.Companion.openMidiInputDevice
import com.harry1453.klaunchpad.api.Launchpad.Companion.openMidiOutputDevice
import kotlinx.cinterop.*
import kotlinx.coroutines.runBlocking
import nativeApi.utils.externalFunction
import nativeApi.utils.toObject

// "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Tools\MSVC\14.15.26726\bin\Hostx64\x64\lib" /def:KLaunchpad.def /out:KLaunchpad.lib

// Global arena to store stuff sent to C in
internal val GlobalMemScope = Arena() // TODO clean up anything sent to C

// Library Info

@CName("KLaunchpad_version")
public fun version(): CPointer<ByteVar> = externalFunction {
    return VERSION.cstr.getPointer(GlobalMemScope) // TODO this memory is never freed...
}

// Global Functions

@CName("KLaunchpad_connectToLaunchpadMK2")
public fun connectToLaunchpadMK2(inputDevicePtr: COpaquePointer, outputDevicePtr: COpaquePointer): COpaquePointer = externalFunction {
        val inputDevice = inputDevicePtr.toObject<MidiInputDevice>()
        val outputDevice = outputDevicePtr.toObject<MidiOutputDevice>()
        val launchpad = Launchpad.connectToLaunchpadMK2(inputDevice, outputDevice)
        return StableRef.create(launchpad).asCPointer()
    }

@CName("KLaunchpad_connectToLaunchpadPro")
public fun connectToLaunchpadPro(inputDevicePtr: COpaquePointer, outputDevicePtr: COpaquePointer): COpaquePointer = externalFunction {
        val inputDevice = inputDevicePtr.toObject<MidiInputDevice>()
        val outputDevice = outputDevicePtr.toObject<MidiOutputDevice>()
        val launchpad = Launchpad.connectToLaunchpadPro(inputDevice, outputDevice)
        return StableRef.create(launchpad).asCPointer()
    }

@CName("KLaunchpad_listMidiInputDevices")
public fun listMidiInputDevicesExt(lengthPtr: CPointer<UIntVar>): CPointer<COpaquePointerVar> = externalFunction {
        val opaquePointers = runBlocking { listMidiInputDevices() }.map { StableRef.create(it).asCPointer() }
        lengthPtr.pointed.value = opaquePointers.size.toUInt()
        return createValues<COpaquePointerVar>(opaquePointers.size) { index -> this.value = opaquePointers[index] }.getPointer(GlobalMemScope)
    }

@CName("KLaunchpad_listMidiOutputDevices")
public fun listMidiOutputDevicesExt(lengthPtr: CPointer<UIntVar>): CPointer<COpaquePointerVar> = externalFunction {
        val opaquePointers = runBlocking { listMidiOutputDevices() }.map { StableRef.create(it).asCPointer() }
        lengthPtr.pointed.value = opaquePointers.size.toUInt()
        return createValues<COpaquePointerVar>(opaquePointers.size) { index -> this.value = opaquePointers[index] }.getPointer(GlobalMemScope)
    }

@CName("KLaunchpad_openMidiInputDevice")
public fun openMidiInputDeviceExt(infoPtr: COpaquePointer): COpaquePointer = externalFunction {
    val inputDeviceInfo = infoPtr.toObject<MidiInputDeviceInfo>()
    val inputDevice = try {
        runBlocking { openMidiInputDevice(inputDeviceInfo) }
    } catch (e: Exception) {
        throw e // TODO handle error
    }
    return StableRef.create(inputDevice).asCPointer()
}

@CName("KLaunchpad_openMidiOutputDevice")
public fun openMidiOutputDeviceExt(infoPtr: COpaquePointer): COpaquePointer = externalFunction {
    val outputDeviceInfo = infoPtr.toObject<MidiOutputDeviceInfo>()
    val outputDevice = try {
        runBlocking { openMidiOutputDevice(outputDeviceInfo) }
    } catch (e: Exception) {
        throw e // TODO handle error
    }
    return StableRef.create(outputDevice).asCPointer()
}

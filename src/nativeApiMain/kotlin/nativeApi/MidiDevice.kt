package nativeApi

import com.harry1453.klaunchpad.api.MidiInputDeviceInfo
import com.harry1453.klaunchpad.api.MidiOutputDeviceInfo
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import nativeApi.utils.externalFunction
import nativeApi.utils.toObject

@CName("KLaunchpad_midiInputDeviceInfo_getName")
fun getInputDeviceInfoName(infoPtr: COpaquePointer): CPointer<ByteVar> = externalFunction {
    val midiInputDeviceInfo = infoPtr.toObject<MidiInputDeviceInfo>()
    return midiInputDeviceInfo.name.cstr.getPointer(GlobalMemScope)
}

@CName("KLaunchpad_midiInputDeviceInfo_getVersion")
fun getInputDeviceInfoVersion(infoPtr: COpaquePointer): CPointer<ByteVar> = externalFunction {
    val midiInputDeviceInfo = infoPtr.toObject<MidiInputDeviceInfo>()
    return midiInputDeviceInfo.version.cstr.getPointer(GlobalMemScope)
}

@CName("KLaunchpad_midiOutputDeviceInfo_getName")
fun getOutputDeviceInfoName(infoPtr: COpaquePointer): CPointer<ByteVar> = externalFunction {
    val midiOutputDeviceInfo = infoPtr.toObject<MidiOutputDeviceInfo>()
    return midiOutputDeviceInfo.name.cstr.getPointer(GlobalMemScope)
}

@CName("KLaunchpad_midiOutputDeviceInfo_getVersion")
fun getOutputDeviceInfoVersion(infoPtr: COpaquePointer): CPointer<ByteVar> = externalFunction {
    val midiOutputDeviceInfo = infoPtr.toObject<MidiOutputDeviceInfo>()
    return midiOutputDeviceInfo.version.cstr.getPointer(GlobalMemScope)
}

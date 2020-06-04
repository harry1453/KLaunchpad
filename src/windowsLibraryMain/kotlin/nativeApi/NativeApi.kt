package nativeApi

import com.harry1453.klaunchpad.api.*
import kotlinx.cinterop.*

// "C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\VC\Tools\MSVC\14.15.26726\bin\Hostx64\x64\lib" /def:KLaunchpad.def /out:KLaunchpad.lib

// Global arena to store stuff sent to C in
val GlobalMemScope = Arena()

// Library Info

@CName("KLaunchpad_version")
fun version(): CPointer<ByteVar> = externalFunction {
    return VERSION.cstr.getPointer(GlobalMemScope)
}

// Constructors

@CName("KLaunchpad_connectToLaunchpadMK2")
fun connectToLaunchpadMK2(inputDevicePtr: COpaquePointer, outputDevicePtr: COpaquePointer): COpaquePointer = externalFunction {
    val inputDevice = inputDevicePtr.toObject<MidiInputDevice>()
    val outputDevice = outputDevicePtr.toObject<MidiOutputDevice>()
    val launchpad = Launchpad.connectToLaunchpadMK2(inputDevice, outputDevice)
    return StableRef.create(launchpad).asCPointer()
}

@CName("KLaunchpad_connectToLaunchpadPro")
fun connectToLaunchpadPro(inputDevicePtr: COpaquePointer, outputDevicePtr: COpaquePointer): COpaquePointer = externalFunction {
    val inputDevice = inputDevicePtr.toObject<MidiInputDevice>()
    val outputDevice = outputDevicePtr.toObject<MidiOutputDevice>()
    val launchpad = Launchpad.connectToLaunchpadPro(inputDevice, outputDevice)
    return StableRef.create(launchpad).asCPointer()
}

// Launchpad Member Functions

@CName("KLaunchpad_gridColumnCount")
fun gridColumnCount(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridColumnCount.toUInt()
}

@CName("KLaunchpad_gridColumnStart")
fun gridColumnStart(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridColumnStart.toUInt()
}

@CName("KLaunchpad_gridRowCount")
fun gridRowCount(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridRowCount.toUInt()
}

@CName("KLaunchpad_gridRowStart")
fun gridRowStart(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridRowStart.toUInt()
}

@CName("KLaunchpad_setPadButtonListener")
fun setPadButtonListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(pad: COpaquePointer, pressed: Boolean, velocity: Byte) -> Unit>>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    if (listener.rawValue.toLong() == 0L) {
        launchpad.setPadButtonListener(null)
    } else {
        launchpad.setPadButtonListener { pad, pressed, velocity -> listener(pad.toPointer(), pressed, velocity)}
    }
}

@CName("KLaunchpad_setPadLight")
fun setPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.setPadLight(padPtr.toPad(), colorPtr.toColor())
}

@CName("KLaunchpad_clearPadLight")
fun clearPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.clearPadLight(padPtr.toPad())
}

@CName("KLaunchpad_flashPadLightOnAndOff")
fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color1Ptr: COpaquePointer, color2Ptr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.flashPadLight(padPtr.toPad(), color1Ptr.toColor(), color2Ptr.toColor())
}

@CName("KLaunchpad_flashPadLight")
fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.flashPadLight(padPtr.toPad(), colorPtr.toColor())
}

@CName("KLaunchpad_pulsePadLight")
fun pulsePadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.pulsePadLight(padPtr.toPad(), colorPtr.toColor())
}

@CName("KLaunchpad_batchSetPadLights")
fun batchSetPadLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, Color>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    // TODO
}

@CName("KLaunchpad_batchSetRowLights")
fun batchSetRowLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, Color>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    // TODO
}

@CName("KLaunchpad_batchSetColumnLights")
fun batchSetColumnLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, Color>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    // TODO
}

@CName("KLaunchpad_setAllPadLights")
fun setAllPadLights(launchpadPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.setAllPadLights(colorPtr.toColor())
}

@CName("KLaunchpad_clearAllPadLights")
fun clearAllPadLights(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.clearAllPadLights()
}

@CName("KLaunchpad_scrollText")
fun scrollText(launchpadPtr: COpaquePointer, text: CPointer<ByteVar>, colorPtr: COpaquePointer, loop: Boolean) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.scrollText(text.toKString(), colorPtr.toColor(), loop)
}

@CName("KLaunchpad_stopScrollingText")
fun stopScrollingText(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.stopScrollingText()
}

@CName("KLaunchpad_setTextScrollFinishedListener")
fun setTextScrollFinishedListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(() -> Unit)>>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    if (listener.rawValue.toLong() == 0L) {
        launchpad.setTextScrollFinishedListener(null)
    } else {
        launchpad.setTextScrollFinishedListener { listener() }
    }
}

@CName("KLaunchpad_isAutoClockEnabled")
fun isAutoClockEnabled(launchpadPtr: COpaquePointer): Boolean = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.autoClockEnabled
}

@CName("KLaunchpad_setAutoClockEnabled")
fun setAutoClockEnabled(launchpadPtr: COpaquePointer, autoClockEnabled: Boolean) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.autoClockEnabled = autoClockEnabled
}

@CName("KLaunchpad_getAutoClockTempo")
fun getAutoClockTempo(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.autoClockTempo.toUInt()
}

@CName("KLaunchpad_setAutoClockTempo")
fun setAutoClockTempo(launchpadPtr: COpaquePointer, autoClockTempo: UInt) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.autoClockTempo = autoClockTempo.toInt()
}

// TODO auto clock tempo range

@CName("KLaunchpad_clock")
fun clock(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.clock()
}

@CName("KLaunchpad_enterBootloader")
fun enterBootloader(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.enterBootloader()
}

@CName("KLaunchpad_getPad")
fun getPad(launchpadPtr: COpaquePointer, x: Int, y: Int): COpaquePointer? = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return StableRef.create(launchpad.getPad(x, y) ?: return null).asCPointer()
}

@CName("KLaunchpad_getMaximumNumberOfFaders")
fun getMaximumNumberOfFaders(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.maxNumberOfFaders.toUInt()
}

@CName("KLaunchpad_setupFaderView")
fun setupFaderView(launchpadPtr: COpaquePointer, faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean = false) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    // TODO
}

@CName("KLaunchpad_updateFader")
fun updateFader(launchpadPtr: COpaquePointer, faderIndex: UInt, value: Byte) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.updateFader(faderIndex.toInt(), value)
}

@CName("KLaunchpad_setFaderUpdateListener")
fun setFaderUpdateListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<((faderIndex: Int, faderValue: Byte) -> Unit)>>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    if (listener.rawValue.toLong() == 0L) {
        launchpad.setFaderUpdateListener(null)
    } else {
        launchpad.setFaderUpdateListener { faderIndex, faderValue ->  listener(faderIndex, faderValue) }
    }
}

@CName("KLaunchpad_exitFaderView")
fun exitFaderView(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.exitFaderView()
}

// Pad Member Functions

@CName("KLaunchpad_getX")
fun getX(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridX
}

@CName("KLaunchpad_getY")
fun getY(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridY
}

// Destructors

@CName("KLaunchpad_isClosed")
fun isClosed(launchpadPtr: COpaquePointer): Boolean = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.isClosed
}

@CName("KLaunchpad_closeLaunchpad")
fun closeLaunchpad(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.close()
    launchpadPtr.asStableRef<Launchpad>().dispose()
}

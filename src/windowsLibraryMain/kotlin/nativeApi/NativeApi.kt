package nativeApi

import com.harry1453.klaunchpad.api.Launchpad
import kotlinx.cinterop.*
import kotlinx.coroutines.runBlocking

// Constructors

fun connectToLaunchpadMK2(): COpaquePointer = externalFunction {
    val launchpad = runBlocking { Launchpad.connectToLaunchpadMK2Async().await() }
    return StableRef.create(launchpad).asCPointer()
}

fun connectToLaunchpadPro(): COpaquePointer = externalFunction {
    val launchpad = runBlocking { Launchpad.connectToLaunchpadProAsync().await() }
    return StableRef.create(launchpad).asCPointer()
}

// Launchpad Member Functions

fun gridColumnCount(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridColumnCount.toUInt()
}

fun gridColumnStart(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridColumnStart.toUInt()
}

fun gridRowCount(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridRowCount.toUInt()
}

fun gridRowStart(launchpadPtr: COpaquePointer): UInt = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.gridRowStart.toUInt()
}

fun setPadButtonListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(pad: COpaquePointer, pressed: Boolean, velocity: Byte) -> Unit>>) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    if (listener.rawValue.toLong() == 0L) {
        launchpad.setPadButtonListener(null)
    } else {
        launchpad.setPadButtonListener { pad, pressed, velocity -> listener(pad.toPointer(), pressed, velocity)}
    }
}

fun setPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.setPadLight(padPtr.toPad(), colorPtr.toColor())
}

fun clearPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.clearPadLight(padPtr.toPad())
}

fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color1Ptr: COpaquePointer, color2Ptr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.flashPadLight(padPtr.toPad(), color1Ptr.toColor(), color2Ptr.toColor())
}

// TODO are overloads alright when we're using them like this?
fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.flashPadLight(padPtr.toPad(), colorPtr.toColor())
}

fun pulsePadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, colorPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.pulsePadLight(padPtr.toPad(), colorPtr.toColor())
}

// Pad Member Functions

fun getX(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridX
}

fun getY(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridY
}

// Destructors

fun isClosed(launchpadPtr: COpaquePointer): Boolean = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    return launchpad.isClosed
}

fun closeLaunchpad(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.close()
    launchpadPtr.asStableRef<Launchpad>().dispose()
}

package nativeApi

import kotlinx.cinterop.COpaquePointer
import nativeApi.utils.externalFunctionWithPad

@CName("KLaunchpad_getX")
fun getX(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridX
}

@CName("KLaunchpad_getY")
fun getY(padPtr: COpaquePointer): Int = externalFunctionWithPad(padPtr) { pad ->
    return pad.gridY
}

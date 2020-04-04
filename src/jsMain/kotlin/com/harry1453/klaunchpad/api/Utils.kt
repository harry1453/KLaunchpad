package com.harry1453.klaunchpad.api

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

internal fun Uint8Array.toByteArray(): ByteArray {
    val byteArray = ByteArray(this.length)
    for (i in 0 until length) {
        byteArray[i] = this[i]
    }
    return byteArray
}

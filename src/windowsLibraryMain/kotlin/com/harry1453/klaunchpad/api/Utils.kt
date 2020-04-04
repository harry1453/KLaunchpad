package com.harry1453.klaunchpad.api

/**
 * Convert an LE Int to a Byte Array
 */
fun Int.toBytesLE(): ByteArray {
    val target = ByteArray(4)
    for (index in 0..3) {
        target[index] = (this shr 8 * index and 0xFF).toByte()
    }
    return target
}

/**
 * Convert a Byte Array to an LE Int
 */
fun ByteArray.toIntLE(): Int { // TODO length 0-4, toUint
    val bytes = this
    return ((bytes[0].toInt() and 0xFF)
        or (bytes[1].toInt() and 0xFF shl 8)
        or (bytes[2].toInt() and 0xFF shl 16)
        or (bytes[3].toInt() and 0xFF shl 24))
}

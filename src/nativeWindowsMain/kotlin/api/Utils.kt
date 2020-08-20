package api

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
fun ByteArray.toUintLE(): UInt { // TODO length 0-4, toUint
    val bytes = this
    return ((bytes[0].toUInt() and 0xFF.toUInt())
        or (bytes[1].toUInt() and 0xFF.toUInt() shl 8)
        or (bytes[2].toUInt() and 0xFF.toUInt() shl 16)
        or (bytes[3].toUInt() and 0xFF.toUInt() shl 24))
}

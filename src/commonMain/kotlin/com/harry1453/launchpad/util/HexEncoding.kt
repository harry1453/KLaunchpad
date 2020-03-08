package com.harry1453.launchpad.util

fun String.parseHexString(): ByteArray {
    return ByteArray(this.length / 2) { index ->
        this.substring(index * 2 .. index * 2 + 1).toUByte(radix = 16).toByte()
    }
}

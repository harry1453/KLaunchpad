package com.harry1453.launchpad.util

interface Closeable {
    val isClosed: Boolean
    fun close()
}

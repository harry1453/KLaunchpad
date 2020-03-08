package com.harry1453.launchpad.util

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface Closeable {
    val isClosed: Boolean
    fun close()
}

inline fun <C : Closeable, T> use(closeable: C, action: (C) -> T) {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    try {
        action(closeable)
    } finally {
        closeable.close()
    }
}

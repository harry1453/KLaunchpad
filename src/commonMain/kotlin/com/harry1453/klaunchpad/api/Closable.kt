package com.harry1453.klaunchpad.api

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public interface Closable {
    public val isClosed: Boolean
    public fun close()
}

public inline fun <C : Closable, T> use(closeable: C, action: (C) -> T) {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    try {
        action(closeable)
    } finally {
        closeable.close()
    }
}

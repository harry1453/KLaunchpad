package com.harry1453.launchpad.impl.util

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

actual fun <T> Deferred<T>.blockingAwait(): T {
    return runBlocking { this@blockingAwait.await() }
}

package com.harry1453.launchpad.impl.util

import kotlinx.coroutines.Deferred

actual fun <T> Deferred<T>.blockingAwait(): T {
    TODO("Not yet implemented")
}

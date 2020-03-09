package com.harry1453.launchpad.impl.util

import kotlinx.coroutines.Deferred

expect fun <T> Deferred<T>.blockingAwait(): T

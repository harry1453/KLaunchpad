package com.harry1453.launchpad.api

import kotlinx.coroutines.runBlocking

/**
 * Synchronously connect to a Launchpad MK2.
 * @throws Exception if we could not connect to a Launchpad MK2
 * TODO support for multiple devices connected
 */
fun Launchpad.Companion.connectToLaunchpadMK2(): Launchpad {
    return runBlocking { connectToLaunchpadMK2Async().await() }
}

/**
 * Synchronously connect to a Launchpad Pro.
 * @throws Exception if we could not connect to a Launchpad Pro
 * TODO support for multiple devices connected
 */
fun Launchpad.Companion.connectToLaunchpadPro(): Launchpad {
    return runBlocking { connectToLaunchpadProAsync().await() }
}

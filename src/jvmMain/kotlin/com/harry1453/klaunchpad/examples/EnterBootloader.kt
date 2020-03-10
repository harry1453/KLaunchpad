@file:JvmName("EnterBootloader")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

fun main() {
    val launchpad = Launchpad.connectToLaunchpadMK2()
    launchpad.enterBootloader()
}

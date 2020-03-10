@file:JvmName("EnterBootloader")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2
import com.harry1453.klaunchpad.api.use

fun main() {
    use(Launchpad.connectToLaunchpadMK2()) { launchpad ->
        launchpad.enterBootloader()
    }
}

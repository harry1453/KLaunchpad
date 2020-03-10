@file:JvmName("HelloWorld")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2
import com.harry1453.klaunchpad.api.use

fun main() {
    use(Launchpad.connectToLaunchpadMK2()) { launchpad ->
        launchpad.scrollText("Hello World!", Color(255, 255, 255), loop = true)
    }
}

@file:JvmName("HelloWorld")

package com.harry1453.launchpad.examples

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.connectToLaunchpadMK2
import com.harry1453.launchpad.api.use

fun main() {
    use(Launchpad.connectToLaunchpadMK2()) { launchpad ->
        launchpad.scrollText("Hello World!", Color(255, 255, 255), loop = true)
    }
}

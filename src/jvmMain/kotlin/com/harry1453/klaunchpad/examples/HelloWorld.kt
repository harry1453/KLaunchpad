@file:JvmName("HelloWorld")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

fun main() {
    val launchpad = Launchpad.connectToLaunchpadMK2()
    launchpad.scrollText("Hello World!", Color(255, 255, 255), loop = true)
}

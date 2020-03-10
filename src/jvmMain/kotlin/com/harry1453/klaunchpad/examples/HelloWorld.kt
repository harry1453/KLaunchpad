@file:JvmName("HelloWorld")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

fun main() {
    val launchpad = Launchpad.connectToLaunchpadMK2()
    launchpad.scrollText("{s7}Hello {s3}World!", Color(0, 255, 0), loop = true)
}

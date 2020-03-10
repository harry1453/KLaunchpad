@file:JvmName("DifferentLightingModes")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

fun main() {
    val launchpad = Launchpad.connectToLaunchpadMK2()
    val red = Color(255, 0, 0)
    val green = Color(0, 255, 0)
    val blue = Color(0, 0, 255)
    launchpad.autoClockEnabled = true
    launchpad.autoClockTempo = 60
    launchpad.flashPadLight(launchpad.getPad(0, 0)!!, red)
    launchpad.setPadLight(launchpad.getPad(1, 0)!!, green)
    launchpad.pulsePadLight(launchpad.getPad(2, 0)!!, blue)
}

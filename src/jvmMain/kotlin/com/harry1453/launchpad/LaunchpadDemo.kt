@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad
import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2Pad

fun main() {
    val color = Color(0, 50, 255)
    val launchpad = Launchpad.connectToLaunchpadMK2()
    launchpad.setPadButtonListener { _, pressed, _ ->
        if (pressed) {
            for (pad in LaunchpadMk2Pad.values()) {
                launchpad.setPadLight(pad, color)
            }
        } else {
            for (pad in LaunchpadMk2Pad.values()) {
                launchpad.setPadLight(pad, Color.BLACK)
            }
        }
    }
}

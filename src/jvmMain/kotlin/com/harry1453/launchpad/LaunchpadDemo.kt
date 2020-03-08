@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setPadLightColour(pad, colour)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

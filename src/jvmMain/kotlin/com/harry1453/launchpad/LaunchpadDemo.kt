@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2
import com.harry1453.launchpad.impl.LaunchpadMk2Pad

fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    launchpad.setPadUpdateListener { _, pressed, _ ->
        if (pressed) {
            for (pad in LaunchpadMk2Pad.values()) {
                launchpad.setPadLightColour(pad, colour)
            }
        } else {
            for (pad in LaunchpadMk2Pad.values()) {
                launchpad.setPadLightColour(pad, Colour.BLACK)
            }
        }
    }
}

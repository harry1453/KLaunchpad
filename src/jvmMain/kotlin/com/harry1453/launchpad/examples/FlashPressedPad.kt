package com.harry1453.launchpad.examples

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

fun main() {
    val colour1 = Colour(0, 50, 255)
    val colour2 = Colour(255, 50, 50)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.flashLightBetween(pad, colour1, colour2)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

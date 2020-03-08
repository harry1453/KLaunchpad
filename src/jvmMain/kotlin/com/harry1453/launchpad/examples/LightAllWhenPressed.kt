package com.harry1453.launchpad.examples

import com.harry1453.launchpad.Launchpad
import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

/**
 * This example demonstrates updating all pads.
 */
fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadUpdateListener { _, pressed, _ ->
        if (pressed) {
            launchpad.setAllPads(colour)
        } else {
            launchpad.setAllPads(Colour.BLACK)
        }
    }
}

package com.harry1453.launchpad.examples
import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2

/**
 * This example demonstrates updating all pads.
 */
fun main() {
    val color = Color(0, 50, 255)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { _, pressed, _ ->
        if (pressed) {
            launchpad.setAllPadLights(color)
        } else {
            launchpad.setAllPadLights(Color.BLACK)
        }
    }
}

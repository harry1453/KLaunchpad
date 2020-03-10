@file:JvmName("LightAllWhenPressedd")

package com.harry1453.launchpad.examples

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.connectToLaunchpadMK2

/**
 * This example demonstrates updating all pads.
 */
fun main() {
    val color = Color(0, 50, 255)
    val launchpad = Launchpad.connectToLaunchpadMK2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { _, pressed, _ ->
        if (pressed) {
            launchpad.setAllPadLights(color)
        } else {
            launchpad.setAllPadLights(Color.BLACK)
        }
    }
}

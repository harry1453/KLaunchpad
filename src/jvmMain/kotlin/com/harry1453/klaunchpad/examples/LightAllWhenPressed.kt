@file:JvmName("LightAllWhenPressedd")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2
import kotlin.random.Random

/**
 * This example demonstrates updating all pads.
 */
fun main() {
    val random = Random(System.currentTimeMillis())
    val launchpad = Launchpad.connectToLaunchpadMK2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { _, pressed, _ ->
        val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
        if (pressed) {
            launchpad.setAllPadLights(color)
        } else {
            launchpad.setAllPadLights(Color.BLACK)
        }
    }
}

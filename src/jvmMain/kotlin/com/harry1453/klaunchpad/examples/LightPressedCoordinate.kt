@file:JvmName("LightPressedCoordinate")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2
import kotlin.random.Random

/**
 * This example demonstrates updating rows and columns.
 */
fun main() {
    val random = Random(System.currentTimeMillis())
    val launchpad = Launchpad.connectToLaunchpadMK2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { pad, pressed, _ ->
        val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
        if (pressed) {
            launchpad.batchSetRowLights(mapOf(pad.gridY to color))
            launchpad.batchSetColumnLights(mapOf(pad.gridX to color))
        } else {
            launchpad.batchSetRowLights(mapOf(pad.gridY to Color.BLACK))
            launchpad.batchSetColumnLights(mapOf(pad.gridX to Color.BLACK))
        }
    }
}

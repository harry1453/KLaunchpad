@file:JvmName("LightPressedCoordinate")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

/**
 * This example demonstrates updating rows and columns.
 */
fun main() {
    val color = Color(0, 50, 255)
    val launchpad = Launchpad.connectToLaunchpadMK2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.batchSetRowLights(mapOf(pad.gridY to color))
            launchpad.batchSetColumnLights(mapOf(pad.gridX to color))
        } else {
            launchpad.batchSetRowLights(mapOf(pad.gridY to Color.BLACK))
            launchpad.batchSetColumnLights(mapOf(pad.gridX to Color.BLACK))
        }
    }
}

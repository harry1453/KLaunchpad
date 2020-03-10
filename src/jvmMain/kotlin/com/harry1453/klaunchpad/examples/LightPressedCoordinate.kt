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
            launchpad.batchSetRowLights(listOf(Pair(pad.gridY, color)))
            launchpad.batchSetColumnLights(listOf(Pair(pad.gridX, color)))
        } else {
            launchpad.batchSetRowLights(listOf(Pair(pad.gridY, Color.BLACK)))
            launchpad.batchSetColumnLights(listOf(Pair(pad.gridX, Color.BLACK)))
        }
    }
}

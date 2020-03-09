@file:JvmName("LightAroundPressedPad")

package com.harry1453.launchpad.examples

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import kotlin.random.Random

/**
 * This example demonstrates bulk updating pads.
 */
fun main() {
    val random = Random(System.currentTimeMillis())
    val launchpad = Launchpad.connectToLaunchpadMK2(userMode = true)
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { pad, pressed, _ ->
        val padAbove = launchpad.getPad(pad.gridX, pad.gridY + 1)
        val padBelow = launchpad.getPad(pad.gridX, pad.gridY - 1)
        val padLeft = launchpad.getPad(pad.gridX - 1, pad.gridY)
        val padRight = launchpad.getPad(pad.gridX + 1, pad.gridY)
        val pads = listOfNotNull(pad, padAbove, padBelow, padLeft, padRight)
        if (pressed) {
            val color = Color(random.nextInt(1, 128), random.nextInt(1, 128), random.nextInt(1, 128))
            launchpad.batchSetPadLights(pads.map { Pair(it, color) })
        } else {
            launchpad.batchSetPadLights(pads.map { Pair(it, Color.BLACK) })
        }
    }
}

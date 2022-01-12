@file:JvmName("LightAroundPressedPad")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.open
import kotlin.random.Random

/**
 * This example demonstrates bulk updating pads.
 */
suspend fun main() {
    val inputDeviceInfo = Launchpad.listMidiInputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI input!")
    val outputDeviceInfo = Launchpad.listMidiOutputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI output!")
    val launchpad = Launchpad.connectToLaunchpadMK2(inputDeviceInfo.open(), outputDeviceInfo.open())
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })

    val random = Random(System.currentTimeMillis())
    launchpad.setPadButtonListener { pad, pressed, _ ->
        val padAbove = launchpad.getPad(pad.gridX, pad.gridY + 1)
        val padBelow = launchpad.getPad(pad.gridX, pad.gridY - 1)
        val padLeft = launchpad.getPad(pad.gridX - 1, pad.gridY)
        val padRight = launchpad.getPad(pad.gridX + 1, pad.gridY)
        val pads = listOf(pad, padAbove, padBelow, padLeft, padRight)
        if (pressed) {
            val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
            launchpad.batchSetPadLights(pads.map { it to color }.toMap())
        } else {
            launchpad.batchSetPadLights(pads.map { it to Color.BLACK }.toMap())
        }
    }
}

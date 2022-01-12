@file:JvmName("LightAllWhenPressedd")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.open
import kotlin.random.Random

/**
 * This example demonstrates updating all pads.
 */
suspend fun main() {
    val inputDeviceInfo = Launchpad.listMidiInputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI input!")
    val outputDeviceInfo = Launchpad.listMidiOutputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI output!")
    val launchpad = Launchpad.connectToLaunchpadMK2(inputDeviceInfo.open(), outputDeviceInfo.open())
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })

    val random = Random(System.currentTimeMillis())
    launchpad.setPadButtonListener { _, pressed, _ ->
        val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
        if (pressed) {
            launchpad.setAllPadLights(color)
        } else {
            launchpad.setAllPadLights(Color.BLACK)
        }
    }
}

@file:JvmName("DifferentLightingModes")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.open

suspend fun main() {
    val inputDeviceInfo = Launchpad.listMidiInputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI input!")
    val outputDeviceInfo = Launchpad.listMidiOutputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI output!")
    val launchpad = Launchpad.connectToLaunchpadMK2(inputDeviceInfo.open(), outputDeviceInfo.open())
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })

    val red = Color(255, 0, 0)
    val green = Color(0, 255, 0)
    val blue = Color(0, 0, 255)
    launchpad.autoClockEnabled = true
    launchpad.autoClockTempo = 60
    launchpad.flashPadLight(launchpad.getPad(0, 0), red)
    launchpad.setPadLight(launchpad.getPad(1, 0), green)
    launchpad.pulsePadLight(launchpad.getPad(2, 0), blue)
}

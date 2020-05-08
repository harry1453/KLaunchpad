@file:JvmName("FlashPressedPad")

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

    val color1 = Color(0, 0, 255)
    val color2 = Color(255, 0, 0)
    launchpad.autoClockTempo = 60
    launchpad.autoClockEnabled = true
    launchpad.setPadButtonListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.flashPadLight(pad, color1, color2)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

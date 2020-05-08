@file:JvmName("EnterBootloader")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.open

suspend fun main() {
    val inputDeviceInfo = Launchpad.listMidiInputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI input!")
    val outputDeviceInfo = Launchpad.listMidiOutputDevices()
        .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI output!")
    val launchpad = Launchpad.connectToLaunchpadMK2(inputDeviceInfo.open(), outputDeviceInfo.open())

    launchpad.enterBootloader()
}

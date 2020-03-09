@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.MidiDevice
import com.harry1453.launchpad.api.openMidiDevice
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2Pad
import com.harry1453.launchpad.impl.toVelocity
import com.harry1453.launchpad.impl.util.parseHexString
import com.harry1453.launchpad.impl.util.toHexString
import com.harry1453.launchpad.impl.util.plus

val color = Color(0, 50, 255)
var faderIndex = 4

fun main() {
    val launchpad = openMidiDevice {
        it.name.toLowerCase().contains("launchpad mk2")
    }.apply {
        setMessageListener {
            println("Received message: ${it.toHexString()}")
        }
    }
    // Enter fader layout
    launchpad.sendSysEx("F000202902182204F7".parseHexString())
    setupFaders(launchpad)
}

fun setupFaders(launchpad: MidiDevice) {
    // Setup up volume fader with colour 69 and initial value whatever
    launchpad.sendSysEx("F000202902182B".parseHexString() + faderIndex + 0x00 + color.toVelocity() + 0x5F + 0xF7)
}

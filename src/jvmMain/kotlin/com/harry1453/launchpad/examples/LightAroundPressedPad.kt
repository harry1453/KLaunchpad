package com.harry1453.launchpad.examples

import com.harry1453.launchpad.Launchpad
import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

/**
 * This example demonstrates bulk updating pads.
 */
fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        val padAbove = launchpad.findPad(pad.gridX, pad.gridY + 1)
        val padBelow = launchpad.findPad(pad.gridX, pad.gridY - 1)
        val padLeft = launchpad.findPad(pad.gridX - 1, pad.gridY)
        val padRight = launchpad.findPad(pad.gridX + 1, pad.gridY)
        val pads = listOfNotNull(pad, padAbove, padBelow, padLeft, padRight)
        if (pressed) {
            launchpad.setPadLightColourBulk(pads.map { Pair(it, colour) }, Launchpad.BulkUpdateMode.PULSE)
        } else {
            launchpad.setPadLightColourBulk(pads.map { Pair(it, Colour.BLACK) })
        }
    }
}

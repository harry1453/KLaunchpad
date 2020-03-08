package com.harry1453.launchpad.examples

import com.harry1453.launchpad.Launchpad
import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

/**
 * This example demonstrates updating rows and columns.
 */
fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setRowColourBulk(listOf(Pair(pad.gridY, colour)))
            launchpad.setColumnColourBulk(listOf(Pair(pad.gridX, colour)))
        } else {
            launchpad.setRowColourBulk(listOf(Pair(pad.gridY, Colour.BLACK)))
            launchpad.setColumnColourBulk(listOf(Pair(pad.gridX, Colour.BLACK)))
        }
    }
}

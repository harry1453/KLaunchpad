@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2
import com.harry1453.launchpad.impl.LaunchpadMk2Pads

fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setPadLightColourBulk(listOf(Pair(pad, colour), Pair(LaunchpadMk2Pads.T8, colour)), Launchpad.BulkUpdateMode.FLASH)
        } else {
            launchpad.clearPadLight(pad)
            launchpad.clearPadLight(LaunchpadMk2Pads.T8)
        }
    }
}

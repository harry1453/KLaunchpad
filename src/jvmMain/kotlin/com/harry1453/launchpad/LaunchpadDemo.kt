@file:JvmName("LaunchpadDemo")

package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2

fun main() {
    val colour = Colour(0, 50, 255)
    val launchpad = LaunchpadMk2()
    launchpad.scrollText("{s7} Your Mum Gay", Colour(255, 0, 0), true)
    var scrollCount = 0
    launchpad.setTextScrollFinishedListener {
        scrollCount++
        if (scrollCount == 2) {
            launchpad.stopScrollingText()
        }
    }
    launchpad.setPadUpdateListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setPadLightColour(pad, colour, Channel.Channel3)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

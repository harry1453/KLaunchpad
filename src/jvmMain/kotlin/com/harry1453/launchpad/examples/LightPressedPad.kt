package com.harry1453.launchpad.examples

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2

fun main() {
    val color = Color(0, 50, 255)
    val launchpad = LaunchpadMk2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
    launchpad.setPadButtonListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setPadLight(pad, color)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

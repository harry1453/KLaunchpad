package com.harry1453.launchpad.examples

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.impl.LaunchpadMk2
import com.harry1453.launchpad.util.use

fun main() {
    use(LaunchpadMk2()) { launchpad ->
        launchpad.scrollText("Hello World!", Colour(255, 255, 255), loop = true)
    }
}

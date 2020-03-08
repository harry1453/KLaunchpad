package com.harry1453.launchpad.examples
import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.impl.mk2.LaunchpadMk2
import com.harry1453.launchpad.api.use

fun main() {
    use(LaunchpadMk2()) { launchpad ->
        launchpad.scrollText("Hello World!", Color(255, 255, 255), loop = true)
    }
}

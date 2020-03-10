@file:JvmName("Faders")

package com.harry1453.klaunchpad.examples

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2

// Try changing this value to observe the different modes.
const val bipolarMode = true

@Suppress("ConstantConditionIf")
fun main() {
    val color1 = Color(0, 0, 255)
    val color2 = Color(255, 100, 50)
    val color3 = Color(0, 50, 255)
    val color4 = Color(255, 50, 255)
    val color5 = Color(0, 255, 0)
    val color6 = Color(255, 50, 0)
    val color7 = Color(200, 200, 255)
    val color8 = Color(255, 0, 0)
    val color9 = Color(0, 255, 255)

    var fader0value: Byte = 15.toByte()
    var fader1value: Byte = 31.toByte()
    var fader2value: Byte = 47.toByte()
    var fader3value: Byte = 63.toByte()
    var fader4value: Byte = 79.toByte()
    var fader5value: Byte = 95.toByte()
    var fader6value: Byte = 111.toByte()
    var fader7value: Byte = 127.toByte()

    if (bipolarMode) {
        fader0value = (fader0value - 63).toByte()
        fader1value = (fader1value - 63).toByte()
        fader2value = (fader2value - 63).toByte()
        fader3value = (fader3value - 63).toByte()
        fader4value = (fader4value - 63).toByte()
        fader5value = (fader5value - 63).toByte()
        fader6value = (fader6value - 63).toByte()
        fader7value = (fader7value - 63).toByte()
    }

    var faderMode = true

    fun faderValueToString(faderValue: Byte): String {
        val value = faderValue.toString()
        return when(value.length) {
            1 -> "  $value"
            2 -> " $value"
            else -> value
        }
    }

    fun printFaderValues() {
        print("\rFader 1: ${faderValueToString(fader0value)}, Fader 2: ${faderValueToString(fader1value)}, Fader 3: ${faderValueToString(fader2value)}, Fader 4: ${faderValueToString(fader3value)}, Fader 5: ${faderValueToString(fader4value)}, Fader 6: ${faderValueToString(fader5value)}, Fader 7: ${faderValueToString(fader6value)}, Fader 8: ${faderValueToString(fader7value)}")
    }

    val launchpad = Launchpad.connectToLaunchpadMK2()
    Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })

    fun exitFaderView() {
        launchpad.exitFaderView()
        launchpad.setPadLight(launchpad.getPad(4, 8)!!, Color(0, 255, 0))
        launchpad.setPadLight(launchpad.getPad(7, 8)!!, Color(0, 0, 50))
        faderMode = false
    }

    fun setupFaderView() {
        launchpad.setupFaderView(mapOf(
            0 to Pair(color1, fader0value),
            1 to Pair(color2, fader1value),
            2 to Pair(color3, fader2value),
            3 to Pair(color4, fader3value),
            4 to Pair(color5, fader4value),
            5 to Pair(color6, fader5value),
            6 to Pair(color7, fader6value),
            7 to Pair(color8, fader7value)
        ), bipolar = bipolarMode)
        launchpad.setPadLight(launchpad.getPad(4, 8)!!, Color(0, 50, 0))
        launchpad.setPadLight(launchpad.getPad(7, 8)!!, Color(0, 0, 255))

        // We can't do a bulk update because it bugs the launchpad (fader lights don't update)
        (0..7).forEach {
            val pad = launchpad.getPad(8, it) ?: return@forEach
            launchpad.setPadLight(pad, color9)
        }

        faderMode = true
    }

    launchpad.setPadButtonListener { pad, pressed, _ ->
        if (!faderMode && !(pad.gridY == 8 && (pad.gridX == 4 || pad.gridX == 7))) { // Don't update the mode buttons when not in fader mode
            if (pressed) {
                launchpad.setPadLight(pad, color5)
            } else {
                launchpad.clearPadLight(pad)
            }
        }
        if (pressed) {
            if (pad.gridX == 8) {
                var faderValue = (pad.gridY * 127 / 7).toByte()
                if (bipolarMode) faderValue = (faderValue - 63).toByte()
                (0..7).forEach {
                    launchpad.updateFader(it, faderValue)
                }
                fader0value = faderValue
                fader1value = faderValue
                fader2value = faderValue
                fader3value = faderValue
                fader4value = faderValue
                fader5value = faderValue
                fader6value = faderValue
                fader7value = faderValue
                printFaderValues()
            } else if (pad.gridX == 4 && pad.gridY == 8) { // Session Button
                exitFaderView()
            } else if (pad.gridX == 7 && pad.gridY == 8) { // Mixer Button
                setupFaderView()
            }
        }
    }
    launchpad.setFaderUpdateListener { faderIndex, faderValue ->
        when(faderIndex) {
            0 -> fader0value = faderValue
            1 -> fader1value = faderValue
            2 -> fader2value = faderValue
            3 -> fader3value = faderValue
            4 -> fader4value = faderValue
            5 -> fader5value = faderValue
            6 -> fader6value = faderValue
            7 -> fader7value = faderValue
        }
        printFaderValues()
    }

    setupFaderView()
    printFaderValues()
}

package com.harry1453.klaunchpad.examples.applications.passcode

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.Pad
import com.harry1453.klaunchpad.api.open
import com.harry1453.klaunchpad.impl.launchpads.mk2.LaunchpadMk2Pad
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

object Passcode {
    private lateinit var launchpad: Launchpad
    private var state = State.LOCKED
    private val lock = Any()

    private val expectedPasscode = listOf(LaunchpadMk2Pad.A8, LaunchpadMk2Pad.B8, LaunchpadMk2Pad.C8, LaunchpadMk2Pad.D8, LaunchpadMk2Pad.A7, LaunchpadMk2Pad.B7, LaunchpadMk2Pad.C7, LaunchpadMk2Pad.D7)
    private val enteredPasscode = CircularFifoList.new<Pad>(8)

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val inputDeviceInfo = Launchpad.listMidiInputDevices()
            .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI input!")
        val outputDeviceInfo = Launchpad.listMidiOutputDevices()
            .firstOrNull { it.name == "Launchpad MK2" } ?: error("Could not find the Launchpad's MIDI output!")
        val launchpad = Launchpad.connectToLaunchpadMK2(inputDeviceInfo.open(), outputDeviceInfo.open())
        Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })

        launchpad.setPadButtonListener { pad, pressed, _ -> padPressed(pad, pressed)}
    }

    private enum class State {
        LOCKED,
        ENTERING_PASSCODE,
        UNLOCKED,
    }

    private fun padPressed(pad: Pad, pressed: Boolean) {
        synchronized(lock) {
            when (state) {
                State.LOCKED -> {
                    padPressedLocked(pad, pressed)
                }
                State.ENTERING_PASSCODE -> {
                    padPressedEnteringPasscode(pad, pressed)
                }
                State.UNLOCKED -> {
                    padPressedUnlocked(pad, pressed)
                }
            }
        }
    }

    private fun padPressedLocked(pad: Pad, pressed: Boolean) {
        if (pressed) {
            launchpad.setAllPadLights(Color(0, 255, 0))
            state = State.ENTERING_PASSCODE
        }
    }

    private fun checkPasscode() {
        if (enteredPasscode == expectedPasscode) {
            launchpad.setAllPadLights(Color.BLACK)
            state = State.UNLOCKED
        }
    }

    private fun padPressedEnteringPasscode(pad: Pad, pressed: Boolean) {
        if (pressed) {
            launchpad.setPadLight(pad, Color(255, 255, 255))
            enteredPasscode.add(pad)
            checkPasscode()
        } else {
            launchpad.setPadLight(pad, Color(0, 255, 0))
        }
    }

    private fun padPressedUnlocked(pad: Pad, pressed: Boolean) {
        if (pressed && pad.gridX == 0 && pad.gridY == 0) {
            state = State.LOCKED
            return
        }

        // Light Around Pressed Pad
        val random = Random(System.currentTimeMillis())
        val padAbove = launchpad.getPad(pad.gridX, pad.gridY + 1)
        val padBelow = launchpad.getPad(pad.gridX, pad.gridY - 1)
        val padLeft = launchpad.getPad(pad.gridX - 1, pad.gridY)
        val padRight = launchpad.getPad(pad.gridX + 1, pad.gridY)
        val pads = listOf(pad, padAbove, padBelow, padLeft, padRight)
        if (pressed) {
            val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
            launchpad.batchSetPadLights(pads.map { it to color }.toMap())
        } else {
            launchpad.batchSetPadLights(pads.map { it to Color.BLACK }.toMap())
        }
    }
}

package com.harry1453.klaunchpad.examples.applications.macros

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.Pad
import com.harry1453.klaunchpad.api.connectToLaunchpadMK2
import com.harry1453.klaunchpad.impl.launchpads.mk2.LaunchpadMk2Pad
import java.awt.Robot
import java.awt.event.KeyEvent

object Macros {
    private val launchpad = Launchpad.connectToLaunchpadMK2()
    private val robot = Robot()

    // Some macros to help in IntelliJ IDEA
    private val macros = mapOf<Pad, MacroDescription>(
        LaunchpadMk2Pad.R8 to MacroDescription(Color(0, 255, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_SHIFT, KeyEvent.VK_F10)), // Run Program
        LaunchpadMk2Pad.R7 to MacroDescription(Color(0, 100, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_F9)), // Resume Program
        LaunchpadMk2Pad.A8 to MacroDescription(Color(0, 255, 0), LightType.PULSE, ShortcutMacro(KeyEvent.VK_SHIFT, KeyEvent.VK_F9)), // Run with Debugger
        LaunchpadMk2Pad.A7 to MacroDescription(Color(255, 0, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_CONTROL, KeyEvent.VK_F2)), // Stop Program
        LaunchpadMk2Pad.A1 to MacroDescription(Color(0, 50, 255), LightType.SOLID, ShortcutMacro(KeyEvent.VK_SHIFT, KeyEvent.VK_F6)), // Refactor -> Rename
        LaunchpadMk2Pad.B1 to MacroDescription(Color(0, 0, 255), LightType.SOLID, ShortcutMacro(KeyEvent.VK_CONTROL, KeyEvent.VK_F6)), // Refactor -> Change Signature
        LaunchpadMk2Pad.A2 to MacroDescription(Color(0, 150, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_F7)), // Find Usages
        LaunchpadMk2Pad.A3 to MacroDescription(Color(255, 0, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_F8)), // Show breakpoints
        LaunchpadMk2Pad.A4 to MacroDescription(Color(255, 204, 0), LightType.SOLID, ShortcutMacro(KeyEvent.VK_ALT, KeyEvent.VK_ENTER)) // Quick Fix
    )

    @JvmStatic
    fun main(args: Array<String>) {
        Runtime.getRuntime().addShutdownHook(Thread { launchpad.close() })
        launchpad.autoClockEnabled = true
        launchpad.autoClockTempo = 60
        setupMacroLights()
        launchpad.setPadButtonListener { pad, pressed, _ -> padUpdate(pad, pressed) }
    }

    private fun setupMacroLights() {
        macros.forEach { (pad, description) -> setupMacroLight(pad, description) }
    }

    private fun setupMacroLight(pad: Pad, description: MacroDescription) {
        when(description.lightType) {
            LightType.SOLID -> launchpad.setPadLight(pad, description.lightColor)
            LightType.FLASH -> launchpad.flashPadLight(pad, description.lightColor)
            LightType.PULSE -> launchpad.pulsePadLight(pad, description.lightColor)
        }
    }

    private fun padUpdate(pad: Pad, pressed: Boolean) {
        val macro = macros[pad] ?: return
        if (pressed) {
            launchpad.setPadLight(pad, Color(255, 255, 255))
            macro.macro.execute(robot)
        } else {
            setupMacroLight(pad, macro)
        }
    }

    private enum class LightType {
        SOLID,
        FLASH,
        PULSE
    }

    private data class MacroDescription(
        val lightColor: Color,
        val lightType: LightType,
        val macro: Macro
    )

    private interface Macro {
        fun execute(robot: Robot)
    }

    private const val delay = 10L // ms

    private class ShortcutMacro(private vararg val keys: Int) : Macro {
        private val reversedKeys = keys.reversed()

        override fun execute(robot: Robot) {
            keys.forEach {
                robot.keyPress(it)
                Thread.sleep(delay)
            }
            reversedKeys.forEach {
                robot.keyRelease(it)
                Thread.sleep(delay)
            }
        }
    }

    private class SequenceMacro(private vararg val keys: Int) : Macro {
        override fun execute(robot: Robot) {
            keys.forEach {
                robot.keyPress(it)
                Thread.sleep(delay)
                robot.keyRelease(it)
                Thread.sleep(delay)
            }
        }
    }
}

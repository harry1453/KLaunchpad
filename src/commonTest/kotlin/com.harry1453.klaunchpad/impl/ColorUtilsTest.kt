package com.harry1453.klaunchpad.impl

import kotlin.test.Test
import kotlin.test.assertTrue

class ColorUtilsTest {
    @Test
    fun testExactMatches() {
        VELOCITY_TO_COLOR.forEachIndexed { index, color ->
            assertTrue(color.toVelocity() == index || VELOCITY_TO_COLOR[color.toVelocity()] == color)
        }
    }
}

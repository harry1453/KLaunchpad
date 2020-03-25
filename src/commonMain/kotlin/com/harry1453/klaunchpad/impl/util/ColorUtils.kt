package com.harry1453.klaunchpad.impl.util

import com.harry1453.klaunchpad.api.Color

/**
 * Square of the difference between two numbers
 */
private fun deltaSquare(l1: UByte, l2: UByte): Long {
    return (l1.toLong() - l2.toLong()) * (l1.toLong() - l2.toLong())
}

/**
 * Produces a value for difference between two colors. Lower result is more similar.
 */
internal fun colorDelta(color1: Color, color2: Color): Long {
    return deltaSquare(color1.r, color2.r) + deltaSquare(color1.g, color2.g) + deltaSquare(color1.b, color2.b)
}

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
    // Using algorithm from https://www.compuphase.com/cmetric.htm

    // Mean of the 2 red values
    val rMean = (color1.r.toLong() + color2.r.toLong()) / 2
    // Red delta square
    val rds = deltaSquare(color1.r, color2.r)
    // Green delta square
    val gds = deltaSquare(color1.g, color2.g)
    // Blue delta square
    val bds = deltaSquare(color1.b, color2.b)

    // We are not taking the square root of the result as we only want to compare 2 colors, we are not looking for linear results.
    return (((512 + rMean) * rds) shr 8) + 4 * gds + (((767 - rMean) * bds) shr 8)
}

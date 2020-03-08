package com.harry1453.launchpad.colour

// I speak the Queen's English.
data class Colour(val r: UByte, val g: UByte, val b: UByte) {
    constructor(r: Int, g: Int, b: Int) : this(r.toUByte(), g.toUByte(), b.toUByte())
    companion object {
        val BLACK = Colour(0.toUByte(), 0.toUByte(), 0.toUByte())
    }
}

/**
 * Square of the difference between two numbers
 */
private fun deltaSquare(l1: UByte, l2: UByte): Long {
    return (l1.toLong() - l2.toLong()) * (l1.toLong() - l2.toLong())
}

/**
 * Produces a value for difference between two colours. Lower result is more similar.
 */
internal fun rgbDifference(colour1: Colour, colour2: Colour): Long {
    // Using algorithm from https://www.compuphase.com/cmetric.htm

    // Mean of the 2 red values
    val rMean = (colour1.r.toLong() + colour2.r.toLong()) / 2
    // Red delta square
    val rds = deltaSquare(colour1.r, colour2.r)
    // Green delta square
    val gds = deltaSquare(colour1.g, colour2.g)
    // Blue delta square
    val bds = deltaSquare(colour1.b, colour2.b)

    // We are not taking the square root of the result as we only want to compare 2 colours, we are not looking for linear results.
    return (((512 + rMean) * rds) shr 8) + 4 * gds + (((767 - rMean) * bds) shr 8)
}

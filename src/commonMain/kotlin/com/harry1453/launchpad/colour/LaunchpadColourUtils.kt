package com.harry1453.launchpad.colour

fun rgbToVelocity(red: Int, green: Int, blue: Int): Int {
    return rgbToVelocity(Colour(red.toUByte(), green.toUByte(), blue.toUByte()))
}

fun rgbToVelocity(colour: Colour): Int {
    var lowestDifference = Long.MAX_VALUE
    var lowestDifferenceIndex: Int = -1

    velocityToColour.forEachIndexed { index, newColour ->
        val newDifference = rgbDifference(colour, newColour)
        if (newDifference < lowestDifference) {
            lowestDifference = newDifference
            lowestDifferenceIndex = index
        }
    }

    check(lowestDifferenceIndex >= 0)
    return lowestDifferenceIndex
}

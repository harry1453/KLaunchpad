package com.harry1453.launchpad.api

data class Color(val r: UByte, val g: UByte, val b: UByte) {
    constructor(r: Int, g: Int, b: Int) : this(r.toUByte(), g.toUByte(), b.toUByte())
    companion object {
        val BLACK = Color(0.toUByte(), 0.toUByte(), 0.toUByte())
    }
}

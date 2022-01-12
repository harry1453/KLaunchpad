package com.harry1453.klaunchpad.api

public data class Color(val r: UByte, val g: UByte, val b: UByte) {
    public constructor(r: Int, g: Int, b: Int) : this(r.toUByte(), g.toUByte(), b.toUByte())
    public companion object {
        public val BLACK: Color = Color(0.toUByte(), 0.toUByte(), 0.toUByte())
    }
}

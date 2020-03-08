package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.util.Closeable

interface Launchpad : Closeable {
    fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit)
    fun setPadLightColour(pad: Pad, colour: Colour, accurateColour: Boolean = false, channel: Channel = Channel.Channel1)
    fun clearPadLight(pad: Pad) = setPadLightColour(pad, Colour.BLACK)

    fun clock()

    fun enterBootloader()

    fun findPad(gridX: Int, gridY: Int): Pad?
    fun mustFindPad(gridX: Int, gridY: Int): Pad = findPad(gridX, gridY)!!

    /*
    TODO: Layout Selection, Scroll text across pads, Faders, Bulk update LEDs (all variants), device enquiry, version enquiry
     */
}

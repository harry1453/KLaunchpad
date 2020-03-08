package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.util.Closeable

interface Launchpad : Closeable {
    fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit)
    fun setPadLightColour(pad: Pad, colour: Colour, channel: Channel = Channel.Channel1)
    fun clearPadLight(pad: Pad) = setPadLightColour(pad, Colour.BLACK)

    /**
     * Scroll text across the launchpad display.
     * Putting "{s1}, {s2}, ... {s7} in [message] inserts a speed command.
     * At this point in the message, the speed will change to match the speed command.
     * {s1} is slowest, {s7} is fastest. The default is {s4}.
     */
    fun scrollText(message: String, colour: Colour, loop: Boolean = false)
    fun stopScrollingText()

    /**
     * Sets a listener that is invoked when a text scroll finishes.
     * A looping text scroll "finishes" once per loop.
     * Call [stopScrollingText] to stop looping.
     */
    fun setTextScrollFinishedListener(listener: () -> Unit)

    fun clock()

    fun enterBootloader()

    fun findPad(gridX: Int, gridY: Int): Pad?
    fun mustFindPad(gridX: Int, gridY: Int): Pad = findPad(gridX, gridY) ?: error("Could not find pad at ($gridX, $gridY)")

    /*
    TODO: Layout Selection, Faders, Bulk update LEDs (all variants), device enquiry, version enquiry
     */
}

package com.harry1453.launchpad

import com.harry1453.launchpad.colour.Colour
import com.harry1453.launchpad.util.Closeable

interface Launchpad : Closeable {
    // Regular light updating
    fun setPadUpdateListener(listener: (pad: Pad, pressed: Boolean, velocity: UByte) -> Unit)
    fun setPadLightColour(pad: Pad, colour: Colour, channel: Channel = Channel.Channel1)
    fun clearPadLight(pad: Pad) = setPadLightColour(pad, Colour.BLACK)
    fun flashLightBetween(pad: Pad, colour1: Colour, colour2: Colour)

    // Bulk light updating
    fun setPadLightColourBulk(padsAndColours: Iterable<Pair<Pad, Colour>>, mode: BulkUpdateMode = BulkUpdateMode.SET)

    /**
     * Bulk update rows to be all one colour
     * @param rowsAndColours A list of row indexes and the colour that row should be set to. Row indexes on the grid are 0-7, Top row is 8.
     */
    fun setRowColourBulk(rowsAndColours: Iterable<Pair<Int, Colour>>)

    /**
     * Bulk update columns to be all one colour
     * @param columnsAndColours A list of column indexes and the colour that column should be set to. Column indexes on the grid are 0-7, right row is 8.
     */
    fun setColumnColourBulk(columnsAndColours: Iterable<Pair<Int, Colour>>)

    /**
     * Set all pads to [colour]
     */
    fun setAllPads(colour: Colour)

    /**
     * Turn off all pads.
     */
    fun clearAllPads() = setAllPads(Colour.BLACK)

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

    var autoClockEnabled: Boolean
    var autoClockSpeed: Int
    fun clock()

    fun enterBootloader()

    fun findPad(gridX: Int, gridY: Int): Pad?
    fun mustFindPad(gridX: Int, gridY: Int): Pad = findPad(gridX, gridY) ?: error("Could not find pad at ($gridX, $gridY)")

    /*
    TODO: Faders, device enquiry, version enquiry
     */

    enum class BulkUpdateMode {
        SET,
        SET_RGB, // TODO this doesn't work
        FLASH, // TODO this doesn't work
        PULSE, // TODO this doesn't work
    }
}

package com.harry1453.klaunchpad.api

import com.harry1453.klaunchpad.impl.launchpads.mk2.LaunchpadMk2
import com.harry1453.klaunchpad.impl.launchpads.pro.LaunchpadPro
import kotlin.jvm.JvmStatic

/**
 * A Launchpad is a grid of [Pad]s, which are comprised of an LED and a Button.
 * The grid is indexed by the bottom left pad of the main grid area being (0,0),
 * with X and Y co-ordinates increasing to the right and to the top of the launchpad respectively.
 * The "main grid area" is defined as the central 8x8 (typically, but this interface supports smaller / larger grids) area of Pads.
 *
 * Bear in mind that launchpads may have buttons missing, meaning that the grid is not completely square.
 * For example, the Launchpad MK2 has no button in the top right of the grid (position 8,8) and the
 * Launchpad Pro has no button in each of the corners of its grid. For these positions, [getPad]
 * will return null.
 */
interface Launchpad : Closable {
    /**
     * The number of columns in the grid.
     */
    val gridColumnCount: Int

    /**
     * The index of the first column of the grid, as some launchpads have buttons to the left of the grid.
     * For example, the Launchpad MK2 would have a value of 0, as there are no buttons to the left of the grid.
     * However, the Launchpad Pro has a column of buttons to the left of the grid, so the value would be -1.
     *
     * The index of the last column can be taken as ([gridColumnCount] + [gridColumnCount] - 1).
     */
    val gridColumnStart: Int

    /**
     * The number of rows in the grid.
     */
    val gridRowCount: Int

    /**
     * The index of the first row of the grid, as some launchpads have buttons underneath the grid.
     * For example, the Launchpad MK2 would have a value of 0, as there are no buttons underneath the grid.
     * However, the Launchpad Pro has a row of buttons underneath the grid, so the value would be -1.
     *
     * The index of the last row can be taken as ([gridRowStart] + [gridRowCount] - 1).
     */
    val gridRowStart: Int

    /**
     * Set a listener that will be called whenever a Pad's button state is updated.
     *
     * [listener] takes the Pad that was udpated, whether it is currently pressed, and the velocity it was pressed with, if it was pressed. The velocity is on a scale of 0-127.
     *
     * [listener] may be invoked on any thread.
     */
    fun setPadButtonListener(listener: ((pad: Pad, pressed: Boolean, velocity: Byte) -> Unit)?)

    /**
     * Set [pad]'s LED to solid [color]. Passing [Color.BLACK] turns off the pad.
     *
     * No-op if the pad is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if the pad does not belong to this Launchpad.
     */
    fun setPadLight(pad: Pad?, color: Color)

    /**
     * Turn off [pad]'s LED.
     *
     * No-op if the pad is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if the pad does not belong to this Launchpad.
     */
    fun clearPadLight(pad: Pad?) = setPadLight(pad, Color.BLACK)

    /**
     * Flash [pad]'s LED between [color1] and [color2], toggling between the two colors every half beat.
     *
     * No-op if the pad is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if the pad does not belong to this Launchpad.
     */
    fun flashPadLight(pad: Pad?, color1: Color, color2: Color)

    /**
     * Flash [pad]'s LED between off and [color], toggling between the two states every half beat (Period of 1 beat).
     *
     * No-op if the pad is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if the pad does not belong to this Launchpad.
     */
    fun flashPadLight(pad: Pad?, color: Color) = flashPadLight(pad, Color.BLACK, color)

    /**
     * Pulse [pad]'s LED between 25% and 100% brightness, with a period of 2 beats.
     *
     * No-op if the pad is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if the pad does not belong to this Launchpad.
     */
    fun pulsePadLight(pad: Pad?, color: Color)

    /**
     * Set lots of Pad LEDs at once.
     * @param padsAndColors A list of Pads and the color that their LED should be set to.
     *
     * Skips a pad if it is null (this prevents you from having to check for if a pad obtained by [getPad] is null) or if it does not belong to this Launchpad.
     */
    fun batchSetPadLights(padsAndColors: Map<Pad?, Color>)

    /**
     * Bulk update rows of Pad LEDs to be all one color
     * @param rowsAndColors A list of row indexes and the color that row should be set to.
     */
    fun batchSetRowLights(rowsAndColors: Map<Int, Color>)

    /**
     * Bulk update columns of Pad LEDs to be all one color
     * @param columnsAndColors A list of column indexes and the color that column should be set to.
     */
    fun batchSetColumnLights(columnsAndColors: Map<Int, Color>)

    /**
     * Set all Pad LEDs to [color].
     */
    fun setAllPadLights(color: Color)

    /**
     * Turn off all Pad LEDs.
     */
    fun clearAllPadLights() = setAllPadLights(Color.BLACK)

    /**
     * Scroll text across the grid.
     * Putting {s1}, {s2}, ... {s7} in [message] inserts a scroll speed command.
     * At this point in the message, the scrolling speed will change accordingly.
     * {s1} is slowest, {s7} is fastest. The default is {s4}.
     *
     * Whilst text is scrolling, the launchpad will not listen for button presses or
     * update pad colors. Once the text has scrolled, the textScrollFinishedListener
     * will be called if set, and then, if [loop] is false, the launchpad will revert
     * to its previous state. If [loop] is true, it will scroll the message again and again,
     * until stopped by calling [stopScrollingText]
     */
    fun scrollText(message: String, color: Color, loop: Boolean = false)

    /**
     * Stop scrolling text across the grid and revert to the previous state.
     */
    fun stopScrollingText()

    /**
     * Sets a listener that is called when a text scroll finishes.
     * A looping text scroll "finishes" once per loop.
     * Call [stopScrollingText] to stop looping.
     *
     * [listener] may be invoked on any thread.
     */
    fun setTextScrollFinishedListener(listener: (() -> Unit)?) // TODO not sure if this works...

    /**
     * Get / Set Whether Auto Clocking is enabled.
     *
     * If Auto Clocking is enabled, the launchpad will be clocked automatically 24 times per beat.
     * This is the equivalent of starting a thread that calls [clock] 24 times per beat.
     * This sets the BPM of the launchpad, which is used for flashing effects and pulsing effects.
     *
     * The tempo of Auto Clocking is determined by [autoClockTempo]
     *
     * Defaults to false.
     */
    var autoClockEnabled: Boolean

    /**
     * Get / Set the tempo used for Auto Clocking.
     * Must be in range [autoClockTempoRange].
     */
    var autoClockTempo: Int

    /**
     * The range of supported values for [autoClockTempo].
     */
    val autoClockTempoRange: IntRange

    /**
     * This can be called to manually clock the launchpad, when [autoClockEnabled] is false.
     * This needs to be called 24 times per beat to keep the Launchpad in sync.
     */
    fun clock()

    /**
     * Force the launchpad to open its bootloader menu.
     */
    fun enterBootloader()

    /**
     * Get the Pad at position ([x], [y]) in the grid.
     * @return The Pad, or `null` if there is no pad in that position. Remember that it is not guaranteed that every grid position contains a pad, even if it lies within the boundaries of the grid.
     */
    fun getPad(x: Int, y: Int): Pad?

    /**
     * The maximum number of faders the Launchpad can display at once
     */
    val maxNumberOfFaders: Int

    /**
     * Switch to the Launchpad's fader view. This turns the main grid area into a bank of up to [maxNumberOfFaders].
     * If [bipolar], these faders will be bipolar, like pan faders. Else, they will be unipolar, like volume faders.
     *
     * Entering Fader View this clears all pad LEDs, and will prevent you from setting main grid area Pad LEDs
     * and listening for main grid area Pad Buttons. However, you can still use buttons outside of the main grid area (I.E. edge buttons).
     * Just remember that entering this view clears them too, so you will need to set them again.
     * LAUNCHPAD PRO ONLY: Also remember that, unless disabled in settings, the edge buttons self-illuminate when pressed in the fader view.
     *
     * One caveat is that you can not use batch update methods such as [batchSetRowLights] in this view as this is bugged, at least on the Launchpad MK2.
     *
     * Once the fader view has been setup, the listener set using [setFaderUpdateListener], will be notified of any changes to the fader values, and you
     * can update the fader values using [updateFader]. If the launchpad user updates the fader, the Launchpad handles the lighting change.
     *
     * @param faders A map of faders with the fader index (0 to maxNumberOfFaders-1) as the key and a pair of a fader color and an initial value (0-127) as the value.
     */
    fun setupFaderView(faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean = false)

    /**
     * Update a fader's value as displayed on the launchpad whilst in fader view.
     * @param faderIndex The index of the fader to update (0 to maxNumberOfFaders-1)
     * @param value The new value of the fader (-63 to 64 if in bipolar mode, 0-127 if in unipolar mode)
     */
    fun updateFader(faderIndex: Int, value: Byte)

    /**
     * Sets a listener that is called when a fader is updated by the launchpad user.
     *
     * The launchpad may, in order to emulate a smooth fade, call this several times for one user action.
     *
     * [listener] may be invoked on any thread.
     */
    fun setFaderUpdateListener(listener: ((faderIndex: Int, newFaderValue: Byte) -> Unit)?)

    /**
     * Exit the fader view and return to the normal view, with full programmability.
     */
    fun exitFaderView()

    /**
     * Close the connection to the Launchpad, turning off all lights in the process.
     */
    override fun close()

    /*
    TODO: device enquiry, version enquiry
     */

    companion object {
        /**
         * Open a Launchpad MK2 instance that uses [inputDevice] and [outputDevice].
         *
         * Essentially, start using a Launchpad MK2 that is connected on these MIDI Devices.
         * The Launchpad instance will close [inputDevice] and [outputDevice] when you close it,
         * so there is no need to keep track of [inputDevice] and [outputDevice] and close them yourself.
         */
        @JvmStatic
        fun connectToLaunchpadMK2(inputDevice: MidiInputDevice, outputDevice: MidiOutputDevice): Launchpad {
            return LaunchpadMk2(MidiDeviceWrapper(inputDevice, outputDevice), false)
        }

        /**
         * Open a Launchpad Pro instance that uses [inputDevice] and [outputDevice].
         *
         * **WARNING: THIS IS UNTESTED AS I DO NOT OWN A LAUNCHPAD PRO.**
         *
         * Essentially, start using a Launchpad Pro that is connected on these MIDI Devices.
         * The Launchpad instance will close [inputDevice] and [outputDevice] when you close it,
         * so there is no need to keep track of [inputDevice] and [outputDevice] and close them yourself.
         */
        @JvmStatic
        fun connectToLaunchpadPro(inputDevice: MidiInputDevice, outputDevice: MidiOutputDevice): Launchpad {
            return LaunchpadPro(MidiDeviceWrapper(inputDevice, outputDevice))
        }

        /**
         * Get a list of currently connected MIDI output devices
         */
        suspend fun listMidiInputDevices(): List<MidiInputDeviceInfo> = listMidiInputDevicesImpl()

        /**
         * Open the MIDI device described by [deviceInfo], which must have been returned by [listMidiOutputDevices]
         */
        suspend fun openMidiInputDevice(deviceInfo: MidiInputDeviceInfo): MidiInputDevice = openMidiInputDeviceImpl(deviceInfo)

        /**
         * Get a list of currently connected MIDI output devices
         */
        suspend fun listMidiOutputDevices(): List<MidiOutputDeviceInfo> = listMidiOutputDevicesImpl()

        /**
         * Open the MIDI device described by [deviceInfo], which must have been returned by [listMidiOutputDevices]
         */
        suspend fun openMidiOutputDevice(deviceInfo: MidiOutputDeviceInfo): MidiOutputDevice = openMidiOutputDeviceImpl(deviceInfo)
    }
}

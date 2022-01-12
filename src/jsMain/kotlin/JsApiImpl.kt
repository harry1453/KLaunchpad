import com.harry1453.klaunchpad.api.*
import jsExternal.JsMap

internal class JsLaunchpadImpl(private val launchpad: Launchpad) : JsLaunchpad {
    override var gridColumnCount: Int
        get() = launchpad.gridColumnCount
        set(_) = throw UnsupportedOperationException()

    override var gridColumnStart: Int
        get() = launchpad.gridColumnStart
        set(_) = throw UnsupportedOperationException()

    override var gridRowCount: Int
        get() = launchpad.gridRowCount
        set(_) = throw UnsupportedOperationException()

    override var gridRowStart: Int
        get() = launchpad.gridRowStart
        set(_) = throw UnsupportedOperationException()

    override fun setPadButtonListener(listener: ((pad: JsPad, pressed: Boolean, velocity: Byte) -> Unit)?) {
        if (listener == null) {
            launchpad.setPadButtonListener(null)
        } else {
            launchpad.setPadButtonListener { pad, pressed, velocity -> listener(pad.toJsPad(), pressed, velocity) }
        }
    }

    override fun setPadLight(pad: JsPad?, color: JsColor) {
        launchpad.setPadLight(pad.toPad(), color.toColor())
    }

    override fun clearPadLight(pad: JsPad?) {
        launchpad.clearPadLight(pad.toPad())
    }

    override fun flashPadLight(pad: JsPad?, color1: JsColor, color2: JsColor) {
        launchpad.flashPadLight(pad.toPad(), color1.toColor(), color2.toColor())
    }

    override fun flashPadLight(pad: JsPad?, color: JsColor) {
        launchpad.flashPadLight(pad.toPad(), color.toColor())
    }

    override fun pulsePadLight(pad: JsPad?, color: JsColor) {
        launchpad.pulsePadLight(pad.toPad(), color.toColor())
    }

    override fun batchSetPadLights(padsAndColors: JsMap<JsPad?, JsColor>) {
        launchpad.batchSetPadLights(padsAndColors.toMap()
            .mapKeys { (jsPad, _) -> jsPad.toPad() }
            .mapValues { (_, jsColor) -> jsColor.toColor() })
    }

    override fun batchSetRowLights(rowsAndColors: JsMap<Int, JsColor>) {
        launchpad.batchSetRowLights(rowsAndColors.toMap()
            .mapValues { (_, jsColor) -> jsColor.toColor() })
    }

    override fun batchSetColumnLights(columnsAndColors: JsMap<Int, JsColor>) {
        launchpad.batchSetColumnLights(columnsAndColors.toMap()
            .mapValues { (_, jsColor) -> jsColor.toColor() })
    }

    override fun setAllPadLights(color: JsColor) {
        launchpad.setAllPadLights(color.toColor())
    }

    override fun clearAllPadLights() {
        launchpad.clearAllPadLights()
    }

    override fun scrollText(message: String, color: JsColor, loop: Boolean?) {
        launchpad.scrollText(message, color.toColor(), loop ?: false)
    }

    override fun stopScrollingText() {
        launchpad.stopScrollingText()
    }

    override fun setTextScrollFinishedListener(listener: (() -> Unit)?) {
        launchpad.setTextScrollFinishedListener(listener)
    }

    override var autoClockEnabled: Boolean?
        get() = launchpad.autoClockEnabled
        set(value) { launchpad.autoClockEnabled = value ?: false }

    override var autoClockTempo: Int
        get() = launchpad.autoClockTempo
        set(value) { launchpad.autoClockTempo = value }

    override var autoClockTempoRange: IntRange
        get() = launchpad.autoClockTempoRange
        set(_) = throw UnsupportedOperationException()

    override fun clock() {
        launchpad.clock()
    }

    override fun enterBootloader() {
        launchpad.enterBootloader()
    }

    override fun getPad(x: Int, y: Int): JsPad? {
        return launchpad.getPad(x, y)?.toJsPad()
    }

    override var maxNumberOfFaders: Int
        get() = launchpad.maxNumberOfFaders
        set(_) = throw UnsupportedOperationException()

    override fun setupFaderView(faders: JsMap<Int, JsFaderSettings>, bipolar: Boolean?) {
        launchpad.setupFaderView(faders.toMap().mapValues { (_, faderSettings) ->
            require(faderSettings.initialValue in if (bipolar == true) -63..64 else 0..127) { "Fader value must be in range " + (if (bipolar == true) "-63-64" else "0-127") + " (was ${faderSettings.initialValue})" }
            faderSettings.color.toColor() to faderSettings.initialValue.toByte()
        }, bipolar ?: false)
    }

    override fun updateFader(faderIndex: Int, value: Byte) {
        launchpad.updateFader(faderIndex, value)
    }

    override fun setFaderUpdateListener(listener: ((faderIndex: Int, faderValue: Byte) -> Unit)?) {
        launchpad.setFaderUpdateListener(listener)
    }

    override fun exitFaderView() {
        launchpad.exitFaderView()
    }

    override fun close() {
        launchpad.close()
    }
}

internal class JsMidiInputDeviceImpl(val device: MidiInputDevice) : JsMidiInputDevice

internal class JsMidiOutputDeviceImpl(val device: MidiOutputDevice) : JsMidiOutputDevice

internal class JsMidiInputDeviceInfoImpl(val info: MidiInputDeviceInfo) : JsMidiInputDeviceInfo {
    override var name
        get() = info.name
        set(_) = throw UnsupportedOperationException()
    override var version
        get() = info.version
        set(_) = throw UnsupportedOperationException()
}

internal class JsMidiOutputDeviceInfoImpl(val info: MidiOutputDeviceInfo) : JsMidiOutputDeviceInfo {
    override var name
        get() = info.name
        set(_) = throw UnsupportedOperationException()
    override var version
        get() = info.version
        set(_) = throw UnsupportedOperationException()
}

private class JsPadImpl(val pad: Pad) : JsPad {
    override var gridX
        get() = pad.gridX
        set(_) = throw UnsupportedOperationException()
    override var gridY
        get() = pad.gridY
        set(_) = throw UnsupportedOperationException()
}

private fun Pad.toJsPad(): JsPad {
    return JsPadImpl(this)
}

private fun JsPad?.toPad(): Pad? {
    if (this !is JsPadImpl) return null
    return this.pad
}

private fun JsColor.toColor(): Color {
    return Color(this.r, this.g, this.b)
}

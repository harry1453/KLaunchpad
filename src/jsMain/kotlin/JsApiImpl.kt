import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad
import com.harry1453.launchpad.api.Pad

internal class JsLaunchpadDelegate(private val delegate: Launchpad) : JsLaunchpad {
    override val gridColumnCount: Int
        get() = delegate.gridColumnCount
    override val gridColumnStart: Int
        get() = delegate.gridColumnStart
    override val gridRowCount: Int
        get() = delegate.gridRowCount
    override val gridRowStart: Int
        get() = delegate.gridRowStart

    override fun setPadButtonListener(listener: (pad: JsPad, pressed: Boolean, velocity: Byte) -> Unit) {
        delegate.setPadButtonListener { pad, pressed, velocity -> listener(pad.toJsPad(), pressed, velocity) }
    }

    override fun setPadLight(pad: JsPad, color: JsColor) {
        delegate.setPadLight(pad.toPad(), color.toColor())
    }

    override fun flashPadLight(pad: JsPad, color1: JsColor, color2: JsColor) {
        delegate.flashPadLight(pad.toPad(), color1.toColor(), color2.toColor())
    }

    override fun pulsePadLight(pad: JsPad, color: JsColor) {
        delegate.pulsePadLight(pad.toPad(), color.toColor())
    }

    override fun batchSetPadLights(padsAndColors: Iterable<Pair<JsPad, JsColor>>) {
        delegate.batchSetPadLights(padsAndColors.map { value -> value.first.toPad() to value.second.toColor() })
    }

    override fun batchSetRowLights(rowsAndColors: Iterable<Pair<Int, JsColor>>) {
        delegate.batchSetRowLights(rowsAndColors.map { value -> value.first to value.second.toColor() })
    }

    override fun batchSetColumnLights(columnsAndColors: Iterable<Pair<Int, JsColor>>) {
        delegate.batchSetColumnLights(columnsAndColors.map { value -> value.first to value.second.toColor() })
    }

    override fun setAllPadLights(color: JsColor) {
        delegate.setAllPadLights(color.toColor())
    }

    override fun scrollText(message: String, color: JsColor, loop: Boolean) {
        delegate.scrollText(message, color.toColor(), loop)
    }

    override fun stopScrollingText() {
        delegate.stopScrollingText()
    }

    override fun setTextScrollFinishedListener(listener: () -> Unit) {
        delegate.setTextScrollFinishedListener(listener)
    }

    override var autoClockEnabled: Boolean
        get() = delegate.autoClockEnabled
        set(value) { delegate.autoClockEnabled = value }
    override var autoClockTempo: Int
        get() = delegate.autoClockTempo
        set(value) { delegate.autoClockTempo = value }
    override val autoClockTempoRange: IntRange
        get() = delegate.autoClockTempoRange

    override fun clock() {
        delegate.clock()
    }

    override fun enterBootloader() {
        delegate.enterBootloader()
    }

    override fun getPad(x: Int, y: Int): JsPad? {
        return delegate.getPad(x, y)?.toJsPad()
    }

    override val maxNumberOfFaders: Int
        get() = delegate.maxNumberOfFaders

    override fun setupFaderView(faders: Map<Int, Pair<JsColor, Byte>>, bipolar: Boolean) {
        delegate.setupFaderView(faders.mapValues { (_, value) -> value.first.toColor() to value.second }, bipolar)
    }

    override fun updateFader(faderIndex: Int, value: Byte) {
        delegate.updateFader(faderIndex, value)
    }

    override fun setFaderUpdateListener(listener: (faderIndex: Int, faderValue: Byte) -> Unit) {
        delegate.setFaderUpdateListener(listener)
    }

    override fun exitFaderView() {
        delegate.exitFaderView()
    }
}

private class JsPadDelegate(internal val delegate: Pad) : JsPad {
    override val gridX: Int
        get() = delegate.gridX
    override val gridY: Int
        get() = delegate.gridY
}

internal class JsColorDelegate(internal val delegate: Color) : JsColor {
    constructor(r: Int, g: Int, b: Int) : this(Color(r, g, b))
    override val r: Int
        get() = delegate.r.toInt()
    override val g: Int
        get() = delegate.g.toInt()
    override val b: Int
        get() = delegate.b.toInt()

}

private fun Pad.toJsPad(): JsPad {
    return JsPadDelegate(this)
}

private fun JsPad.toPad(): Pad {
    require(this is JsPadDelegate)
    return this.delegate
}

private fun JsColor.toColor(): Color {
    require(this is JsColorDelegate)
    return this.delegate
}

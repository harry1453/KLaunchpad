package nativeApi

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Pad
import kotlinx.cinterop.*
import nativeApi.utils.externalFunctionWithLaunchpad
import nativeApi.utils.toColor
import nativeApi.utils.toPad
import nativeApi.utils.toPointer
import nativeApi.types.Color as NativeColor

@CName("KLaunchpad_gridColumnCount")
public fun gridColumnCount(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridColumnCount.toUInt()
    }

@CName("KLaunchpad_gridColumnStart")
public fun gridColumnStart(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridColumnStart.toUInt()
    }

@CName("KLaunchpad_gridRowCount")
public fun gridRowCount(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridRowCount.toUInt()
    }

@CName("KLaunchpad_gridRowStart")
public fun gridRowStart(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridRowStart.toUInt()
    }

@CName("KLaunchpad_setPadButtonListener")
public fun setPadButtonListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(pad: COpaquePointer, pressed: Boolean, velocity: Byte) -> Unit>>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setPadButtonListener(null)
        } else {
            launchpad.setPadButtonListener { pad, pressed, velocity -> listener(pad.toPointer(), pressed, velocity) }
        }
    }

@CName("KLaunchpad_setPadLight")
public fun setPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.setPadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_clearPadLight")
public fun clearPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clearPadLight(padPtr.toPad())
    }

@CName("KLaunchpad_flashPadLight")
public fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color1: NativeColor, color2: NativeColor): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.flashPadLight(padPtr.toPad(), color1.toColor(), color2.toColor())
    }

@CName("KLaunchpad_flashPadLightOnAndOff")
public fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.flashPadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_pulsePadLight")
public fun pulsePadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.pulsePadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_batchSetPadLights")
public fun batchSetPadLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_batchSetRowLights")
public fun batchSetRowLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_batchSetColumnLights")
public fun batchSetColumnLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_setAllPadLights")
public fun setAllPadLights(launchpadPtr: COpaquePointer, color: NativeColor): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.setAllPadLights(color.toColor())
    }

@CName("KLaunchpad_clearAllPadLights")
public fun clearAllPadLights(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clearAllPadLights()
    }

@CName("KLaunchpad_scrollText")
public fun scrollText(launchpadPtr: COpaquePointer, text: CPointer<ByteVar>, color: NativeColor, loop: Boolean): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.scrollText(text.toKString(), color.toColor(), loop)
    }

@CName("KLaunchpad_stopScrollingText")
public fun stopScrollingText(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.stopScrollingText()
    }

@CName("KLaunchpad_setTextScrollFinishedListener")
public fun setTextScrollFinishedListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(() -> Unit)>>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setTextScrollFinishedListener(null)
        } else {
            launchpad.setTextScrollFinishedListener { listener() }
        }
    }

@CName("KLaunchpad_isAutoClockEnabled")
public fun isAutoClockEnabled(launchpadPtr: COpaquePointer): Boolean =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.autoClockEnabled
    }

@CName("KLaunchpad_setAutoClockEnabled")
public fun setAutoClockEnabled(launchpadPtr: COpaquePointer, autoClockEnabled: Boolean): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.autoClockEnabled = autoClockEnabled
    }

@CName("KLaunchpad_getAutoClockTempo")
public fun getAutoClockTempo(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.autoClockTempo.toUInt()
    }

@CName("KLaunchpad_setAutoClockTempo")
public fun setAutoClockTempo(launchpadPtr: COpaquePointer, autoClockTempo: UInt): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.autoClockTempo = autoClockTempo.toInt()
    }

// TODO auto clock tempo range

@CName("KLaunchpad_clock")
public fun clock(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clock()
    }

@CName("KLaunchpad_enterBootloader")
public fun enterBootloader(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.enterBootloader()
    }

@CName("KLaunchpad_getPad")
public fun getPad(launchpadPtr: COpaquePointer, x: Int, y: Int): COpaquePointer? =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return StableRef.create(launchpad.getPad(x, y) ?: return null).asCPointer()
    }

@CName("KLaunchpad_getMaximumNumberOfFaders")
public fun getMaximumNumberOfFaders(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.maxNumberOfFaders.toUInt()
    }

@CName("KLaunchpad_setupFaderView")
public fun setupFaderView(launchpadPtr: COpaquePointer, faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean = false): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_updateFader")
public fun updateFader(launchpadPtr: COpaquePointer, faderIndex: UInt, value: Byte): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.updateFader(faderIndex.toInt(), value)
    }

@CName("KLaunchpad_setFaderUpdateListener")
public fun setFaderUpdateListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<((faderIndex: Int, newFaderValue: Byte) -> Unit)>>): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setFaderUpdateListener(null)
        } else {
            launchpad.setFaderUpdateListener { faderIndex, newFaderValue -> listener(faderIndex, newFaderValue) }
        }
    }

@CName("KLaunchpad_exitFaderView")
public fun exitFaderView(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.exitFaderView()
    }

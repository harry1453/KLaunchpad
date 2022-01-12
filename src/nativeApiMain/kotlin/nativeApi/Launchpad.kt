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
fun gridColumnCount(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridColumnCount.toUInt()
    }

@CName("KLaunchpad_gridColumnStart")
fun gridColumnStart(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridColumnStart.toUInt()
    }

@CName("KLaunchpad_gridRowCount")
fun gridRowCount(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridRowCount.toUInt()
    }

@CName("KLaunchpad_gridRowStart")
fun gridRowStart(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.gridRowStart.toUInt()
    }

@CName("KLaunchpad_setPadButtonListener")
fun setPadButtonListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(pad: COpaquePointer, pressed: Boolean, velocity: Byte) -> Unit>>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setPadButtonListener(null)
        } else {
            launchpad.setPadButtonListener { pad, pressed, velocity -> listener(pad.toPointer(), pressed, velocity) }
        }
    }

@CName("KLaunchpad_setPadLight")
fun setPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.setPadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_clearPadLight")
fun clearPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clearPadLight(padPtr.toPad())
    }

@CName("KLaunchpad_flashPadLight")
fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color1: NativeColor, color2: NativeColor) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.flashPadLight(padPtr.toPad(), color1.toColor(), color2.toColor())
    }

@CName("KLaunchpad_flashPadLightOnAndOff")
fun flashPadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.flashPadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_pulsePadLight")
fun pulsePadLight(launchpadPtr: COpaquePointer, padPtr: COpaquePointer, color: NativeColor) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.pulsePadLight(padPtr.toPad(), color.toColor())
    }

@CName("KLaunchpad_batchSetPadLights")
fun batchSetPadLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_batchSetRowLights")
fun batchSetRowLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_batchSetColumnLights")
fun batchSetColumnLights(launchpadPtr: COpaquePointer, padsAndColors: Map<Pad?, NativeColor>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_setAllPadLights")
fun setAllPadLights(launchpadPtr: COpaquePointer, color: NativeColor) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.setAllPadLights(color.toColor())
    }

@CName("KLaunchpad_clearAllPadLights")
fun clearAllPadLights(launchpadPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clearAllPadLights()
    }

@CName("KLaunchpad_scrollText")
fun scrollText(launchpadPtr: COpaquePointer, text: CPointer<ByteVar>, color: NativeColor, loop: Boolean) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.scrollText(text.toKString(), color.toColor(), loop)
    }

@CName("KLaunchpad_stopScrollingText")
fun stopScrollingText(launchpadPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.stopScrollingText()
    }

@CName("KLaunchpad_setTextScrollFinishedListener")
fun setTextScrollFinishedListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<(() -> Unit)>>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setTextScrollFinishedListener(null)
        } else {
            launchpad.setTextScrollFinishedListener { listener() }
        }
    }

@CName("KLaunchpad_isAutoClockEnabled")
fun isAutoClockEnabled(launchpadPtr: COpaquePointer): Boolean =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.autoClockEnabled
    }

@CName("KLaunchpad_setAutoClockEnabled")
fun setAutoClockEnabled(launchpadPtr: COpaquePointer, autoClockEnabled: Boolean) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.autoClockEnabled = autoClockEnabled
    }

@CName("KLaunchpad_getAutoClockTempo")
fun getAutoClockTempo(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.autoClockTempo.toUInt()
    }

@CName("KLaunchpad_setAutoClockTempo")
fun setAutoClockTempo(launchpadPtr: COpaquePointer, autoClockTempo: UInt) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.autoClockTempo = autoClockTempo.toInt()
    }

// TODO auto clock tempo range

@CName("KLaunchpad_clock")
fun clock(launchpadPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.clock()
    }

@CName("KLaunchpad_enterBootloader")
fun enterBootloader(launchpadPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.enterBootloader()
    }

@CName("KLaunchpad_getPad")
fun getPad(launchpadPtr: COpaquePointer, x: Int, y: Int): COpaquePointer? =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return StableRef.create(launchpad.getPad(x, y) ?: return null).asCPointer()
    }

@CName("KLaunchpad_getMaximumNumberOfFaders")
fun getMaximumNumberOfFaders(launchpadPtr: COpaquePointer): UInt =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.maxNumberOfFaders.toUInt()
    }

@CName("KLaunchpad_setupFaderView")
fun setupFaderView(launchpadPtr: COpaquePointer, faders: Map<Int, Pair<Color, Byte>>, bipolar: Boolean = false) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        // TODO
    }

@CName("KLaunchpad_updateFader")
fun updateFader(launchpadPtr: COpaquePointer, faderIndex: UInt, value: Byte) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.updateFader(faderIndex.toInt(), value)
    }

@CName("KLaunchpad_setFaderUpdateListener")
fun setFaderUpdateListener(launchpadPtr: COpaquePointer, listener: CPointer<CFunction<((faderIndex: Int, newFaderValue: Byte) -> Unit)>>) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        if (listener.rawValue.toLong() == 0L) {
            launchpad.setFaderUpdateListener(null)
        } else {
            launchpad.setFaderUpdateListener { faderIndex, newFaderValue -> listener(faderIndex, newFaderValue) }
        }
    }

@CName("KLaunchpad_exitFaderView")
fun exitFaderView(launchpadPtr: COpaquePointer) =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.exitFaderView()
    }

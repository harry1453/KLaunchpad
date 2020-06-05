package nativeApi.utils

import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.Pad
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import nativeApi.types.Color as NativeColor;

/**
 * Helper function for externally facing non-member functions
 */
internal inline fun <T> externalFunction(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    initRuntimeIfNeeded()
    return block()
}

/**
 * Helper function for externally facing launchpad member functions
 */
internal inline fun <T> externalFunctionWithLaunchpad(launchpadPtr: COpaquePointer, block: (Launchpad) -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return externalFunction {
        block(launchpadPtr.toObject())
    }
}

/**
 * Convert an opaque pointer into [T]
 */
internal inline fun <reified T: Any> COpaquePointer.toObject(): T {
    if (this.rawValue.toLong() == 0L) throw NullPointerException()
    return this.asStableRef<T>().get()
}

/**
 * Dispose (free) a [COpaquePointer]
 */
internal inline fun <reified T: Any> COpaquePointer.dispose() {
    if (this.rawValue.toLong() == 0L) throw NullPointerException()
    this.asStableRef<T>().dispose()
}

/**
 * Helper function for externally facing pad member functions
 */
internal inline fun <T> externalFunctionWithPad(padPtr: COpaquePointer, block: (Pad) -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    initRuntimeIfNeeded()
    if (padPtr.rawValue.toLong() == 0L) throw NullPointerException()
    val pad = padPtr.asStableRef<Pad>().get()
    return block(pad)
}

internal fun Pad.toPointer(): COpaquePointer {
    return StableRef.create(this).asCPointer() // TODO when is this freed?
}

internal fun COpaquePointer.toPad(): Pad? {
    if (this.rawValue.toLong() == 0L) return null
    return this.asStableRef<Pad>().get()
}

internal fun NativeColor.toColor(): Color {
    return Color(this.r, this.g, this.b)
}

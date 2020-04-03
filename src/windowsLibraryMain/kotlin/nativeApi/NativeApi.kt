package nativeApi

import com.harry1453.klaunchpad.api.Launchpad
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.coroutines.runBlocking
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private inline fun <T> externalFunction(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    initRuntimeIfNeeded()
    return block()
}

private fun <T> externalFunctionWithLaunchpad(launchpadPtr: COpaquePointer, block: (Launchpad) -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    kotlin.native.initRuntimeIfNeeded()
    val launchpad = launchpadPtr.asStableRef<Launchpad>().get()
    return block(launchpad)
}

fun connectToLaunchpadMK2(): COpaquePointer = externalFunction {
    val launchpad = runBlocking { Launchpad.connectToLaunchpadMK2Async().await() }
    return StableRef.create(launchpad).asCPointer()
}

fun closeLaunchpad(launchpadPtr: COpaquePointer) = externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
    launchpad.close()
    launchpadPtr.asStableRef<Launchpad>().dispose()
}

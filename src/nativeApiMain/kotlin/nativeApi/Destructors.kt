package nativeApi

import com.harry1453.klaunchpad.api.Launchpad
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.asStableRef
import nativeApi.utils.externalFunctionWithLaunchpad

// Destructors

@CName("KLaunchpad_isClosed")
public fun isClosed(launchpadPtr: COpaquePointer): Boolean =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        return launchpad.isClosed
    }

@CName("KLaunchpad_closeLaunchpad")
public fun closeLaunchpad(launchpadPtr: COpaquePointer): Unit =
    externalFunctionWithLaunchpad(launchpadPtr) { launchpad ->
        launchpad.close()
        launchpadPtr.asStableRef<Launchpad>().dispose()
    }

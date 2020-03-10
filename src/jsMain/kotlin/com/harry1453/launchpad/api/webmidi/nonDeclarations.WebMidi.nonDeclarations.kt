@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION")
package com.harry1453.launchpad.api.webmidi

external interface MIDIInputMap {
    val values: () -> IterableIterator<MIDIInput>
}

external interface MIDIOutputMap {
    val values: () -> IterableIterator<MIDIOutput>
}

@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION")

package com.harry1453.launchpad.api.webmidi

import org.w3c.dom.Navigator
import kotlin.js.Promise

/* extending interface from lib.dom.d.ts */
inline fun Navigator.requestMIDIAccess(): Promise<MIDIAccess> = this.asDynamic().requestMIDIAccess() as Promise<MIDIAccess>

inline fun Navigator.requestMIDIAccess(options: MIDIOptions): Promise<MIDIAccess> = this.asDynamic().requestMIDIAccess(options) as Promise<MIDIAccess>

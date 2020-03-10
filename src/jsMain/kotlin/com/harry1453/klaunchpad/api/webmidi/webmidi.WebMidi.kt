@file:JsQualifier("WebMidi")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION")

package com.harry1453.klaunchpad.api.webmidi

import org.khronos.webgl.Uint8Array
import org.w3c.dom.AddEventListenerOptions
import org.w3c.dom.EventInit
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.EventTarget
import kotlin.js.Promise

external interface MIDIOptions {
    var sysex: Boolean
}

external interface MIDIAccess : EventTarget {
    var inputs: MIDIInputMap
    var outputs: MIDIOutputMap
    fun onstatechange(e: MIDIConnectionEvent)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIAccess /* this */, e: MIDIConnectionEvent) -> Any, options: Boolean = definedExternally)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIAccess /* this */, e: MIDIConnectionEvent) -> Any, options: AddEventListenerOptions = definedExternally)
    fun addEventListener(type: String, listener: EventListener, options: Boolean = definedExternally)
    fun addEventListener(type: String, listener: EventListener, options: AddEventListenerOptions = definedExternally)
    var sysexEnabled: Boolean
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIAccess /* this */, e: MIDIConnectionEvent) -> Any)
    fun addEventListener(type: String, listener: EventListener)
}

external interface MIDIPort : EventTarget {
    var id: String
    var manufacturer: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var type: String /* 'input' | 'output' */
    var version: String?
        get() = definedExternally
        set(value) = definedExternally
    var state: String /* 'disconnected' | 'connected' */
    var connection: String /* 'open' | 'closed' | 'pending' */
    fun onstatechange(e: MIDIConnectionEvent)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIPort /* this */, e: MIDIConnectionEvent) -> Any, options: Boolean = definedExternally)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIPort /* this */, e: MIDIConnectionEvent) -> Any, options: AddEventListenerOptions = definedExternally)
    fun addEventListener(type: String, listener: EventListener, options: Boolean = definedExternally)
    fun addEventListener(type: String, listener: EventListener, options: AddEventListenerOptions = definedExternally)
    fun open(): Promise<MIDIPort>
    fun close(): Promise<MIDIPort>
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIPort /* this */, e: MIDIConnectionEvent) -> Any)
    fun addEventListener(type: String, listener: EventListener)
}

external interface MIDIInput : MIDIPort {
    override var type: String /* 'input' */
    var onmidimessage: (MIDIMessageEvent) -> Unit
    fun addEventListener(type: String /* 'midimessage' */, listener: (self: MIDIInput /* this */, e: MIDIMessageEvent) -> Any, options: Boolean = definedExternally)
    fun addEventListener(type: String /* 'midimessage' */, listener: (self: MIDIInput /* this */, e: MIDIMessageEvent) -> Any, options: AddEventListenerOptions = definedExternally)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIInput /* this */, e: MIDIConnectionEvent) -> Any, options: Boolean = definedExternally)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIInput /* this */, e: MIDIConnectionEvent) -> Any, options: AddEventListenerOptions = definedExternally)
    override fun addEventListener(type: String, listener: EventListener, options: Boolean)
    override fun addEventListener(type: String, listener: EventListener, options: AddEventListenerOptions)
    fun addEventListener(type: String /* 'midimessage' */, listener: (self: MIDIInput /* this */, e: MIDIMessageEvent) -> Any)
    fun addEventListener(type: String /* 'statechange' */, listener: (self: MIDIInput /* this */, e: MIDIConnectionEvent) -> Any)
    override fun addEventListener(type: String, listener: EventListener)

    override fun open(): Promise<MIDIInput>
}

external interface MIDIOutput : MIDIPort {
    override var type: String /* 'output' */
    fun send(data: Array<Number>, timestamp: Number = definedExternally)
    fun send(data: Uint8Array, timestamp: Number = definedExternally)
    fun clear()

    override fun open(): Promise<MIDIOutput>
}

external interface MIDIMessageEvent : Event {
    var receivedTime: Number
    var data: Uint8Array
}

external interface MIDIMessageEventInit : EventInit {
    var receivedTime: Number
    var data: Uint8Array
}

external interface MIDIConnectionEvent : Event {
    var port: MIDIPort
}

external interface MIDIConnectionEventInit : EventInit {
    var port: MIDIPort
}

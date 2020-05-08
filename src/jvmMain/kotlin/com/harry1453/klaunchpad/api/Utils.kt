package com.harry1453.klaunchpad.api

import javax.sound.midi.MidiMessage

internal class ArbitraryMidiMessage(data: ByteArray) : MidiMessage(data) {
    override fun clone(): Any {
        return ArbitraryMidiMessage(data)
    }
}


package com.harry1453.klaunchpad.impl

import com.harry1453.klaunchpad.api.Launchpad
import com.harry1453.klaunchpad.api.MidiDevice
import kotlinx.coroutines.*

abstract class AbstractLaunchpad(protected val midiDevice: MidiDevice) : Launchpad {
    init {
        midiDevice.setMessageListener { onMidiMessage(it) }
    }

    abstract fun onMidiMessage(message: ByteArray)

    private var autoClockJob: Job? = null

    override var autoClockEnabled: Boolean = false
        set(newSetting) {
            if (newSetting) {
                startAutoClock()
            } else {
                autoClockJob?.cancel()
            }
            field = newSetting
        }

    private fun startAutoClock() {
        autoClockJob = GlobalScope.launch {
            while(isActive) {
                delay(60000.toLong() / 24 / autoClockTempo)
                midiDevice.clock()
            }
        }
    }

    override var autoClockTempo: Int = 120 // Launchpad default
        set(newBpm) {
            require(newBpm in 40..240) { "Launchpad MK2 only supports BPM between 40 and 240"}
            field = newBpm
        }

    override fun clock() {
        if (!autoClockEnabled) {
            midiDevice.clock()
        }
    }

    override val isClosed: Boolean
        get() = !(autoClockJob?.isActive ?: false) && midiDevice.isClosed

    override fun close() {
        stopScrollingText()
        exitFaderView()
        clearAllPadLights()
        autoClockJob?.cancel()
        midiDevice.close()
    }
}

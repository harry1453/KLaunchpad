import com.harry1453.launchpad.impl.launchpads.mk2.LaunchpadMk2Pad
import com.harry1453.launchpad.impl.launchpads.pro.LaunchpadProPad
import kotlin.test.Test
import kotlin.test.assertEquals

class PadTests {
    @Test
    fun testLaunchpadMk2Pads() {
        assertEquals(11, LaunchpadMk2Pad.A1.sessionMidiCode)
        assertEquals(36, LaunchpadMk2Pad.A1.userMidiCode)
        assertEquals(false, LaunchpadMk2Pad.A1.isControlChange)

        assertEquals(88, LaunchpadMk2Pad.H8.sessionMidiCode)
        assertEquals(99, LaunchpadMk2Pad.H8.userMidiCode)
        assertEquals(false, LaunchpadMk2Pad.H8.isControlChange)

        assertEquals(111, LaunchpadMk2Pad.T8.sessionMidiCode)
        assertEquals(111, LaunchpadMk2Pad.T8.userMidiCode)
        assertEquals(true, LaunchpadMk2Pad.T8.isControlChange)

        assertEquals(19, LaunchpadMk2Pad.R8.sessionMidiCode)
        assertEquals(107, LaunchpadMk2Pad.R8.userMidiCode)
        assertEquals(false, LaunchpadMk2Pad.R8.isControlChange)
    }

    @Test
    fun testLaunchpadProPads() {
        assertEquals(11, LaunchpadProPad.A1.midiCode)
        assertEquals(false, LaunchpadProPad.A1.isEdgePad)

        assertEquals(88, LaunchpadProPad.H8.midiCode)
        assertEquals(false, LaunchpadProPad.H8.isEdgePad)

        assertEquals(98, LaunchpadProPad.T8.midiCode)
        assertEquals(true, LaunchpadProPad.T8.isEdgePad)

        assertEquals(19, LaunchpadProPad.R8.midiCode)
        assertEquals(true, LaunchpadProPad.R8.isEdgePad)

        assertEquals(1, LaunchpadProPad.U1.midiCode)
        assertEquals(true, LaunchpadProPad.U1.isEdgePad)

        assertEquals(80, LaunchpadProPad.L1.midiCode)
        assertEquals(true, LaunchpadProPad.L1.isEdgePad)
    }
}

package com.harry1453.launchpad.impl.pro

import com.harry1453.launchpad.api.Pad

/**
 * Launchpad Pro grid is like this:
 *
 *    T1 T2 T3 T4 T5 T6 T7 T8
 * L1 H1 H2 H3 H4 H5 H6 H7 H8 R1
 * L2 G1 G2 G3 G4 G5 G6 G7 G8 R2
 * L3 F1 F2 F3 F4 F5 F6 F7 F8 R3
 * L4 E1 E2 E3 E4 E5 E6 E7 E8 R4
 * L5 D1 D2 D3 D4 D5 D6 D7 D8 R5
 * L6 C1 C2 C3 C4 C5 C6 C7 C8 R6
 * L7 B1 B2 B3 B4 B5 B6 B7 B8 D7
 * L8 A1 A2 A3 A4 A5 A6 A7 A8 D8
 *    U1 U2 U3 U4 U5 U6 U7 U8
 *
 * where A1 is (0, 0) and H8 is (7, 7)
 */
internal enum class LaunchpadProPad : Pad {
        T1, T2, T3, T4, T5, T6, T7, T8,
    L1, H1, H2, H3, H4, H5, H6, H7, H8, R1,
    L2, G1, G2, G3, G4, G5, G6, G7, G8, R2,
    L3, F1, F2, F3, F4, F5, F6, F7, F8, R3,
    L4, E1, E2, E3, E4, E5, E6, E7, E8, R4,
    L5, D1, D2, D3, D4, D5, D6, D7, D8, R5,
    L6, C1, C2, C3, C4, C5, C6, C7, C8, R6,
    L7, B1, B2, B3, B4, B5, B6, B7, B8, R7,
    L8, A1, A2, A3, A4, A5, A6, A7, A8, R8,
        U1, U2, U3, U4, U5, U6, U7, U8
    ;

    override val gridX: Int
    override val gridY: Int

    // MIDI code in either fader or programmer mode
    internal val midiCode: Int

    // Basically whether it is an edge button. Edge buttons are always CCs in fader and programmer mode.
    internal val isEdgePad: Boolean

    init {
        // Name parser determines MIDI codes
        val letter = name[0]
        // Range 0-7
        val number = name[1].toString().toByte() - 1
        require(number in 0..7) { "Invalid X Grid position: ${number + 1}" }

        if (letter == 'T' || letter == 'U' || letter == 'L' || letter == 'R') { // Edge buttons
            // Range 0-9
            val x = when (letter) {
                'T', 'U' -> number + 1
                'L' -> 0
                'R' -> 9
                else -> error("Invalid X Grid position: $letter")
            }
            // Range 0-9
            val y = when (letter) {
                'L', 'R' -> 8 - number
                'T' -> 9
                'U' -> 0
                else -> error("Invalid Y Grid position: $letter")
            }
            isEdgePad = true
            midiCode = (y * 10) + x
            gridX = x - 1
            gridY = y - 1
        } else {
            isEdgePad = false
            gridY = number
            gridX = when(letter) {
                'A' -> 0
                'B' -> 1
                'C' -> 2
                'D' -> 3
                'E' -> 4
                'F' -> 5
                'G' -> 6
                'H' -> 7
                else -> error("Invalid X Grid position: $letter")
            }
            midiCode = ((gridY + 1) * 10) + gridX + 1
        }
    }

    override fun toString(): String {
        return name
    }

    companion object {
        internal val EDGE_PADS: Map<Int, Pad> = values()
            .filter { it.isEdgePad }
            .map { it.midiCode to it }
            .toMap()

        internal val NON_EDGE_PADS: Map<Int, Pad> = values()
            .filter { !it.isEdgePad }
            .map { it.midiCode to it }
            .toMap()

        private val GRID_PADS: Map<Pair<Int, Int>, Pad> = values()
            .map { Pair(it.gridX, it.gridY) to it }
            .toMap()

        fun findPad(gridX: Int, gridY: Int): Pad? {
            return GRID_PADS[Pair(gridX, gridY)]
        }
    }
}

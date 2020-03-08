package com.harry1453.launchpad.impl

import com.harry1453.launchpad.Pad

/**
 * Launchpad MK2 is like this:
 *
 * T1 T2 T3 T4 T5 T6 T7 T8
 * H1 H2 H3 H4 H5 H6 H7 H8 R1
 * G1 G2 G3 G4 G5 G6 G7 G8 R2
 * F1 F2 F3 F4 F5 F6 F7 F8 R3
 * E1 E2 E3 E4 E5 E6 E7 E8 R4
 * D1 D2 D3 D4 D5 D6 D7 D8 R5
 * C1 C2 C3 C4 C5 C6 C7 C8 R6
 * B1 B2 B3 B4 B5 B6 B7 B8 D7
 * A1 A2 A3 A4 A5 A6 A7 A8 D8
 */
internal enum class LaunchpadMk2Pad : Pad {
    T1, T2, T3, T4, T5, T6, T7, T8,
    H1, H2, H3, H4, H5, H6, H7, H8, R1,
    G1, G2, G3, G4, G5, G6, G7, G8, R2,
    F1, F2, F3, F4, F5, F6, F7, F8, R3,
    E1, E2, E3, E4, E5, E6, E7, E8, R4,
    D1, D2, D3, D4, D5, D6, D7, D8, R5,
    C1, C2, C3, C4, C5, C6, C7, C8, R6,
    B1, B2, B3, B4, B5, B6, B7, B8, R7,
    A1, A2, A3, A4, A5, A6, A7, A8, R8,
    ;

    override val gridX: Int
    override val gridY: Int
    val sessionMidiCode: Int
    val userMidiCode: Int
    val isControlChange: Boolean

    init {
        // Name parser determines code
        val letter = name[0]
        val number = name[1].toString().toByte() - 1
        require(number in 0..7) { "Invalid X Grid position: ${number + 1}" }

        isControlChange = letter == 'T'
        if (isControlChange) {
            sessionMidiCode = 104 + number
            userMidiCode = 104 + number
            gridY = 8
            gridX = number
        } else {
            // Range 0 - 7
            gridY = when (letter) {
                'A' -> 0
                'B' -> 1
                'C' -> 2
                'D' -> 3
                'E' -> 4
                'F' -> 5
                'G' -> 6
                'H' -> 7
                'R' -> 7 - number
                else -> error("Invalid Y Grid position: $letter")
            }

            // Range 0 - 8
            gridX = if (letter == 'R') {
                8
            } else {
                number
            }
            sessionMidiCode = gridY * 10 + gridX + 11
            userMidiCode = if (gridX == 8) {
                107 - gridY
            } else {
                (if (gridX >= 4) 68 else 36) + gridY * 4 + gridX % 4
            }
        }
    }

    override fun toString(): String {
        return name
    }

    companion object {
        internal val CONTROL_CHANGE_PADS: Map<Int, Pad> = values()
            .filter { it.isControlChange }
            .map { it.sessionMidiCode to it }
            .toMap()

        internal val SESSION_MODE_PADS: Map<Int, Pad> = values()
            .filter { !it.isControlChange }
            .map { it.sessionMidiCode to it }
            .toMap()

        internal val USER_MODE_PADS: Map<Int, Pad> = values()
            .filter { !it.isControlChange }
            .map { it.userMidiCode to it }
            .toMap()

        private val GRID_PADS: Map<Pair<Int, Int>, Pad> = values()
            .map { Pair(it.gridX, it.gridY) to it }
            .toMap()

        fun findPad(gridX: Int, gridY: Int): Pad? {
            return GRID_PADS[Pair(gridX, gridY)]
        }
    }
}

package com.harry1453.launchpad.impl

import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.impl.util.colorDelta

private fun hexStringToColor(hexString: String): Color {
    val r = hexString.substring(0..1).toUByte(radix = 16)
    val g = hexString.substring(2..3).toUByte(radix = 16)
    val b = hexString.substring(4..5).toUByte(radix = 16)
    return Color(r, g, b)
}

private val VELOCITY_TO_COLOR: Array<Color> = Array(128) { Color.BLACK }.apply {
    this[0] = hexStringToColor("000000")
    this[1] = hexStringToColor("1c1c1c")
    this[2] = hexStringToColor("7c7c7c")
    this[3] = hexStringToColor("fcfcfc")
    this[4] = hexStringToColor("ff4d47")
    this[5] = hexStringToColor("ff0a00")
    this[6] = hexStringToColor("5a0100")
    this[7] = hexStringToColor("190000")

    this[8] = hexStringToColor("ffbd62")
    this[9] = hexStringToColor("ff5600")
    this[10] = hexStringToColor("5a1d00")
    this[11] = hexStringToColor("241800")
    this[12] = hexStringToColor("fdfd21")
    this[13] = hexStringToColor("fdfd00")
    this[14] = hexStringToColor("585800")
    this[15] = hexStringToColor("181800")

    this[16] = hexStringToColor("80fd2a")
    this[17] = hexStringToColor("40fd00")
    this[18] = hexStringToColor("165800")
    this[19] = hexStringToColor("132800")
    this[20] = hexStringToColor("34fd2b")
    this[21] = hexStringToColor("00fd00")
    this[22] = hexStringToColor("005800")
    this[23] = hexStringToColor("001800")

    this[24] = hexStringToColor("33fd46")
    this[25] = hexStringToColor("00fd00")
    this[26] = hexStringToColor("005800")
    this[27] = hexStringToColor("001800")
    this[28] = hexStringToColor("32fd7e")
    this[29] = hexStringToColor("00fd3a")
    this[30] = hexStringToColor("005814")
    this[31] = hexStringToColor("001c0f")

    this[32] = hexStringToColor("2ffcb0")
    this[33] = hexStringToColor("00fc91")
    this[34] = hexStringToColor("005831")
    this[35] = hexStringToColor("00180f")
    this[36] = hexStringToColor("39bfff")
    this[37] = hexStringToColor("00a7ff")
    this[38] = hexStringToColor("004051")
    this[39] = hexStringToColor("001018")

    this[40] = hexStringToColor("4186ff")
    this[41] = hexStringToColor("0050ff")
    this[42] = hexStringToColor("001a5a")
    this[43] = hexStringToColor("000719")
    this[44] = hexStringToColor("4647ff")
    this[45] = hexStringToColor("0000ff")
    this[46] = hexStringToColor("00005b")
    this[47] = hexStringToColor("000019")

    this[48] = hexStringToColor("8347ff")
    this[49] = hexStringToColor("5000ff")
    this[50] = hexStringToColor("160067")
    this[51] = hexStringToColor("0b0032")
    this[52] = hexStringToColor("ff49ff")
    this[53] = hexStringToColor("ff00ff")
    this[54] = hexStringToColor("5a005a")
    this[55] = hexStringToColor("190019")

    this[56] = hexStringToColor("ff4d84")
    this[57] = hexStringToColor("ff0752")
    this[58] = hexStringToColor("5a011b")
    this[59] = hexStringToColor("210010")
    this[60] = hexStringToColor("ff1900")
    this[61] = hexStringToColor("9b3500")
    this[62] = hexStringToColor("7a5100")
    this[63] = hexStringToColor("3e6400")

    this[64] = hexStringToColor("003800")
    this[65] = hexStringToColor("005432")
    this[66] = hexStringToColor("00537e")
    this[67] = hexStringToColor("0000ff")
    this[68] = hexStringToColor("00444d")
    this[69] = hexStringToColor("1b00d2")
    this[70] = hexStringToColor("7c7c7c")
    this[71] = hexStringToColor("202020")

    this[72] = hexStringToColor("ff0a00")
    this[73] = hexStringToColor("bafd00")
    this[74] = hexStringToColor("aaed00")
    this[75] = hexStringToColor("56fd00")
    this[76] = hexStringToColor("008800")
    this[77] = hexStringToColor("00fc7a")
    this[78] = hexStringToColor("00a7ff")
    this[79] = hexStringToColor("001bff")

    this[80] = hexStringToColor("3500ff")
    this[81] = hexStringToColor("7700ff")
    this[82] = hexStringToColor("b4177e")
    this[83] = hexStringToColor("412000")
    this[84] = hexStringToColor("ff4a00")
    this[85] = hexStringToColor("83e100")
    this[86] = hexStringToColor("65fd00")
    this[87] = hexStringToColor("00fd00")

    this[88] = hexStringToColor("00fd00")
    this[89] = hexStringToColor("45fd61")
    this[90] = hexStringToColor("00fcca")
    this[91] = hexStringToColor("5086ff")
    this[92] = hexStringToColor("274dc9")
    this[93] = hexStringToColor("827aed")
    this[94] = hexStringToColor("d30cff")
    this[95] = hexStringToColor("ff065a")

    this[96] = hexStringToColor("ff7d00")
    this[97] = hexStringToColor("b9b100")
    this[98] = hexStringToColor("8afd00")
    this[99] = hexStringToColor("825d00")
    this[100] = hexStringToColor("392800")
    this[101] = hexStringToColor("0d4c05")
    this[102] = hexStringToColor("005037")
    this[103] = hexStringToColor("131329")

    this[104] = hexStringToColor("101f5a")
    this[105] = hexStringToColor("6a3c17")
    this[106] = hexStringToColor("ac0400")
    this[107] = hexStringToColor("e15135")
    this[108] = hexStringToColor("dc6900")
    this[109] = hexStringToColor("ffe100")
    this[110] = hexStringToColor("99e100")
    this[111] = hexStringToColor("5fb500")

    this[112] = hexStringToColor("1b1b31")
    this[113] = hexStringToColor("dcfd54")
    this[114] = hexStringToColor("76fcb8")
    this[115] = hexStringToColor("9697ff")
    this[116] = hexStringToColor("8b61ff")
    this[117] = hexStringToColor("404040")
    this[118] = hexStringToColor("747474")
    this[119] = hexStringToColor("defcfc")

    this[120] = hexStringToColor("a40400")
    this[121] = hexStringToColor("350000")
    this[122] = hexStringToColor("00d100")
    this[123] = hexStringToColor("004000")
    this[124] = hexStringToColor("b9b100")
    this[125] = hexStringToColor("3d3000")
    this[126] = hexStringToColor("b45d00")
    this[127] = hexStringToColor("4a1400")
}

/**
 * Converts an RGB color to the closest color in the RGB Launchpads' 128 color palette.
 * @return a MIDI velocity value corresponding to the closest color in the Launchpad's color palette.
 */
internal fun Color.toVelocity(): Int {
    var lowestDifference = Long.MAX_VALUE
    var lowestDifferenceIndex: Int = -1

    VELOCITY_TO_COLOR.forEachIndexed { index, newColor ->
        val newDifference = colorDelta(this, newColor)
        if (newDifference < lowestDifference) {
            lowestDifference = newDifference
            lowestDifferenceIndex = index
        }
    }

    check(lowestDifferenceIndex >= 0)
    return lowestDifferenceIndex
}

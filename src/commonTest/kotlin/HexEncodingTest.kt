import com.harry1453.klaunchpad.impl.util.parseHexString
import com.harry1453.klaunchpad.impl.util.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HexEncodingTest {
    @Test
    fun testEncode() {
        assertEquals("0102030405", byteArrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte()).toHexString())
        assertEquals("FFA099", byteArrayOf(255.toByte(), 160.toByte(), 153.toByte()).toHexString())
    }

    @Test
    fun testDecode() {
        assertArrayEquals(byteArrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte()), "0102030405".parseHexString())
        assertArrayEquals(byteArrayOf(255.toByte(), 160.toByte(), 153.toByte()), "FFA099".parseHexString())
    }
}

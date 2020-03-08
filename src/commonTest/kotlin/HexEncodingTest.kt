import com.harry1453.launchpad.util.parseHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HexEncodingTest {
    @Test
    fun testEncode() {
        // We haven't implemented this yet
    }

    @Test
    fun testDecode() {
        assertArrayEquals(byteArrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte()), "0102030405".parseHexString())
        assertArrayEquals(byteArrayOf(255.toByte(), 160.toByte(), 153.toByte()), "FFA099".parseHexString())
    }
}

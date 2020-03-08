import com.harry1453.launchpad.impl.util.parseHexString
import kotlin.test.Test

class HexEncodingTest {
    @Test
    fun testDecode() {
        assertArrayEquals(byteArrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte()), "0102030405".parseHexString())
        assertArrayEquals(byteArrayOf(255.toByte(), 160.toByte(), 153.toByte()), "FFA099".parseHexString())
    }
}

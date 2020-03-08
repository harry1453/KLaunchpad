import com.harry1453.launchpad.colour.rgbToVelocity
import kotlin.test.Test
import kotlin.test.assertEquals

class LaunchpadColourUtilsTest {
    @Test
    fun testColourToVelocity() {
        assertEquals(0, rgbToVelocity(0, 0, 0))
    }
}

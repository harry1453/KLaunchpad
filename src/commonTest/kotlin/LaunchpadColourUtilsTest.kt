import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.impl.toVelocity
import kotlin.test.Test
import kotlin.test.assertEquals

class LaunchpadcolorUtilsTest {
    @Test
    fun testcolorToVelocity() {
        assertEquals(0, Color(0, 0, 0).toVelocity())
    }
}

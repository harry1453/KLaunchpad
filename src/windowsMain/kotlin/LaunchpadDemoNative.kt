import com.harry1453.klaunchpad.api.Color
import com.harry1453.klaunchpad.api.Launchpad
import kotlinx.coroutines.runBlocking
import platform.windows.Sleep
import kotlin.random.Random

fun main() {
    Platform.isMemoryLeakCheckerActive = false // FIXME
    val random = Random.Default
    val launchpad = runBlocking { Launchpad.connectToLaunchpadMK2Async().await() }
    launchpad.setPadButtonListener { pad, pressed, _ ->
        val color = Color(random.nextInt(1, 256), random.nextInt(1, 256), random.nextInt(1, 256))
        if (pressed) {
            launchpad.setPadLight(pad, color)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
    Sleep(1000000000u)
}

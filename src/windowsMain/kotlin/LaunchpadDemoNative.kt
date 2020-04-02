import com.harry1453.klaunchpad.api.Launchpad
import kotlinx.coroutines.runBlocking
import platform.windows.Sleep

fun main() {
    runBlocking {
        val launchpad = Launchpad.connectToLaunchpadMK2Async().await()
        println(launchpad)
        Sleep(UInt.MAX_VALUE)
    }
}

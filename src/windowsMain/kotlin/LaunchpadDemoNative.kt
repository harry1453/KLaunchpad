import com.harry1453.launchpad.api.Color
import com.harry1453.launchpad.api.Launchpad

fun main() {
    val color = Color(0, 50, 255)
    val launchpad = Launchpad.connectToLaunchpadMK2()
    launchpad.setPadButtonListener { pad, pressed, _ ->
        if (pressed) {
            launchpad.setPadLight(pad, color)
        } else {
            launchpad.clearPadLight(pad)
        }
    }
}

package app.pocketstats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class FullscreenActivity : ComponentActivity() {

    private val dataViewModel = DataViewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(dataViewModel)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        hideSystemUI()
    }

    private fun hideSystemUI() {
        window.insetsController?.apply {
            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val nanoTime = System.nanoTime()
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (dataViewModel.lastKeyEvent.value == KeyEvent.KEYCODE_VOLUME_DOWN && nanoTime - dataViewModel.lastMissTime.value!! < 1000000000) {
                dataViewModel.threesMissedInc()
                dataViewModel.twosMissedDec()
                dataViewModel.clearLastKeyEvent()
            } else {
                dataViewModel.twosMissedInc()
                dataViewModel.updateLastKeyEvent(KeyEvent.KEYCODE_VOLUME_DOWN)
            }
            dataViewModel.updateLastMissTime(nanoTime)
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (dataViewModel.lastKeyEvent.value == KeyEvent.KEYCODE_VOLUME_UP && nanoTime - dataViewModel.lastMakeTime.value!! < 1000000000) {
                dataViewModel.threesMadeInc()
                dataViewModel.twosMadeDec()
                dataViewModel.clearLastKeyEvent()
            } else {
                dataViewModel.twosMadeInc()
                dataViewModel.updateLastKeyEvent(KeyEvent.KEYCODE_VOLUME_UP)
            }
            dataViewModel.updateLastMakeTime(nanoTime)
        }
        return true
    }
}
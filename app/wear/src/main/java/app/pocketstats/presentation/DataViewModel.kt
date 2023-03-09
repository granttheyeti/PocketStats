package app.pocketstats.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DataViewModel : ViewModel() {
    val upState: MutableStateFlow<Int> = MutableStateFlow(0)
    val downState: MutableStateFlow<Int> = MutableStateFlow(0)

    fun onShotChangeByScroll(
        onUpRegistered: () -> Unit = {},
        onDownRegistered: () -> Unit = {}
    ): (Float) -> Unit {
        return { pixels: Float ->
            if (pixels > 0) {
                upState.update { it.inc() }
                onUpRegistered()
            }
            if (pixels < 0) {
                downState.update { it.inc() }
                onDownRegistered()
            }
        }
    }
}
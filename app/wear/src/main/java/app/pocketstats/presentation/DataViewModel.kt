package app.pocketstats.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class DataViewModel(private val state: SavedStateHandle = SavedStateHandle()) : ViewModel() {
    val upState: MutableStateFlow<Int> = MutableStateFlow(0)
    val downState: MutableStateFlow<Int> = MutableStateFlow(0)

    init {
        upState.update { state.get<Int>("ups") ?: 0 }
        downState.update { state.get<Int>("downs") ?: 0 }
    }

    fun onShotChangeByScroll(
        onUpRegistered: () -> Unit = {},
        onDownRegistered: () -> Unit = {}
    ): (Float) -> Unit {
        return { pixels: Float ->
            if (pixels > 0) {
                val ups = upState.updateAndGet { it.inc() }
                state["ups"] = ups
                onUpRegistered()
            }
            if (pixels < 0) {
                val downs = downState.updateAndGet { it.inc() }
                state["downs"] = downs
                onDownRegistered()
            }
        }
    }
}
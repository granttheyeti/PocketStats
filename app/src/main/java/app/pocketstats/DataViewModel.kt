package app.pocketstats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.*


class DataViewModel : ViewModel() {
    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds
    private var timer: Timer? = null
    fun startTimer() {
        timer = Timer().apply {
            val task = object : TimerTask() {
                override fun run() {
                    _seconds.update { it.inc() }
                }
            }
            scheduleAtFixedRate(task, 1000L, 1000L)
        }
    }

    fun stopTimer() {
        timer?.cancel()
    }

    private val _twosMade = MutableStateFlow(0)
    val twosMade: StateFlow<Int> = _twosMade
    fun twosMadeInc() {
        _twosMade.update { it.inc() }
    }

    fun twosMadeDec() {
        _twosMade.update { it.dec() }
    }

    private val _twosMissed = MutableStateFlow(0)
    val twosMissed: StateFlow<Int> = _twosMissed
    fun twosMissedInc() {
        _twosMissed.update { it.inc() }
    }

    fun twosMissedDec() {
        _twosMissed.update { it.dec() }
    }

    private val _threesMade = MutableStateFlow(0)
    val threesMade: StateFlow<Int> = _threesMade
    fun threesMadeInc() {
        _threesMade.update { it.inc() }
    }

    private val _threesMissed = MutableStateFlow(0)
    val threesMissed: StateFlow<Int> = _threesMissed
    fun threesMissedInc() {
        _threesMissed.update { it.inc() }
    }

    private val _lastKeyEvent = MutableStateFlow(0)
    val lastKeyEvent: StateFlow<Int> = _lastKeyEvent
    fun updateLastKeyEvent(event: Int) {
        _lastKeyEvent.value = event
    }

    fun clearLastKeyEvent() {
        _lastKeyEvent.value = 0
    }

    private val _lastMakeTime = MutableStateFlow(System.nanoTime())
    val lastMakeTime: StateFlow<Long> = _lastMakeTime
    fun updateLastMakeTime(time: Long) {
        _lastMakeTime.value = time
    }

    private val _lastMissTime = MutableStateFlow(System.nanoTime())
    val lastMissTime: StateFlow<Long> = _lastMissTime
    fun updateLastMissTime(time: Long) {
        _lastMissTime.value = time
    }

    fun resetAll() {
        _twosMade.value = 0
        _twosMissed.value = 0
        _threesMade.value = 0
        _threesMissed.value = 0
        _lastKeyEvent.value = 0
        _seconds.value = 0
        _lastMakeTime.value = System.nanoTime()
        _lastMissTime.value = System.nanoTime()
    }
}
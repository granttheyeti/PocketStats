package app.pocketstats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DataViewModel : ViewModel() {
    private val _twosMade = MutableLiveData(0)
    val twosMade: LiveData<Int> = _twosMade
    fun twosMadeInc() {
        _twosMade.value = _twosMade.value?.inc()
    }

    fun twosMadeDec() {
        _twosMade.value = _twosMade.value?.dec()
    }

    private val _twosMissed = MutableLiveData(0)
    val twosMissed: LiveData<Int> = _twosMissed
    fun twosMissedInc() {
        _twosMissed.value = _twosMissed.value?.inc()
    }

    fun twosMissedDec() {
        _twosMissed.value = _twosMissed.value?.dec()
    }

    private val _threesMade = MutableLiveData(0)
    val threesMade: LiveData<Int> = _threesMade
    fun threesMadeInc() {
        _threesMade.value = _threesMade.value?.inc()
    }

    private val _threesMissed = MutableLiveData(0)
    val threesMissed: LiveData<Int> = _threesMissed
    fun threesMissedInc() {
        _threesMissed.value = _threesMissed.value?.inc()
    }

    private val _lastKeyEvent = MutableLiveData(0)
    val lastKeyEvent: LiveData<Int> = _lastKeyEvent
    fun updateLastKeyEvent(event: Int) {
        _lastKeyEvent.value = event
    }

    fun clearLastKeyEvent() {
        _lastKeyEvent.value = 0
    }

    private val _lastMakeTime = MutableLiveData(System.nanoTime())
    val lastMakeTime: LiveData<Long> = _lastMakeTime
    fun updateLastMakeTime(time: Long) {
        _lastMakeTime.value = time
    }

    private val _lastMissTime = MutableLiveData(System.nanoTime())
    val lastMissTime: LiveData<Long> = _lastMissTime
    fun updateLastMissTime(time: Long) {
        _lastMissTime.value = time
    }

    fun resetAll() {
        _twosMade.value = 0
        _twosMissed.value = 0
        _threesMade.value = 0
        _threesMissed.value = 0
        _lastMakeTime.value = System.nanoTime()
        _lastMissTime.value = System.nanoTime()
    }
}
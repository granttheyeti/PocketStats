package app.pocketstats.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import app.pocketstats.R
import app.pocketstats.presentation.theme.PocketStatsTheme
import com.google.android.horologist.compose.rotaryinput.onRotaryInputAccumulated
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val dataViewModel = DataViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(dataViewModel)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
fun WearApp(dataViewModel: DataViewModel) {
    PocketStatsTheme {
        val focusRequester: FocusRequester = rememberActiveFocusRequester()
        val upState by dataViewModel.upState.collectAsState()
        val downState by dataViewModel.downState.collectAsState()
        val haptic = LocalHapticFeedback.current

        Scaffold(
            timeText = {
                TimeText()
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .onRotaryInputAccumulated(
                    onValueChange = dataViewModel.onShotChangeByScroll({
                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
                    }, { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }),
                    rateLimitCoolDownMs = 1000L,
                    minValueChangeDistancePx = 100f
                )
                .focusRequester(focusRequester)
                .focusable()
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    text = stringResource(R.string.app_name)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    fontSize = 72.sp,
                    fontFamily = FontFamily(Font(R.font.bebasneue_regular, FontWeight.Normal)),
                    text = "$upState/$downState"
                )
                AnimatedVisibility(upState + downState > 0) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        text = "${
                            upState.toFloat().div(upState.plus(downState)).times(100).roundToInt()
                        }%"
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(DataViewModel())
}
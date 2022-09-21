package app.pocketstats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.pocketstats.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: DataViewModel = viewModel()) {
    val twosMade by viewModel.twosMade.observeAsState()
    val twosMissed by viewModel.twosMissed.observeAsState()
    val threesMade by viewModel.threesMade.observeAsState()
    val threesMissed by viewModel.threesMissed.observeAsState()

    PocketStatsTheme {
        val showInstructions = remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                TopAppBar(title = { InlineLogo() }, actions = {
                    InstructionsButton(showInstructions)
                    ResetButton(viewModel, snackbarHostState, scope)
                })
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        if (showInstructions.value) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Instructions()
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Stats(
                            ups = twosMade!!,
                            downs = twosMissed!!,
                            ups2 = threesMade!!,
                            downs2 = threesMissed!!
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        )
    }
}

@Composable
fun InstructionsButton(showInstructions: MutableState<Boolean>) {
    IconButton(
        onClick = { showInstructions.value = !showInstructions.value },
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = "Instructions"
        )
    }
}

@Composable
fun ResetButton(
    viewModel: DataViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val s = "Reset Counters"
    IconButton(
        onClick = {
            scope.launch {
                val snackbarResult = snackbarHostState.showSnackbar(
                    "Are you sure?",
                    s,
                    false,
                    SnackbarDuration.Short
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) viewModel.resetAll()
            }
        },
    ) {
        Icon(
            Icons.Filled.Refresh,
            contentDescription = "s"
        )
    }
}

@Composable
fun InlineLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
        )
        AutosizeText(
            "Pocket Stats",
            targetSize = 40.sp,
            modifier = Modifier.padding(start = 8.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun Instructions() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text("Instructions", fontSize = 24.sp, textDecoration = TextDecoration.Underline)
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            "2 Pt Made   -> Volume Up\n2 Pt Missed -> Volume Down\n3 Pt Made   -> Volume Up 2x\n3 Pt Missed -> Volume Down 2x",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text("The screen will stay on until you exit the app", textAlign = TextAlign.Center)
    }
}

@Composable
fun Stats(ups: Int, downs: Int, ups2: Int, downs2: Int) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatLine(ups, downs, "Twos made")
        Spacer(modifier = Modifier.height(24.dp))
        StatLine(ups2, downs2, "Threes made")
        Spacer(modifier = Modifier.height(24.dp))
        StatLine(ups + ups2, downs + downs2, "Field goals")
    }
}

@Composable
fun StatLine(success: Int, failure: Int, metric: String) {
    val total = success + failure
    val percent =
        if (success > 0 || failure > 0) success.toDouble().div(success.plus(failure)).times(100)
            .roundToInt() else 0
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val s = 120.dp
            Box(
                Modifier
                    .size(s)
                    .padding(10.dp), contentAlignment = Alignment.Center
            ) {
                DoughnutChart(
                    values = listOf(success.toFloat(), failure.toFloat()), colors = listOf(
                        ShootingWellColor, ShootingPoorlyColor
                    ), size = s, thickness = 8.dp
                )
                AutosizeText(
                    success.toString(), 70.sp, Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        append("$success / $total = ")

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                shadow = Shadow(
                                    color = getShadow(percent),
                                    blurRadius = 2f
                                )
                            )
                        ) {
                            append("$percent%")
                        }
                    }, fontSize = 16.sp
                )
                Text(
                    metric,
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.bebasneue_regular, FontWeight.Normal))
                )
            }
        }
    }
}

fun getShadow(percent: Int): Color {
    return when {
        percent < 30 -> ShootingPoorlyColor
        percent < 60 -> ShootingOkColor
        else -> ShootingWellColor
    }
}

@Composable
fun AutosizeText(
    text: String,
    targetSize: TextUnit,
    modifier: Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    val textStyleBodyLarge = Typography.bodyLarge.copy(fontSize = targetSize)
    var textStyle by remember(text) { mutableStateOf(textStyleBodyLarge) }
    var readyToDraw by remember(text) { mutableStateOf(false) }
    Text(
        text,
        style = textStyle,
        maxLines = 1,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = {
            if (it.didOverflowWidth || it.didOverflowHeight) {
                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
            } else {
                readyToDraw = true
            }
        },
        textAlign = textAlign,
    )
}

@Composable
fun DoughnutChart(
    values: List<Float>,
    colors: List<Color>,
    size: Dp,
    thickness: Dp
) {

    // Sum of all the values
    val sumOfValues = values.sum()

    // Calculate each proportion
    val proportions = values.map {
        it / sumOfValues
    }

    // Convert each proportion to angle
    val sweepAngles = proportions.map {
        360 * it
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(size.times(0.05f))
    ) {
        var startAngle = 90f

        for (i in values.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Square),
            )
            startAngle += sweepAngles[i]
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val vm = DataViewModel()
    vm.threesMadeInc()
    vm.threesMadeInc()
    vm.threesMissedInc()
    vm.twosMadeInc()
    vm.twosMissedInc()
    vm.twosMissedInc()
    vm.twosMissedInc()
    PocketStatsTheme {
        MyApp(vm)
    }
}
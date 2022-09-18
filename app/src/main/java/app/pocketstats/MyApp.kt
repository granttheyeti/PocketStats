package app.pocketstats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.pocketstats.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun MyApp(dateModel: DataViewModel = viewModel()) {
    val twosMade by dateModel.twosMade.observeAsState()
    val twosMissed by dateModel.twosMissed.observeAsState()
    val threesMade by dateModel.threesMade.observeAsState()
    val threesMissed by dateModel.threesMissed.observeAsState()

    PocketStatsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Logo()
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Tutorial()
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(32.dp))
                Stats(
                    ups = twosMade!!,
                    downs = twosMissed!!,
                    ups2 = threesMade!!,
                    downs2 = threesMissed!!
                )
                Button(onClick = { dateModel.resetAll() }) {
                    Text(text = "Reset Values")
                }
            }
        }
    }
}

@Composable
fun Logo() {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
        )
        Text("Pocket Stats", fontSize = 40.sp, modifier = Modifier.padding(horizontal = 8.dp))
    }
}

@Composable
fun Tutorial() {
    val s = 12.sp
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Tutorial")
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            Text("2 Pointer Made -> Press Volume Up", fontSize = s)
            Text("2 Pointer Missed -> Press Volume Down", fontSize = s)
            Text("3 Pointer Made -> Press Volume Up Twice", fontSize = s)
            Text("3 Pointer Missed -> Press Volume Down Twice", fontSize = s)
            Text("The screen will stay on until you exit the app", fontSize = s)
        }
    }
}

@Composable
fun Stats(ups: Int, downs: Int, ups2: Int, downs2: Int) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        StatLine(ups, downs, "Twos made")
        StatLine(ups2, downs2, "Threes made")
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
                Text(metric, fontSize = 40.sp, fontFamily = FontFamily(Font(R.font.bebasneue_regular, FontWeight.Normal)))
            }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}

fun getShadow(percent: Int): Color {
    return when {
        percent < 30 -> ShootingPoorlyColor
        percent < 60 -> ShootingOkColor
        else -> ShootingWellColor
    }
}

@Composable
fun AutosizeText(text: String, targetSize: TextUnit, modifier: Modifier) {
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
        textAlign = TextAlign.Center,
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
    PocketStatsTheme {
        MyApp()
    }
}
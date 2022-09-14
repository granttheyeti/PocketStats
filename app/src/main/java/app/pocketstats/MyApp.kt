package app.pocketstats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.pocketstats.ui.theme.PocketStatsTheme
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
                Spacer(modifier = Modifier.height(32.dp))
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
//        .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
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
        StatLine(ups, downs, "TWOS MADE")
        StatLine(ups2, downs2, "THREES MADE")
        StatLine(ups + ups2, downs + downs2, "FIELD GOALS")
    }
}

@Composable
fun StatLine(success: Int, failure: Int, metric: String) {
    val total = success + failure
    val percent =
        if (success > 0 || failure > 0) success.toDouble().div(success.plus(failure)).times(100)
            .roundToInt() else 0
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(success.toString(), fontSize = 70.sp, modifier = Modifier.padding(start = 24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$success / $total = $percent%", fontSize = 14.sp)
                Text(metric, fontSize = 30.sp, fontStyle = FontStyle.Italic)
            }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PocketStatsTheme {
        MyApp()
    }
}
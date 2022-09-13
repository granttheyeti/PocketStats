package app.pocketstats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
                modifier = Modifier.fillMaxSize()
            ) {
                Logo()
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
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
        Text("Pocket Stats", fontSize = 40.sp)
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
        }
    }
}

@Composable
fun Stats(ups: Int, downs: Int, ups2: Int, downs2: Int) {
    Column(horizontalAlignment = Alignment.Start) {
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(success.toString(), fontSize = 70.sp)
        Spacer(modifier = Modifier.width(32.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$success / $total = $percent%", fontSize = 14.sp)
            Text(metric, fontSize = 30.sp)
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
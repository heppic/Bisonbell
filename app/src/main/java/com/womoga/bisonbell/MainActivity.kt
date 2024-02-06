package com.womoga.bisonbell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.womoga.bisonbell.data.Datasource
import com.womoga.bisonbell.model.Race
import com.womoga.bisonbell.model.RaceDay
import com.womoga.bisonbell.ui.theme.BisonbellTheme
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BisonbellTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RaceApp()
                }
            }
        }
    }
}

@Composable
fun RaceApp() {
    val races = Datasource().loadRaces()
    val days = ArrayList<RaceDay>()
    for (r in races) {
        for (d in r.raceDays()) {
            days.add(d)
        }
    }
    RaceList(
        raceList = days
    )
}

@Composable
fun RaceList(raceList: List<RaceDay>, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        items(items = raceList) { race ->
            RaceCard(
                race = race,
                modifier = modifier
                    .fillMaxHeight(1.0f)
            )
        }
    }
}

@Composable
fun LeftColumn(race: RaceDay, modifier: Modifier = Modifier){
    val zid = ZoneId.systemDefault()
    val zdt = race.date.atZone(zid)
    val dayOfMonth = zdt.dayOfMonth
    Box(
        modifier = modifier
            .fillMaxHeight(1.0f)
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Text(
            text = dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(0.1f)
                .wrapContentHeight(unbounded = true)
        )
    }
}

@Composable
fun CenterColumn(race: RaceDay, modifier: Modifier = Modifier) {
    var platform = (race.platform.map {Race.toString(it)}).joinToString(" ")

    Column(
        modifier = modifier
            .fillMaxHeight(1.0f)
    )
    {
        Text(
            text = race.raceName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Row(
            modifier = modifier
        ) {
            Text(
                text = Race.toString(race.discipline),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.0f, true)
            )
            Text(
                text = platform,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.0f, true)
            )
            Text(
                text="",
                style = MaterialTheme.typography.titleSmall,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.0f, true)
            )
        }
    }
}

@Composable
fun RightColumn(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .background(Color.Green)
    ) {
        Text(
            text="Right",
            modifier = modifier
        )
    }
}

@Composable
fun RaceCard(race: RaceDay, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(2.dp, 0.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxHeight(1.0f)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            LeftColumn(race, modifier)
            CenterColumn(race, modifier)
        }
        Spacer(modifier = modifier.padding(0.dp, 4.dp))
    }
}
/*    Column(modifier = modifier
        .background(Color.Magenta)
        .fillMaxWidth(0.8f)) {
        Row (
            modifier = modifier
                .background(Color.Green)
                .fillMaxWidth(0.5f)
        ) {
            Text(
                text = race.name,
                color = Color.Red,
                modifier = modifier.padding(2.dp),
                style = MaterialTheme.typography.bodyMedium

            )
        }
        Row(
            modifier = modifier
                .background(Color.Cyan)
                .fillMaxWidth(1.0f)
        )
        {
            Text(
                text = "Col2",
                color = Color.Red,
                modifier = modifier.padding(0.dp),
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Col2",
                color = Color.Red,
                modifier = modifier.padding(0.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}*/



@Preview
@Composable
private fun RaceCardPreview() {
    var races = listOf<Race> (
        Race(
            Race.Discipline.ROAD_WOMENS,
            "UAE Tour",
            Instant.now(),
            Instant.now(),
            listOf(Race.Platform.Discovery)
            ),
        Race(
            Race.Discipline.ROAD_MENS,
            "UAE Tour M",
            Instant.now(),
            Instant.now().plus(31, ChronoUnit.DAYS),
            listOf(Race.Platform.Peacock)
        )
    )

    var days = ArrayList<RaceDay>()
    for (r in races) {
        for (d in r.raceDays()) {
            days.add(d)
        }
    }

    RaceList(days)
    /*RaceList(listOf(
        RaceDay(
            Race.Discpline.ROAD_WOMENS,
            "UAE Tour",
            Instant.now(),
            listOf(Race.Platform.Discovery)
        ),
        RaceDay(
            Race.Discpline.ROAD_MENS,
            "UAE TourX",
            Instant.now().plus(1, ChronoUnit.DAYS),
            listOf(Race.Platform.Peacock)
        ),
        RaceDay(
            Race.Discpline.ROAD_MENS,
            "UAE TourX",
            Instant.now().plus(0, ChronoUnit.DAYS),
            listOf(Race.Platform.Peacock)
        ),
        RaceDay(
            Race.Discpline.ROAD_MENS,
            "UAE TourX",
            Instant.now().plus(1, ChronoUnit.DAYS),
            listOf(Race.Platform.Peacock)
        ),
        RaceDay(
            Race.Discpline.ROAD_MENS,
            "UAE TourX",
            Instant.now().plus(2, ChronoUnit.DAYS),
            listOf(Race.Platform.Peacock)
        )
        ))*/
}
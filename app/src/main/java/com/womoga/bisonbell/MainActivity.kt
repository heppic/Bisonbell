package com.womoga.bisonbell

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.womoga.bisonbell.data.Datasource
import com.womoga.bisonbell.data.Result
import com.womoga.bisonbell.model.Race
import com.womoga.bisonbell.model.RaceDay
import com.womoga.bisonbell.model.RaceMonth
import com.womoga.bisonbell.model.RaceYear
import com.womoga.bisonbell.ui.theme.BisonbellTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.LinkedList

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
                    RaceApp(Modifier)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaceApp(modifier: Modifier) {
    val schedule = remember { mutableStateListOf<RaceMonth>() }
    runBlocking {
        launch {
            val result = withContext(Dispatchers.IO) { Datasource().fetchRaces() }
            when (result) {
                is Result.Success<RaceYear> -> {
                    schedule.clear()
                    val now = LocalDateTime.now()
                    val after = LocalDateTime.of(now.year, now.monthValue, now.dayOfMonth, 0, 0).toInstant(
                        ZoneOffset.UTC)
                    if (now.monthValue <= 1) {
                        schedule.add(
                            RaceMonth(
                                "January",
                                result.data.getRaceDays(Month.JANUARY, after)
                            )
                        )
                    }
                    if (now.monthValue <= 2) {
                        schedule.add(
                            RaceMonth(
                                "February",
                                result.data.getRaceDays(Month.FEBRUARY, after)
                            )
                        )
                    }
                    if (now.monthValue <= 3) {
                        schedule.add(
                            RaceMonth(
                                "March",
                                result.data.getRaceDays(Month.MARCH, after)
                            )
                        )
                    }
                    if (now.monthValue <= 4) {
                        schedule.add(
                            RaceMonth(
                                "April",
                                result.data.getRaceDays(Month.APRIL, after)
                            )
                        )
                    }
                    if (now.monthValue <= 5) {
                        schedule.add(RaceMonth("May", result.data.getRaceDays(Month.MAY, after)))
                    }
                    if (now.monthValue <= 6) {
                        schedule.add(RaceMonth("June", result.data.getRaceDays(Month.JUNE, after)))
                    }
                    if (now.monthValue <= 7) {
                        schedule.add(RaceMonth("July", result.data.getRaceDays(Month.JULY, after)))
                    }
                    if (now.monthValue <= 8) {
                        schedule.add(
                            RaceMonth(
                                "August",
                                result.data.getRaceDays(Month.AUGUST, after)
                            )
                        )
                    }
                    if (now.monthValue <= 9) {
                        schedule.add(
                            RaceMonth(
                                "September",
                                result.data.getRaceDays(Month.SEPTEMBER, after)
                            )
                        )
                    }
                    if (now.monthValue <= 10) {
                        schedule.add(
                            RaceMonth(
                                "October",
                                result.data.getRaceDays(Month.OCTOBER, after)
                            )
                        )
                    }
                    if (now.monthValue <= 11) {
                        schedule.add(
                            RaceMonth(
                                "November",
                                result.data.getRaceDays(Month.NOVEMBER, after)
                            )
                        )
                    }
                    if (now.monthValue <= 12) {
                        schedule.add(
                            RaceMonth(
                                "December",
                                result.data.getRaceDays(Month.DECEMBER, after)
                            )
                        )
                    }
                }

                else -> {}
            }
        }
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
/*            ExtendedFloatingActionButton(
                text = { Text("Filter") },
                icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )*/
        }
    ) { innerPadding ->
        Column {
            // Screen content
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                items(items = schedule) { month ->
                    RaceList(month.name, month.days)
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Row() {
                        // Sheet content
                        Column()
                        {
                            Race.disciplineStrings().forEach { str ->
                                Row {
                                    Checkbox(
                                        checked = false,
                                        onCheckedChange = {}
                                    )
                                    Text(str)
                                }
                            }
                            Button(onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            }) {
                                Text("Hide bottom sheet")
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun RaceList(name: String, raceList: List<RaceDay>, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        Text(name)
        raceList.forEach({race ->
            RaceCard(
                race = race,
                modifier = modifier
                    .fillMaxHeight(1.0f)
            )
        })
    }
}

@Composable
fun LeftColumn(race: RaceDay, modifier: Modifier = Modifier){
    val zdt = race.start.atOffset(ZoneOffset.UTC)
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
    var platform = (race.platform.map {Race.platformString(it)}).joinToString(" ")
    var checked by remember { mutableStateOf(false)}
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
                text = Race.disciplineString(race.discipline),
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
/*            Switch(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.0f, true),
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )*/
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



@Preview
@Composable
private fun RaceCardPreview() {
    var races = listOf<Race>(
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

    RaceList("tesT", days.toList())
}
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

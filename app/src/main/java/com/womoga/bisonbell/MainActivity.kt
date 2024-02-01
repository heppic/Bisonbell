package com.womoga.bisonbell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.womoga.bisonbell.data.Datasource
import com.womoga.bisonbell.model.Race
import com.womoga.bisonbell.ui.theme.BisonbellTheme
import java.time.Instant

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
    RaceList(
        raceList = Datasource().loadRaces(),
    )
}

@Composable
fun RaceList(raceList: List<Race>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = raceList) { race ->
            RaceCard(
                race = race,
                modifier = Modifier.padding(1.dp)
            )
        }
    }
}
@Composable
fun RaceCard(race: Race, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Column (
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(Color.Green)
        ){
            Text(
                text = race.name,
                color = Color.Red,
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.bodySmall

            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.Black)
        )
        {
            Text(
                text = "Col2",
                color = Color.Red,
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Col2",
                color = Color.Red,
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}



@Preview
@Composable
private fun RaceCardPreview() {
    RaceList(listOf<Race>(
        Race(
            Race.Discpline.ROAD_WOMENS,
            "UAE Tour",
            Instant.now(),
            Instant.now()),
        Race(
            Race.Discpline.ROAD_WOMENS,
            "UAE TourX",
            Instant.now(),
            Instant.now()),
        ));
}
package com.womoga.bisonbell.data
import android.content.ContentResolver
import android.net.Uri
import android.util.JsonReader
import android.util.Log
import com.womoga.bisonbell.model.Race
import com.womoga.bisonbell.model.RaceDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.stream.Collectors

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}


class Datasource {

    private fun toInstant(year: Int, month: Int, day: Int): Instant {
        val c1: Calendar = Calendar.getInstance();
        c1.set(Calendar.YEAR, year);
        c1.set(Calendar.MONTH, month);
        c1.set(Calendar.DAY_OF_MONTH, day);
        return c1.getTime().toInstant()
    }

    suspend fun fetchRaces(): Result<String> {
        val url =
            URL("https://raw.githubusercontent.com/heppic/Bisonbell/initial/app/schedule/2024.json")
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"


            return Result.Success(BufferedReader(InputStreamReader(inputStream)).lines().collect(
                Collectors.joining()));
        }
        return Result.Error(Exception("Cannot open HttpURLConnection"))
    }
    fun loadNetRaces() {
        runBlocking {
            val result = withContext(Dispatchers.IO) { Datasource().fetchRaces(); }
            when(result) {
                is Result.Success<String> -> {
                    val raceDays:Array<ArrayList<RaceDay>?> = arrayOfNulls(12);
                    for (i in 0..11) {
                        raceDays[i] = ArrayList<RaceDay>();
                    }
                    val json = JSONObject(result.data)
                    for (platform_string in json.keys()) {
                        val discipline_tier = json.getJSONObject(platform_string)
                        for (discipline_string in discipline_tier.keys()) {
                            val race_tier = discipline_tier.getJSONObject(discipline_string)
                            for (race_name in race_tier.keys()) {
                                val dates = race_tier.getString(race_name);
                                val days = Race.fromStringData(platform_string, discipline_string, race_name, dates).raceDays()
                                for (d in days) {
                                    val zid = ZoneId.systemDefault()
                                    val zdt = d.date.atZone(zid)
                                    val month = zdt.month.value
                                    raceDays[month-1]?.add(d)
                                }
                            }
                        }
                    }

                    return@runBlocking arrayOf(
                        raceDays[0]!!.toList(),
                        raceDays[1]!!.toList(),
                        raceDays[2]!!.toList(),
                        raceDays[3]!!.toList(),
                        raceDays[4]!!.toList(),
                        raceDays[5]!!.toList(),
                        raceDays[6]!!.toList(),
                        raceDays[7]!!.toList(),
                        raceDays[8]!!.toList(),
                        raceDays[9]!!.toList(),
                        raceDays[10]!!.toList(),
                        raceDays[11]!!.toList()
                    )
                }
                else ->
                    Log.v("freeze", "Sad")

            }
        }
    }

    fun loadRaces(contentResolver: ContentResolver): List<Race> {
        return listOf<Race>(
            Race(Race.Discipline.ROAD_WOMENS,
                "UAE Tour",
                toInstant(2024, 2, 8),
                toInstant(2024, 3, 11),
                listOf(Race.Platform.Discovery)),
            Race(Race.Discipline.ROAD_MENS,
                "Figueira Champions Classic",
                toInstant(2024, 2, 10),
                toInstant(2024, 2, 10),
                listOf(Race.Platform.Discovery)),
            Race(Race.Discipline.ROAD_MENS,
                "Vuelta A Murcia",
                toInstant(2024, 2, 10),
                toInstant(2024, 2, 10),
                listOf(Race.Platform.Discovery))
        )
    }
}
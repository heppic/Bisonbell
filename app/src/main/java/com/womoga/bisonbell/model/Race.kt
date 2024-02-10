package com.womoga.bisonbell.model

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar

data class Race(
    val discipline: Discipline,
    val name: String,
    val firstDay: Instant,
    val lastDay: Instant,
    val platform: List<Platform>
) {
    enum class Discipline {
        ROAD_MENS,
        ROAD_WOMENS,
        CYCLOCROSS_MENS,
        CYCLOCROSS_WOMENS
    }

    enum class Platform {
        Peacock,
        Discovery
    }

    fun raceDays(): List<RaceDay> {
        val raceDays: ArrayList<RaceDay> = ArrayList<RaceDay>();
        for (i in 0 ..ChronoUnit.DAYS.between(firstDay, lastDay)) {

            raceDays.add(
                RaceDay(discipline, name, firstDay.plus(i, ChronoUnit.DAYS), platform)
            )
        }
        return raceDays.toList()
    }

    companion object {
        fun toString(discipline: Discipline): String {
            return when (discipline) {
                Race.Discipline.CYCLOCROSS_MENS -> "Cyclocross (M)"
                Race.Discipline.CYCLOCROSS_MENS -> "Cyclocross (W)"
                Race.Discipline.ROAD_MENS -> "Road (M)"
                Race.Discipline.ROAD_WOMENS -> "Road(W)"
                else -> "Unknown"
            }
        }

        private fun toInstant(year: Int, month: Int, day: Int): Instant {
            val c1: Calendar = Calendar.getInstance();
            c1.set(year, month-1, day);
            return c1.getTime().toInstant()
        }
        private fun toMonth(monthString: String): Int {
            return when (monthString.uppercase()) {
                "JAN" -> 1
                "FEB" -> 2
                "MAR" -> 3
                "APR" -> 4
                "MAY" -> 5
                "JUN" -> 6
                "JUL" -> 7
                "AUG" -> 8
                "SEP" -> 9
                "OCT" -> 10
                "NOV" -> 11
                "DEC" -> 12
                else -> 0
            }
        }
        fun fromStringData(platform: String, discipline: String, name: String, dates: String): Race {
            var firstDay: Instant;
            var lastDay: Instant;
            Log.v("WORM", "fromStringData")
            Log.v("WORM", platform+" "+discipline+" "+name+" "+dates)
            if (dates.contains('-')) {
                val tokens = dates.trim().split('-')
                Log.v("WORM",tokens[0])
                Log.v("WORM",tokens[1])
                val firstTokens = tokens[0].trim().split(' ')
                val secondTokens = tokens[1].trim().split(' ')
                firstDay = toInstant(
                    Calendar.getInstance().get(Calendar.YEAR),
                    toMonth(firstTokens[1].trim()),
                    firstTokens[0].trim().toInt()
                )
                lastDay = toInstant(
                    Calendar.getInstance().get(Calendar.YEAR),
                    toMonth(secondTokens[1].trim()),
                    secondTokens[0].trim().toInt()
                )

            } else {
                val tokens = dates.trim().split(' ')
                Log.v("WORM",tokens[0])
                Log.v("WORM",tokens[1])
                firstDay = toInstant(
                    Calendar.getInstance().get(Calendar.YEAR),
                    toMonth(tokens[1].trim()),
                    tokens[0].trim().toInt()
                )
                lastDay = firstDay
            }

            val disciplineEnum = when(discipline.lowercase()) {
                "cyclocross men" -> Race.Discipline.CYCLOCROSS_MENS
                "cyclocross women" -> Race.Discipline.CYCLOCROSS_WOMENS
                "road men" -> Race.Discipline.ROAD_MENS
                "road women" -> Race.Discipline.ROAD_WOMENS
                else -> Race.Discipline.CYCLOCROSS_MENS
            }

            val platformEnum = when (platform.lowercase()) {
                "discovery" -> Race.Platform.Discovery
                "peacock" -> Race.Platform.Peacock
                else -> Race.Platform.Discovery
            }

            return Race(disciplineEnum, name, firstDay, lastDay, listOf(platformEnum))
        }

        fun fromStringDataRegex(platform: String, discipline: String, name: String, dates: String): Race {
            var firstDay: Instant;
            var lastDay: Instant;

            val regex = Regex("""(\d\d?)\s([A-Za-z]{3})(?:\s*-\s*)?(\d\d?)?\\s?([A-Za-z]{3})?""")
            val matchResult = regex.find(dates)

            Log.v("WORM", matchResult!!.groups[0]!!.value.toString())
            Log.v("WORM", matchResult!!.groups[1]!!.value.toString())
            firstDay = toInstant(
                Calendar.getInstance().get(Calendar.YEAR),
                toMonth(matchResult!!.groups[1]!!.value),
                matchResult.groups[0]!!.value.toInt()
            )

            if (matchResult.groups.size == 2) {
                lastDay = firstDay
            } else {
                lastDay = toInstant(
                    Calendar.getInstance().get(Calendar.YEAR),
                    toMonth(matchResult!!.groups[3]!!.value),
                    matchResult.groups[2]!!.value.toInt()
                )
            }

            val discipline_enum = when(discipline.lowercase()) {
                "cyclocross men" -> Race.Discipline.CYCLOCROSS_MENS
                "cyclocross women" -> Race.Discipline.CYCLOCROSS_WOMENS
                "road men" -> Race.Discipline.ROAD_MENS
                "road women" -> Race.Discipline.ROAD_WOMENS
                else -> Race.Discipline.CYCLOCROSS_MENS
            }

            val platform_enum = when (platform.lowercase()) {
                "discovery" -> Race.Platform.Discovery
                "peacock" -> Race.Platform.Peacock
                else -> Race.Platform.Discovery
            }

            return Race(discipline_enum, name, firstDay, lastDay, listOf(platform_enum))
        }

        fun toString(platform: Platform): String {
            return when (platform) {
                Race.Platform.Discovery -> "D"
                Race.Platform.Peacock -> "P"
                else -> "Unknown"
            }
        }
    }
}

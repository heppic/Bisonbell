package com.womoga.bisonbell.model

import java.time.Instant
import java.time.temporal.ChronoUnit

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

        fun toString(platform: Platform): String {
            return when (platform) {
                Race.Platform.Discovery -> "D"
                Race.Platform.Peacock -> "P"
                else -> "Unknown"
            }
        }
    }
}

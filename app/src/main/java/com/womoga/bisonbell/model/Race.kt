package com.womoga.bisonbell.model

import java.time.Instant
import java.time.temporal.ChronoUnit

data class Race(
    val discipline: Discpline,
    val name: String,
    val firstDay: Instant,
    val lastDay: Instant,
    val platform: List<Platform>
) {
    enum class Discpline {
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
}

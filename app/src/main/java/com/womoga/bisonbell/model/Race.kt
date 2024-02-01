package com.womoga.bisonbell.model

import java.time.Instant

data class Race(
    val discipline: Discpline,
    val name: String,
    val firstDay: Instant,
    val lastDay: Instant
) {
    enum class Discpline {
        ROAD_MENS,
        ROAD_WOMENS,
        CYCLOCROSS_MENS,
        CYCLOCROSS_WOMENS
    }
}

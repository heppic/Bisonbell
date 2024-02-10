package com.womoga.bisonbell.model

import java.time.Instant
import java.time.temporal.ChronoUnit
data class RaceDay (
    val discipline: Race.Discipline,
    val raceName: String,
    val start: Instant,
    val platform: List<Race.Platform>
)


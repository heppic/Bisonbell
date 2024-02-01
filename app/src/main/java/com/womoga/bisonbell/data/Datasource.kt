package com.womoga.bisonbell.data
import com.womoga.bisonbell.model.Race
import java.time.Instant
import java.util.Calendar
import java.util.Date

class Datasource {

    private fun toInstant(year: Int, month: Int, day: Int): Instant {
        val c1: Calendar = Calendar.getInstance();
        c1.set(Calendar.YEAR, year);
        c1.set(Calendar.MONTH, month);
        c1.set(Calendar.DAY_OF_MONTH, day);
        return c1.getTime().toInstant();
    }
    fun loadRaces(): List<Race> {
        return listOf<Race>(
            Race(Race.Discpline.ROAD_WOMENS,
                "UAE Tour",
                toInstant(2024, 2, 8),
                toInstant(2024, 2, 11)),
            Race(Race.Discpline.ROAD_MENS,
                "Figueira Champions Classic",
                toInstant(2024, 2, 10),
                toInstant(2024, 2, 10)),
            Race(Race.Discpline.ROAD_MENS,
                "Vuelta A Murcia",
                toInstant(2024, 2, 10),
                toInstant(2024, 2, 10))
        )
    }
}
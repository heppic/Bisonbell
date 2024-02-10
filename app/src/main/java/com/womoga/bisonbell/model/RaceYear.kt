package com.womoga.bisonbell.model

import android.util.Log
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.LinkedList

class RaceYear {
    private val schedule = HashMap<Month, LinkedList<RaceDay>>();

    fun addRace(race: Race) {
        for (day in race.raceDays()) {
            val zdt = day.start.atOffset(ZoneOffset.UTC)
            Log.v("WORM", zdt.toString())
            val dayOfMonth = zdt.dayOfMonth
            if (!schedule.containsKey(zdt.month)) {
                schedule[zdt.month] = LinkedList<RaceDay>();
            }
            schedule[zdt.month]!!.add(day)
        }
    }

    fun getRaceDays(month: Month) : List<RaceDay> {
        return schedule.getOrDefault(month, LinkedList<RaceDay>().toList())
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("(JAN ")
        builder.append(schedule[Month.JANUARY])
        builder.append(") ")
        builder.append("(FEB ")
        builder.append(schedule[Month.FEBRUARY])
        builder.append(") ")
        builder.append("(MAR ")
        builder.append(schedule[Month.MARCH])
        builder.append(") ")
        return builder.toString()
    }
}
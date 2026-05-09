package com.example.mindstreak.feature.habit_detail

data class DayCell(val day: Int, val level: Int)

val MONTH_GRID: List<DayCell?> = List(35) { i ->
    if (i < 3) null
    else {
        val day = i - 2
        if (day > 30) null
        else DayCell(day, (0..3).random())
    }
}

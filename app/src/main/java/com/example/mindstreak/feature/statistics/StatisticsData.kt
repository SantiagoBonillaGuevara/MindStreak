package com.example.mindstreak.feature.statistics

fun getBarChartData(tabIndex: Int): List<Pair<String, Float>> = when (tabIndex) {
    0 -> listOf("Mon" to 0.6f, "Tue" to 1.0f, "Wed" to 0.7f, "Thu" to 1.0f, "Fri" to 0.8f, "Sat" to 0.5f, "Sun" to 0.6f)
    1 -> listOf("Wk 1" to 0.5f, "Wk 2" to 0.7f, "Wk 3" to 0.6f, "Wk 4" to 0.8f)
    2 -> listOf("Jan" to 0.6f, "Feb" to 0.8f, "Mar" to 0.9f, "Apr" to 0.7f, "May" to 0f, "Jun" to 0f, "Jul" to 0f, "Aug" to 0f, "Sep" to 0f, "Oct" to 0f, "Nov" to 0f, "Dec" to 0f)
    else -> emptyList()
}

val TREND_DATA = listOf(60f, 65f, 58f, 72f, 75f, 80f, 78f, 85f, 87f)

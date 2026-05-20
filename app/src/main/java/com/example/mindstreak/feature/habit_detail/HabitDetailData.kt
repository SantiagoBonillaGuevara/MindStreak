package com.example.mindstreak.feature.habit_detail

import com.example.mindstreak.data.model.Habit
import java.time.LocalDate

data class DayCell(val day: Int, val level: Int)

class HabitDetailProcessor(private val habit: Habit) {

    // 1. Generar la cuadrícula del calendario para el mes actual
    fun getMonthGrid(): List<DayCell?> {
        val today = LocalDate.now()
        val firstDayOfMonth = today.withDayOfMonth(1)
        
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) to 7 (Sun)
        val offset = startDayOfWeek - 1 // Días vacíos al inicio (suponiendo que la cuadrícula empieza el Lunes)
        
        val grid = mutableListOf<DayCell?>()
        
        // Espacios en blanco al principio
        repeat(offset) { grid.add(null) }
        
        // Días del mes
        for (day in 1..today.lengthOfMonth()) {
            val dateStr = today.withDayOfMonth(day).toString()
            val completed = habit.completionLog[dateStr] == true
            // nivel 3 si completado, 0 si no (para el heatmap)
            grid.add(DayCell(day, if (completed) 3 else 0))
        }
        
        // Rellenar hasta completar semanas (opcional, para estética de 35 o 42 celdas)
        while (grid.size % 7 != 0) {
            grid.add(null)
        }
        
        return grid
    }

    // 2. Calcular tasa de cumplimiento del mes actual vs mes pasado
    fun getMonthlyComparison(): String {
        val today = LocalDate.now()
        val currentMonth = today.monthValue
        val lastMonth = today.minusMonths(1).monthValue
        val currentYear = today.year
        val lastMonthYear = today.minusMonths(1).year

        val currentMonthLogs = habit.completionLog.filter { (date, _) ->
            val d = LocalDate.parse(date)
            d.monthValue == currentMonth && d.year == currentYear
        }
        
        val lastMonthLogs = habit.completionLog.filter { (date, _) ->
            val d = LocalDate.parse(date)
            d.monthValue == lastMonth && d.year == lastMonthYear
        }

        if (lastMonthLogs.isEmpty()) return "0% vs last month"

        val currentRate = currentMonthLogs.values.count { it }.toFloat() / today.lengthOfMonth()
        val lastRate = lastMonthLogs.values.count { it }.toFloat() / today.minusMonths(1).lengthOfMonth()
        
        val diff = ((currentRate - lastRate) / lastRate) * 100
        val sign = if (diff >= 0) "+" else ""
        return "$sign${diff.toInt()}% vs last month"
    }
}

package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.model.Category
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    val habitsFlow: Flow<List<Habit>>
    val habitsAllFlow: Flow<List<Habit>> // Todos los hábitos activos con logs del mes
    suspend fun saveHabits(habits: List<Habit>)
    suspend fun addHabit(habit: Habit)
    suspend fun deleteHabit(id: String)
    suspend fun updateHabit(habit: Habit)
    suspend fun toggleHabitLog(habitId: String, date: String, completed: Boolean)
    suspend fun getCategories(): List<Category>
    suspend fun getTotalHabitLogsCount(): Int
    fun refresh()
}

package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    val habitsFlow: Flow<List<Habit>>
    suspend fun saveHabits(habits: List<Habit>)
    suspend fun addHabit(habit: Habit)
    suspend fun deleteHabit(id: String)
    suspend fun updateHabit(habit: Habit)
}

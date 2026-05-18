package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.model.Category
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    val habitsFlow: Flow<List<Habit>>
    suspend fun saveHabits(habits: List<Habit>)
    suspend fun addHabit(habit: Habit)
    suspend fun deleteHabit(id: String)
    suspend fun updateHabit(habit: Habit)
    suspend fun getCategories(): List<Category>
    fun refresh()
}

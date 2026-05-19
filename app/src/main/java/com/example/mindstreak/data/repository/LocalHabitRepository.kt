package com.example.mindstreak.data.repository

import android.content.Context
import com.example.mindstreak.data.local.HabitsStore
import com.example.mindstreak.data.local.habitsDataStore
import com.example.mindstreak.data.local.toHabit
import com.example.mindstreak.data.local.toSerializable
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.model.Category
import com.example.mindstreak.data.mock.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalHabitRepository(private val context: Context) : HabitRepository {

    override val habitsFlow: Flow<List<Habit>> = context.habitsDataStore.data
        .map { store -> store.habits.map { it.toHabit() } }

    override val habitsAllFlow: Flow<List<Habit>> = habitsFlow

    override suspend fun saveHabits(habits: List<Habit>) {
        context.habitsDataStore.updateData {
            HabitsStore(habits.map { it.toSerializable() })
        }
    }

    override suspend fun addHabit(habit: Habit) {
        val currentHabits = habitsFlow.first()
        saveHabits(currentHabits + habit)
    }

    override suspend fun deleteHabit(id: String) {
        val currentHabits = habitsFlow.first()
        saveHabits(currentHabits.filter { it.id != id })
    }

    override suspend fun updateHabit(habit: Habit) {
        val currentHabits = habitsFlow.first()
        saveHabits(currentHabits.map { if (it.id == habit.id) habit else it })
    }

    override suspend fun toggleHabitLog(
        habitId: String,
        date: String,
        completed: Boolean
    ) {
        val currentHabits = habitsFlow.first()
        val updatedHabits = currentHabits.map { habit ->
            if (habit.id == habitId) {
                val newLog = habit.completionLog.toMutableMap()
                newLog[date] = completed
                habit.copy(completionLog = newLog)
            } else habit
        }
        saveHabits(updatedHabits)
    }

    override suspend fun getCategories(): List<Category> {
        return MockData.CATEGORIES
    }

    override suspend fun getTotalHabitLogsCount(): Int {
        val currentHabits = habitsFlow.first()
        return currentHabits.sumOf { it.completionLog.values.count { completed -> completed } }
    }

    override fun refresh() {
        // No-op for local as it uses DataStore which is already reactive
    }
}

package com.example.mindstreak.data.repository

import android.content.Context
import com.example.mindstreak.data.local.HabitsStore
import com.example.mindstreak.data.local.habitsDataStore
import com.example.mindstreak.data.local.toHabit
import com.example.mindstreak.data.local.toSerializable
import com.example.mindstreak.data.model.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitRepository(private val context: Context) {

    // Flow que emite cada vez que cambia el storage — equivalente al useEffect de lectura
    val habitsFlow: Flow<List<Habit>> = context.habitsDataStore.data
        .map { store -> store.habits.map { it.toHabit() } }

    suspend fun saveHabits(habits: List<Habit>) {
        context.habitsDataStore.updateData {
            HabitsStore(habits.map { it.toSerializable() })
        }
    }
}
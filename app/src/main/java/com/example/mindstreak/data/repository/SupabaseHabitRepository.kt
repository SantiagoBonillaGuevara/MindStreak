package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.remote.dto.HabitDto
import com.example.mindstreak.data.remote.dto.toDomain
import com.example.mindstreak.data.remote.dto.toDto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.github.jan.supabase.auth.auth

class SupabaseHabitRepository : HabitRepository {

    private val client = SupabaseClientProvider.client

    override val habitsFlow: Flow<List<Habit>> = flow {
        try {
            val userId = client.auth.currentSessionOrNull()?.user?.id
            if (userId != null) {
                val habits = client.postgrest["habits"]
                    .select()
                    .decodeList<HabitDto>()
                    .map { it.toDomain() }
                emit(habits)
            } else {
                emit(emptyList())
            }
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun saveHabits(habits: List<Habit>) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        val dtos = habits.map { it.toDto(userId) }
        client.postgrest["habits"].upsert(dtos)
    }

    override suspend fun addHabit(habit: Habit) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        client.postgrest["habits"].insert(habit.toDto(userId))
    }

    override suspend fun deleteHabit(id: String) {
        client.postgrest["habits"].delete {
            filter {
                eq("id", id)
            }
        }
    }

    override suspend fun updateHabit(habit: Habit) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        client.postgrest["habits"].update(habit.toDto(userId)) {
            filter {
                eq("id", habit.id)
            }
        }
    }
}

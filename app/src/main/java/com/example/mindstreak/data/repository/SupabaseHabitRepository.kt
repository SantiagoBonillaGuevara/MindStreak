package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.remote.dto.HabitDto
import com.example.mindstreak.data.remote.dto.toDomain
import com.example.mindstreak.data.remote.dto.toDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.*
import android.util.Log

import com.example.mindstreak.data.remote.dto.HabitLogDto

class SupabaseHabitRepository : HabitRepository {

    private val client by lazy { SupabaseClientProvider.client }
    private val TAG = "SupabaseHabitRepo"

    override val habitsFlow: Flow<List<Habit>> = client.auth.sessionStatus
        .onStart { Log.d(TAG, "habitsFlow collection started") }
        .flatMapLatest { status ->
            Log.d(TAG, "Auth status changed: $status")
            if (status is SessionStatus.Authenticated) {
                flow {
                    try {
                        val userId = status.session.user?.id ?: return@flow
                        Log.d(TAG, "Fetching habits for $userId")
                        val habitsDto = client.postgrest["habits"]
                            .select()
                            .decodeList<HabitDto>()
                        
                        Log.d(TAG, "Habits fetched: ${habitsDto.size}")
                        
                        val logsDto = try {
                            Log.d(TAG, "Fetching logs for $userId")
                            client.postgrest["habit_logs"]
                                .select {
                                    filter {
                                        eq("user_id", userId)
                                    }
                                }
                                .decodeList<HabitLogDto>()
                        } catch (e: Exception) {
                            Log.w(TAG, "Error fetching logs (maybe table empty): ${e.message}")
                            emptyList<HabitLogDto>()
                        }
                        
                        val logsByHabit = logsDto.groupBy { it.habitId }
                        
                        val habits = habitsDto.map { hDto ->
                            val hLogs = logsByHabit[hDto.id] ?: emptyList()
                            val completionLog = hLogs.associate { it.completedDate to it.completed }
                            hDto.toDomain(completionLog)
                        }
                        Log.d(TAG, "Emitting ${habits.size} habits to flow")
                        emit(habits)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching habits/logs: ${e.message}", e)
                        emit(emptyList())
                    }
                }
            } else {
                Log.d(TAG, "Not authenticated, emitting empty habits")
                flowOf(emptyList())
            }
        }

    override suspend fun saveHabits(habits: List<Habit>) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            val habitsDto = habits.map { it.toDto(userId) }
            client.postgrest["habits"].upsert(habitsDto)
            
            // Persistimos también los logs de completación
            val allLogs = habits.flatMap { h ->
                h.completionLog.map { (date, completed) ->
                    HabitLogDto(habitId = h.id, userId = userId, completedDate = date, completed = completed)
                }
            }
            if (allLogs.isNotEmpty()) {
                client.postgrest["habit_logs"].upsert(allLogs)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving habits/logs: ${e.message}")
        }
    }

    override suspend fun addHabit(habit: Habit) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            client.postgrest["habits"].insert(habit.toDto(userId))
        } catch (e: Exception) {
            Log.e(TAG, "Error adding habit: ${e.message}")
        }
    }

    override suspend fun deleteHabit(id: String) {
        try {
            client.postgrest["habits"].delete {
                filter {
                    eq("id", id)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting habit: ${e.message}")
        }
    }

    override suspend fun updateHabit(habit: Habit) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            client.postgrest["habits"].update(habit.toDto(userId)) {
                filter {
                    eq("id", habit.id)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating habit: ${e.message}")
        }
    }
}

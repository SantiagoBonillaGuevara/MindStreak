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
import com.example.mindstreak.data.remote.dto.CategoryDto
import com.example.mindstreak.data.model.Category

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine

import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SupabaseHabitRepository : HabitRepository {

    private val client by lazy { SupabaseClientProvider.client }
    private val tag = "SupabaseHabitRepo"
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val habitsFlow: Flow<List<Habit>> = combine(
        client.auth.sessionStatus,
        refreshTrigger
    ) { status, _ -> status }
        .onStart { Log.d(tag, "habitsFlow collection started") }
        .flatMapLatest { status ->
            Log.d(tag, "Auth status changed or refresh triggered: $status")
            if (status is SessionStatus.Authenticated) {
                flow {
                    try {
                        val userId = status.session.user?.id ?: return@flow
                        val today = java.time.LocalDate.now()
                        val sevenDaysAgo = today.minusDays(7)
                        
                        Log.d(tag, "Fetching logs for $userId from $sevenDaysAgo to $today")
                        
                        // 1. Obtener logs de los últimos 7 días
                        val logsDto = try {
                            client.postgrest["habit_logs"]
                                .select {
                                    filter {
                                        eq("user_id", userId)
                                        gte("completed_date", sevenDaysAgo.toString())
                                        lte("completed_date", today.toString())
                                    }
                                }
                                .decodeList<HabitLogDto>()
                        } catch (e: Exception) {
                            Log.w(tag, "Error fetching logs: ${e.message}")
                            emptyList()
                        }

                        // 2. Identificar qué hábitos tienen registro hoy
                        val todayStr = today.toString()
                        val habitIdsForToday = logsDto
                            .filter { it.completedDate == todayStr }
                            .map { it.habitId }
                            .distinct()

                        if (habitIdsForToday.isEmpty()) {
                            Log.d(tag, "No habits scheduled for today")
                            emit(emptyList())
                            return@flow
                        }

                        // 3. Obtener solo esos hábitos
                        val habitsDto = client.postgrest["habits"]
                            .select {
                                filter {
                                    isIn("id", habitIdsForToday)
                                    eq("is_active", true)
                                }
                            }
                            .decodeList<HabitDto>()
                        
                        val logsByHabit = logsDto.groupBy { it.habitId }
                        
                        val habits = habitsDto.map { hDto ->
                            val hLogs = logsByHabit[hDto.id] ?: emptyList()
                            val completionLog = hLogs.associate { it.completedDate to it.completed }
                            hDto.toDomain(completionLog)
                        }
                        
                        Log.d(tag, "Emitting ${habits.size} habits scheduled for today")
                        emit(habits)
                    } catch (e: Exception) {
                        Log.e(tag, "Error fetching habits/logs: ${e.message}", e)
                        emit(emptyList())
                    }
                }
            } else {
                Log.d(tag, "Not authenticated, emitting empty habits")
                flowOf(emptyList())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val habitsAllFlow: Flow<List<Habit>> = combine(
        client.auth.sessionStatus,
        refreshTrigger
    ) { status, _ -> status }
        .flatMapLatest { status ->
            if (status is SessionStatus.Authenticated) {
                flow {
                    try {
                        val userId = status.session.user?.id ?: return@flow
                        val today = java.time.LocalDate.now()
                        val firstOfMonth = today.withDayOfMonth(1)
                        
                        // Obtener todos los hábitos activos
                        val habitsDto = client.postgrest["habits"]
                            .select {
                                filter {
                                    eq("user_id", userId)
                                    eq("is_active", true)
                                }
                            }
                            .decodeList<HabitDto>()

                        // Obtener logs del mes
                        val logsDto = client.postgrest["habit_logs"]
                            .select {
                                filter {
                                    eq("user_id", userId)
                                    gte("completed_date", firstOfMonth.toString())
                                    lte("completed_date", today.toString())
                                }
                            }
                            .decodeList<HabitLogDto>()

                        val logsByHabit = logsDto.groupBy { it.habitId }
                        val habits = habitsDto.map { hDto ->
                            val hLogs = logsByHabit[hDto.id] ?: emptyList()
                            val completionLog = hLogs.associate { it.completedDate to it.completed }
                            hDto.toDomain(completionLog)
                        }
                        emit(habits)
                    } catch (e: Exception) {
                        Log.e(tag, "Error in habitsAllFlow: ${e.message}")
                        emit(emptyList())
                    }
                }
            } else {
                flowOf(emptyList())
            }
        }

    override suspend fun getTotalHabitLogsCount(): Int {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return 0
        return try {
            val response = client.postgrest["habit_logs"]
                .select(columns = Columns.list("id")) {
                    count(Count.EXACT)
                    filter {
                        eq("user_id", userId)
                        eq("completed", true)
                    }
                }
            response.countOrNull()?.toInt() ?: 0
        } catch (e: Exception) {
            Log.e(tag, "Error getting total logs count: ${e.message}")
            0
        }
    }

    override fun refresh() {
        Log.d(tag, "Refreshing habits manually")
        refreshTrigger.tryEmit(Unit)
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
            Log.e(tag, "Error saving habits/logs: ${e.message}")
        }
    }

    override suspend fun toggleHabitLog(habitId: String, date: String, completed: Boolean) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            Log.d(tag, "Upserting log for habit $habitId on $date to $completed")
            client.postgrest["habit_logs"].update({
                HabitLogDto::completed setTo completed
            }) {
                filter {
                    eq("habit_id", habitId)
                    eq("completed_date", date)
                    eq("user_id", userId)
                }
            }
            Log.d(tag, "Log toggled successfully via upsert")
        } catch (e: Exception) {
            Log.e(tag, "Error toggling habit log: ${e.message}")
        }
    }

    override suspend fun addHabit(habit: Habit) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            client.postgrest["habits"].insert(habit.toDto(userId))
        } catch (e: Exception) {
            Log.e(tag, "Error adding habit: ${e.message}")
        }
    }

    override suspend fun deleteHabit(id: String) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            // Cambio de enfoque: Soft delete cambiando is_active a FALSE
            client.postgrest["habits"].update({
                HabitDto::isActive setTo false
            }) {
                filter {
                    eq("id", id)
                    eq("user_id", userId)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error soft-deleting habit: ${e.message}")
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
            Log.e(tag, "Error updating habit: ${e.message}")
        }
    }

    override suspend fun updateHabitReminder(habitId: String, enabled: Boolean) {
        val userId = client.auth.currentSessionOrNull()?.user?.id ?: return
        try {
            client.postgrest["habits"].update({
                HabitDto::reminderEnabled setTo enabled
            }) {
                filter {
                    eq("id", habitId)
                    eq("user_id", userId)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error updating habit reminder: ${e.message}")
        }
    }

    override suspend fun getCategories(): List<Category> {
        return try {
            val categoriesDto = client.postgrest["categories"]
                .select()
                .decodeList<CategoryDto>()
            categoriesDto.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(tag, "Error fetching categories: ${e.message}")
            emptyList()
        }
    }
}

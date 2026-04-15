package com.example.mindstreak.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Achievement
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.repository.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Equivalente al AppContextType de React
data class AppUiState(
    val habits: List<Habit> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val currentStreak: Int = 0,
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val completionPercent: Int = 0,
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitRepository(application)

    // StateFlow privado mutable — equivalente al useState interno
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    private val _achievements = MutableStateFlow<List<Achievement>>(MockData.ACHIEVEMENTS)

    // UiState público derivado — equivalente al value del Context
    val uiState: StateFlow<AppUiState> = combine(_habits, _achievements) { habits, achievements ->
        deriveUiState(habits, achievements)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppUiState(),
    )

    init {
        viewModelScope.launch {
            // Carga inicial desde DataStore (equivalente al useState lazy initializer)
            val stored = repository.habitsFlow.firstOrNull()

            _habits.value = if (!stored.isNullOrEmpty()) {
                reconcileHabitsOnLaunch(stored)
            } else {
                MockData.HABITS  // Fallback igual que en React
            }

            // Observa cambios en DataStore (por si hay múltiples fuentes)
            repository.habitsFlow.collect { stored ->
                // Solo actualiza si hay datos guardados y difieren del estado actual
                if (stored.isNotEmpty() && stored != _habits.value) {
                    _habits.value = stored
                }
            }
        }

        // Persiste cada cambio en DataStore — equivalente al useEffect([habits])
        viewModelScope.launch {
            _habits
                .drop(1) // Ignora el valor inicial vacío
                .collect { habits ->
                    repository.saveHabits(habits)
                }
        }
    }

    // ─── toggleHabit ────────────────────────────────────────
    // Lógica exacta del toggleHabit de React
    fun toggleHabit(id: String) {
        val today = todayString()
        _habits.update { habits ->
            habits.map { h ->
                if (h.id != id) return@map h

                if (!h.completedToday) {
                    // Toggling ON
                    val alreadyCountedToday = h.lastCompletedDate == today
                    val newLog = h.completionLog + (today to true)
                    h.copy(
                        completedToday = true,
                        lastCompletedDate = today,
                        streak = if (alreadyCountedToday) h.streak else h.streak + 1,
                        completionLog = newLog,
                        weekHistory = deriveWeekHistory(newLog),
                    )
                } else {
                    // Toggling OFF
                    val countedToday = h.lastCompletedDate == today
                    val newLog = h.completionLog + (today to false)
                    h.copy(
                        completedToday = false,
                        streak = if (countedToday) maxOf(0, h.streak - 1) else h.streak,
                        completionLog = newLog,
                        weekHistory = deriveWeekHistory(newLog),
                    )
                }
            }
        }
    }

    // ─── addHabit / deleteHabit ──────────────────────────────
    fun addHabit(habit: Habit) {
        _habits.update { it + habit }
    }

    fun deleteHabit(id: String) {
        _habits.update { habits -> habits.filter { it.id != id } }
    }

    // ─── Helpers de fecha ───────────────────────────────────
    // Equivalente a dateString() / todayString() / yesterdayString()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private fun todayString(): String = LocalDate.now().format(formatter)
    private fun yesterdayString(): String = LocalDate.now().minusDays(1).format(formatter)

    // Equivalente a deriveWeekHistory() — Mon=0, Sun=6
    private fun deriveWeekHistory(log: Map<String, Boolean>): List<Boolean> {
        val today = LocalDate.now()
        // dayOfWeek: MONDAY=1, SUNDAY=7 → offset desde lunes
        val mondayOffset = today.dayOfWeek.value - 1  // 0=lunes, 6=domingo
        return (0..6).map { i ->
            val date = today.minusDays(mondayOffset.toLong()).plusDays(i.toLong())
            log[date.format(formatter)] == true
        }
    }

    // Reconcilia hábitos al abrir la app — equivalente al useState lazy de React
    private fun reconcileHabitsOnLaunch(habits: List<Habit>): List<Habit> {
        val today = todayString()
        val yesterday = yesterdayString()
        return habits.map { h ->
            val lcd = h.lastCompletedDate
            when {
                lcd == today     -> h                          // Ya manejado hoy
                lcd == yesterday -> if (h.completedToday)      // Ayer — streak vivo
                    h.copy(completedToday = false)
                else h
                lcd != null      -> h.copy(                    // 2+ días — streak roto
                    completedToday = false,
                    streak = 0,
                )
                else             -> if (h.completedToday)      // Sin historial previo
                    h.copy(completedToday = false)
                else h
            }
        }
    }

    // Derivar el UiState a partir de la lista de hábitos
    // Equivalente a las variables computadas al final del AppProvider
    private fun deriveUiState(habits: List<Habit>, achievements: List<Achievement>): AppUiState {
        val completedToday = habits.count { it.completedToday }
        val total = habits.size
        return AppUiState(
            habits = habits,
            achievements = achievements,
            currentStreak = habits.maxOfOrNull { it.streak } ?: 0,
            completedToday = completedToday,
            totalHabits = total,
            completionPercent = if (total == 0) 0 else (completedToday * 100) / total,
        )
    }
}
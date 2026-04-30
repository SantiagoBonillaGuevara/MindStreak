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
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    private val _achievements = MutableStateFlow(MockData.ACHIEVEMENTS)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val uiState: StateFlow<AppUiState> = combine(_habits, _achievements) { habits, achievements ->
        deriveUiState(habits, achievements)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppUiState(),
    )

    init {
        loadAndReconcileHabits()
        observeAndSaveHabits()
    }

    private fun loadAndReconcileHabits() {
        viewModelScope.launch {
            val stored = repository.habitsFlow.firstOrNull()
            _habits.value = if (!stored.isNullOrEmpty()) {
                reconcileHabitsOnLaunch(stored)
            } else {
                MockData.HABITS
            }

            repository.habitsFlow.collect { stored ->
                if (stored.isNotEmpty() && stored != _habits.value) {
                    _habits.value = stored
                }
            }
        }
    }

    private fun observeAndSaveHabits() {
        viewModelScope.launch {
            _habits.drop(1).collect { habits ->
                repository.saveHabits(habits)
            }
        }
    }

    fun toggleHabit(id: String) {
        val today = todayString()
        _habits.update { habits ->
            habits.map { h ->
                if (h.id != id) return@map h
                if (!h.completedToday) {
                    toggleOn(h, today)
                } else {
                    toggleOff(h, today)
                }
            }
        }
    }

    private fun toggleOn(h: Habit, today: String): Habit {
        val alreadyCountedToday = h.lastCompletedDate == today
        val newLog = h.completionLog + (today to true)
        return h.copy(
            completedToday = true,
            lastCompletedDate = today,
            streak = if (alreadyCountedToday) h.streak else h.streak + 1,
            completionLog = newLog,
            weekHistory = deriveWeekHistory(newLog),
        )
    }

    private fun toggleOff(h: Habit, today: String): Habit {
        val countedToday = h.lastCompletedDate == today
        val newLog = h.completionLog + (today to false)
        return h.copy(
            completedToday = false,
            streak = if (countedToday) maxOf(0, h.streak - 1) else h.streak,
            completionLog = newLog,
            weekHistory = deriveWeekHistory(newLog),
        )
    }

    fun addHabit(habit: Habit) {
        _habits.update { it + habit }
    }

    fun deleteHabit(id: String) {
        _habits.update { habits -> habits.filter { it.id != id } }
    }

    private fun todayString(): String = LocalDate.now().format(formatter)
    private fun yesterdayString(): String = LocalDate.now().minusDays(1).format(formatter)

    private fun deriveWeekHistory(log: Map<String, Boolean>): List<Boolean> {
        val today = LocalDate.now()
        val mondayOffset = today.dayOfWeek.value - 1
        return (0..6).map { i ->
            val date = today.minusDays(mondayOffset.toLong()).plusDays(i.toLong())
            log[date.format(formatter)] == true
        }
    }

    private fun reconcileHabitsOnLaunch(habits: List<Habit>): List<Habit> {
        val today = todayString()
        val yesterday = yesterdayString()
        return habits.map { h ->
            val lcd = h.lastCompletedDate
            when {
                lcd == today -> h
                lcd == yesterday -> h.copy(completedToday = false)
                lcd != null -> h.copy(completedToday = false, streak = 0)
                else -> h.copy(completedToday = false)
            }
        }
    }

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

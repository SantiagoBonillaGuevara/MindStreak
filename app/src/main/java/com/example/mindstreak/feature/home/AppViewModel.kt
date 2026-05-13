package com.example.mindstreak.feature.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Achievement
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.example.mindstreak.data.model.User

data class AppUiState(
    val user: User? = null,
    val habits: List<Habit> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val currentStreak: Int = 0,
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val completionPercent: Int = 0,
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AppViewModel"
    private val repository = RepositoryProvider.getHabitRepository(application)
    private val userRepository = RepositoryProvider.getUserRepository(application)
    
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    private val _achievements = MutableStateFlow(MockData.ACHIEVEMENTS)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val uiState: StateFlow<AppUiState> = combine(
        userRepository.userFlow,
        _habits,
        _achievements
    ) { user, habits, achievements ->
        Log.d(TAG, "Combining UI State - User: ${user?.name}, Habits: ${habits.size}")
        deriveUiState(user, habits, achievements)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly, // Cambiado a Eagerly para asegurar emisión inmediata
        initialValue = AppUiState(),
    )

    init {
        Log.d(TAG, "Initializing AppViewModel")
        loadUserAndHabits()
        observeAndSaveHabits()
    }

    private fun loadUserAndHabits() {
        // Observar usuario
        viewModelScope.launch {
            userRepository.userFlow.collect { user ->
                Log.d(TAG, "User flow emitted: ${user?.name}")
            }
        }

        // Observar hábitos
        viewModelScope.launch {
            Log.d(TAG, "Starting habits collection")
            repository.habitsFlow.collect { stored ->
                Log.d(TAG, "Habits flow emitted: ${stored.size} items")
                if (stored.isNotEmpty()) {
                    _habits.value = reconcileHabitsOnLaunch(stored)
                } else if (_habits.value.isEmpty()) {
                    Log.d(TAG, "Habits empty, using MockData as fallback")
                    _habits.value = MockData.HABITS
                }
            }
        }
    }

    private fun observeAndSaveHabits() {
        viewModelScope.launch {
            _habits.drop(1).collect { habits ->
                if (habits.isNotEmpty() && habits != MockData.HABITS) {
                    Log.d(TAG, "Saving ${habits.size} habits to repository")
                    repository.saveHabits(habits)
                }
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

    private fun deriveUiState(user: User?, habits: List<Habit>, achievements: List<Achievement>): AppUiState {
        val completedToday = habits.count { it.completedToday }
        val total = habits.size
        return AppUiState(
            user = user,
            habits = habits,
            achievements = achievements,
            currentStreak = habits.maxOfOrNull { it.streak } ?: 0,
            completedToday = completedToday,
            totalHabits = total,
            completionPercent = if (total == 0) 0 else (completedToday * 100) / total,
        )
    }
}

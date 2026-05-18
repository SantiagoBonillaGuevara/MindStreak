package com.example.mindstreak.feature.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Achievement
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.data.model.Category
import com.example.mindstreak.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.example.mindstreak.data.model.User

data class AppUiState(
    val user: User? = null,
    val habits: List<Habit> = emptyList(),
    val categories: List<Category> = emptyList(),
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
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    private val _achievements = MutableStateFlow(MockData.ACHIEVEMENTS)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val uiState: StateFlow<AppUiState> = combine(
        userRepository.userFlow,
        _habits,
        _categories,
        _achievements
    ) { user, habits, categories, achievements ->
        Log.d(TAG, "Combining UI State - User: ${user?.name}, Habits: ${habits.size}, Categories: ${categories.size}")
        deriveUiState(user, habits, categories, achievements)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly, // Cambiado a Eagerly para asegurar emisión inmediata
        initialValue = AppUiState(),
    )

    init {
        Log.d(TAG, "Initializing AppViewModel")
        loadData()
    }

    private fun loadData() {
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
                _habits.value = reconcileHabitsOnLaunch(stored)
            }
        }

        // Cargar categorías
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun refreshData() {
        Log.d(TAG, "Manual data refresh triggered")
        userRepository.refresh()
        repository.refresh()
    }

    fun toggleHabit(id: String) {
        val today = todayString()
        _habits.update { habits ->
            habits.map { h ->
                if (h.id == id && !h.completedToday) toggleOn(h, today)
                else h
            }
        }
        // Persistir cambios inmediatamente
        viewModelScope.launch {
            repository.saveHabits(_habits.value)
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

    fun addHabit(habit: Habit) {
        _habits.update { it + habit }
        viewModelScope.launch {
            repository.addHabit(habit)
        }
    }

    fun deleteHabit(id: String) {
        _habits.update { habits -> habits.filter { it.id != id } }
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
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

    private fun deriveUiState(user: User?, habits: List<Habit>, categories: List<Category>, achievements: List<Achievement>): AppUiState {
        val completedToday = habits.count { it.completedToday }
        val total = habits.size
        return AppUiState(
            user = user,
            habits = habits,
            categories = categories,
            achievements = achievements,
            currentStreak = habits.maxOfOrNull { it.streak } ?: 0,
            completedToday = completedToday,
            totalHabits = total,
            completionPercent = if (total == 0) 0 else (completedToday * 100) / total,
        )
    }
    }


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

import com.example.mindstreak.data.local.SettingsManager
import com.example.mindstreak.data.model.User

data class AppUiState(
    val user: User? = null,
    val habits: List<Habit> = emptyList(),
    val allHabits: List<Habit> = emptyList(),
    val categories: List<Category> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val completedToday: Int = 0,
    val totalHabits: Int = 0,
    val completionPercent: Int = 0,
    val totalLogs: Int = 0,
    val monthlyRate: Int = 0,
    val activeDaysInMonth: Set<String> = emptySet(),
    val isDarkMode: Boolean? = null,
    val notificationsEnabled: Boolean = false
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AppViewModel"
    private val repository = RepositoryProvider.getHabitRepository(application)
    private val userRepository = RepositoryProvider.getUserRepository(application)
    private val settingsManager = SettingsManager(application)

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    private val _allHabits = MutableStateFlow<List<Habit>>(emptyList())
    private val _totalLogs = MutableStateFlow(0)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    private val _achievements = MutableStateFlow(MockData.ACHIEVEMENTS)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val uiState: StateFlow<AppUiState> = combine(
        userRepository.userFlow,
        _habits,
        _allHabits,
        _totalLogs,
        _categories,
        _achievements,
        settingsManager.darkModeFlow,
        settingsManager.notificationsEnabledFlow
    ) { array ->
        val user = array[0] as? User
        val habits = array[1] as List<Habit>
        val allHabits = array[2] as List<Habit>
        val totalLogs = array[3] as Int
        val categories = array[4] as List<Category>
        val achievements = array[5] as List<Achievement>
        val isDarkMode = array[6] as? Boolean
        val notificationsEnabled = array[7] as Boolean
        deriveUiState(user, habits, allHabits, totalLogs, categories, achievements, isDarkMode, notificationsEnabled)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
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

        // Observar hábitos de hoy
        viewModelScope.launch {
            repository.habitsFlow.collect { stored ->
                _habits.value = stored
            }
        }

        // Observar todos los hábitos del mes
        viewModelScope.launch {
            repository.habitsAllFlow.collect { stored ->
                _allHabits.value = stored
            }
        }

        // Cargar total de logs
        viewModelScope.launch {
            _totalLogs.value = repository.getTotalHabitLogsCount()
        }

        // Cargar categorías
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun refreshData() {
        Log.d(TAG, "data refresh triggered")
        userRepository.refresh()
        repository.refresh()
        viewModelScope.launch {
            _totalLogs.value = repository.getTotalHabitLogsCount()
        }
    }

    fun toggleHabit(id: String) {
        val today = todayString()
        val habit = _habits.value.find { it.id == id } ?: return
        
        if (!habit.completedToday) {
            viewModelScope.launch {
                repository.toggleHabitLog(id, today, true)
                refreshData()
            }
        }
    }

    fun addHabit(habit: Habit) {
        _habits.update { it + habit }
        viewModelScope.launch {
            repository.addHabit(habit)
            refreshData()
        }
    }

    fun deleteHabit(id: String) {
        _habits.update { habits -> habits.filter { it.id != id } }
        viewModelScope.launch {
            repository.deleteHabit(id)
            refreshData()
        }
    }

    fun toggleHabitReminder(id: String, enabled: Boolean) {
        viewModelScope.launch {
            repository.updateHabitReminder(id, enabled)
            refreshData()
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkMode(enabled)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setNotificationsEnabled(enabled)
        }
    }

    private fun todayString(): String = LocalDate.now().format(formatter)

    private fun deriveUiState(
        user: User?, 
        habits: List<Habit>, 
        allHabits: List<Habit>,
        totalLogs: Int,
        categories: List<Category>, 
        achievements: List<Achievement>,
        isDarkMode: Boolean?,
        notificationsEnabled: Boolean
    ): AppUiState {
        val completedToday = habits.count { it.completedToday }
        val total = habits.size
        
        // Calcular tasa mensual
        val today = LocalDate.now()
        val daysInMonth = today.dayOfMonth
        val totalExpected = allHabits.size * daysInMonth
        val totalCompletedThisMonth = allHabits.sumOf { h -> 
            h.completionLog.values.count { it } 
        }
        val monthlyRate = if (totalExpected == 0) 0 else (totalCompletedThisMonth * 100) / totalExpected
        
        // Calcular días activos (algún hábito completado)
        val activeDays = allHabits.flatMap { h -> 
            h.completionLog.filter { it.value }.keys 
        }.toSet()

        return AppUiState(
            user = user,
            habits = habits,
            allHabits = allHabits,
            categories = categories,
            achievements = achievements,
            currentStreak = user?.totalStreak ?: 0,
            bestStreak = user?.bestStreak ?: 0,
            completedToday = completedToday,
            totalHabits = total,
            completionPercent = if (total == 0) 0 else (completedToday * 100) / total,
            totalLogs = totalLogs,
            monthlyRate = monthlyRate,
            activeDaysInMonth = activeDays,
            isDarkMode = isDarkMode,
            notificationsEnabled = notificationsEnabled
        )
    }
}


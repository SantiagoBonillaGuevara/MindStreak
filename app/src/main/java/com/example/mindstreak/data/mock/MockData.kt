package com.example.mindstreak.data.mock

import com.example.mindstreak.data.model.*

object MockData {

    val USER = User(
        name = "Alex Chen",
        username = "@alexchen",
        university = "MIT — Computer Science, Year 3",
        avatarEmoji = "🧑‍💻",
        level = 12,
        xp = 2840,
        nextLevelXp = 3000,
        totalStreak = 21,
        bestStreak = 34,
        totalHabitsCompleted = 342,
        joinDate = "Sep 2024",
    )

    val HABITS = listOf(
        Habit(
            id = "1", name = "Morning Run", emoji = "🏃",
            category = "fitness", color = "#FF6B35",
            streak = 21, completedToday = true,
            frequency = "daily", completionRate = 0.87f,
            reminderTime = "07:00",
            weekHistory = listOf(true, true, true, false, true, true, true),
        ),
        Habit(
            id = "2", name = "Meditate", emoji = "🧘",
            category = "mindfulness", color = "#7C6EFF",
            streak = 14, completedToday = true,
            frequency = "daily", completionRate = 0.73f,
            reminderTime = "07:30",
            weekHistory = listOf(true, false, true, true, true, true, true),
        ),
        Habit(
            id = "3", name = "Drink Water", emoji = "💧",
            category = "health", color = "#4ECDC4",
            streak = 28, completedToday = true,
            frequency = "daily", completionRate = 0.92f,
            reminderTime = "08:00",
            weekHistory = listOf(true, true, true, true, true, true, true),
        ),
        Habit(
            id = "4", name = "Read 30 min", emoji = "📚",
            category = "learning", color = "#FFD166",
            streak = 7, completedToday = true,
            frequency = "daily", completionRate = 0.65f,
            reminderTime = "21:00",
            weekHistory = listOf(true, false, false, true, true, true, true),
        ),
        Habit(
            id = "5", name = "Sleep 8 Hours", emoji = "😴",
            category = "health", color = "#6ECFF6",
            streak = 5, completedToday = false,
            frequency = "daily", completionRate = 0.58f,
            reminderTime = "22:30",
            weekHistory = listOf(false, true, true, false, true, true, false),
        ),
        Habit(
            id = "6", name = "Study Session", emoji = "📖",
            category = "academic", color = "#FF8FAB",
            streak = 12, completedToday = false,
            frequency = "daily", completionRate = 0.71f,
            reminderTime = "14:00",
            weekHistory = listOf(true, true, false, true, true, false, true),
        ),
    )

    val FRIENDS = listOf(
        Friend("1", "Emma Wilson",  "@emma",    "👩‍🎨", streak = 23, level = 15, points = 3240, weeklyHabits = 41),
        Friend("2", "Alex Chen",    "@you",     "🧑‍💻", streak = 21, level = 12, points = 2840, weeklyHabits = 38, isYou = true),
        Friend("3", "Sarah Kim",    "@sarahk",  "👩‍💻", streak = 18, level = 11, points = 2610, weeklyHabits = 35),
        Friend("4", "Mike Torres",  "@miket",   "👨‍🎓", streak = 15, level = 10, points = 2200, weeklyHabits = 30),
        Friend("5", "James Lee",    "@jamesl",  "👨‍💻", streak = 12, level =  9, points = 1980, weeklyHabits = 27),
        Friend("6", "Olivia Park",  "@oliviap", "👩‍🔬", streak =  9, level =  8, points = 1720, weeklyHabits = 22),
    )

    val ACHIEVEMENTS = listOf(
        Achievement("1",  "First Flame",      "🔥", "Reach a 7-day streak",           earned = true,  earnedDate = "Oct 2024", rarity = Rarity.COMMON),
        Achievement("2",  "Iron Will",        "💪", "Reach a 14-day streak",          earned = true,  earnedDate = "Nov 2024", rarity = Rarity.RARE),
        Achievement("3",  "Habit Hero",       "🦸", "Log all habits for 30 days",     earned = false, progress = 21, total = 30,  rarity = Rarity.EPIC),
        Achievement("4",  "Early Bird",       "🌅", "Log habit before 7am, 5 days",   earned = true,  earnedDate = "Dec 2024", rarity = Rarity.COMMON),
        Achievement("5",  "Mindful Master",   "🧠", "Meditate 21 days in a row",      earned = false, progress = 14, total = 21,  rarity = Rarity.RARE),
        Achievement("6",  "Sleep Champion",   "💤", "Log 8h sleep for 10 days",       earned = false, progress = 5,  total = 10,  rarity = Rarity.COMMON),
        Achievement("7",  "Social Butterfly", "🦋", "Connect with 5 friends",         earned = true,  earnedDate = "Jan 2025", rarity = Rarity.COMMON),
        Achievement("8",  "Triple Threat",    "⚡", "Track 3 habits for 30 days",     earned = false, progress = 21, total = 30,  rarity = Rarity.EPIC),
        Achievement("9",  "Century Club",     "💯", "Complete 100 total habits",      earned = true,  earnedDate = "Feb 2025", rarity = Rarity.RARE),
        Achievement("10", "Legend",           "👑", "Reach a 100-day streak",         earned = false, progress = 21, total = 100, rarity = Rarity.LEGENDARY),
    )

    val WEEK_DATA = listOf(
        WeekDay("M", 5, 6), WeekDay("T", 6, 6), WeekDay("W", 4, 6),
        WeekDay("T", 6, 6), WeekDay("F", 5, 6), WeekDay("S", 3, 6),
        WeekDay("S", 4, 6),
    )

    // En Kotlin generamos el MONTH_DATA con la misma lógica aleatoria
    // Pero en producción esto vendrá de la base de datos
    val MONTH_DATA: List<MonthDay> = (1..30).map { day ->
        MonthDay(
            day = day,
            completed = Math.random() > 0.25,
            partial = Math.random() > 0.6,
        )
    }

    val MOTIVATIONAL_QUOTES = listOf(
        "You're on fire! 🔥 Keep the chain alive.",
        "21 days strong! You're unstoppable 💪",
        "Consistency beats perfection every time.",
        "Every check-in is a vote for who you want to be.",
        "2 more habits to complete today — you've got this!",
        "Your future self will thank you for today.",
    )

    val CATEGORIES = listOf(
        Category("fitness",     "Fitness",     "🏋️", "#FF6B35"),
        Category("mindfulness", "Mindfulness", "🧘", "#7C6EFF"),
        Category("health",      "Health",      "💊", "#4ECDC4"),
        Category("learning",    "Learning",    "📚", "#FFD166"),
        Category("sleep",       "Sleep",       "😴", "#6ECFF6"),
        Category("academic",    "Academic",    "📖", "#FF8FAB"),
        Category("social",      "Social",      "👥", "#A8E063"),
        Category("nutrition",   "Nutrition",   "🥗", "#56AB2F"),
    )
}
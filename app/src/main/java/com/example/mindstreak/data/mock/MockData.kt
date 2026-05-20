package com.example.mindstreak.data.mock

import com.example.mindstreak.data.model.*

object MockData {

    val USER = User(
        id= "1",
        name = "Alex Chen",
        username = "@alexchen",
        university = "MIT — Computer Science, Year 3",
        avatarEmoji = "🧑‍💻",
        levelId = 12,
        levelTitle = "Habit Architect",
        xp = 2840,
        totalStreak = 21,
        bestStreak = 34,
        totalHabitsCompleted = 342,
        joinDate = "Sep 2024",
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
package com.example.mindstreak.data.repository

import android.content.Context

object RepositoryProvider {

    // Cambia esto a true cuando quieras usar Supabase
    private const val USE_SUPABASE = true

    private var habitRepository: HabitRepository? = null
    private var userRepository: UserRepository? = null
    private var quoteRepository: MotivationalQuoteRepository? = null

    fun getHabitRepository(context: Context): HabitRepository {
        return habitRepository ?: synchronized(this) {
            val instance =
                if (USE_SUPABASE) SupabaseHabitRepository()
                else LocalHabitRepository(context.applicationContext)

            habitRepository = instance
            instance
        }
    }

    fun getUserRepository(): UserRepository {
        return userRepository ?: synchronized(this) {
            val instance = if (USE_SUPABASE) {
                SupabaseUserRepository()
            } else {
                MockUserRepository()
            }
            userRepository = instance
            instance
        }
    }

    fun getQuoteRepository(): MotivationalQuoteRepository {
        return quoteRepository ?: synchronized(this) {
            val instance = SupabaseMotivationalQuoteRepository()
            quoteRepository = instance
            instance
        }
    }
}

package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.MotivationalQuote

interface MotivationalQuoteRepository {
    suspend fun getRandomQuote(): MotivationalQuote?
}

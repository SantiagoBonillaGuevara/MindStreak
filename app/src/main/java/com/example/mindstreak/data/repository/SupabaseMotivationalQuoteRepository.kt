package com.example.mindstreak.data.repository

import android.util.Log
import com.example.mindstreak.data.model.MotivationalQuote
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.remote.dto.MotivationalQuoteDto
import com.example.mindstreak.data.remote.dto.toDomain
import io.github.jan.supabase.postgrest.postgrest

class SupabaseMotivationalQuoteRepository : MotivationalQuoteRepository {
    private val client by lazy { SupabaseClientProvider.client }
    private val TAG = "SupabaseQuoteRepo"

    override suspend fun getRandomQuote(): MotivationalQuote? {
        return try {
            val quotesDto = client.postgrest["motivational_quotes"]
                .select {
                    filter {
                        eq("is_active", true)
                    }
                }
                .decodeList<MotivationalQuoteDto>()
            
            if (quotesDto.isNotEmpty()) {
                quotesDto.random().toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching random quote: ${e.message}")
            null
        }
    }
}

package com.example.mindstreak.data.local

import android.content.Context
import androidx.datastore.dataStore

// Crea el archivo "habits_v1.json" en el storage del dispositivo
// Equivalente a la key 'mindstreak_habits_v1' en localStorage
val Context.habitsDataStore by dataStore(
    fileName = "habits_v1.json",
    serializer = HabitsSerializer
)
package com.example.mindstreak.data.local

import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import com.example.mindstreak.data.model.Habit

// Wrapper serializable para guardar la lista
@Serializable
data class HabitsStore(
    val habits: List<SerializableHabit> = emptyList()
)

// Versión serializable de Habit (DataStore solo maneja tipos primitivos/serializables)
@Serializable
data class SerializableHabit(
    val id: String,
    val name: String,
    val emoji: String,
    val category: String,
    val color: String,
    val streak: Int,
    val completedToday: Boolean,
    val lastCompletedDate: String? = null,
    val frequency: String,
    val completionRate: Float,
    val reminderTime: String,
    val weekHistory: List<Boolean>,
    val completionLog: Map<String, Boolean> = emptyMap(),
)

// Conversiones entre el modelo de dominio y el serializable
fun Habit.toSerializable() = SerializableHabit(
    id, name, emoji, category, color, streak,
    completedToday, lastCompletedDate, frequency,
    completionRate, reminderTime, weekHistory, completionLog
)

fun SerializableHabit.toHabit() = Habit(
    id, name, emoji, category, color, streak,
    completedToday, lastCompletedDate, frequency,
    completionRate, reminderTime, weekHistory, completionLog
)

object HabitsSerializer : Serializer<HabitsStore> {
    override val defaultValue = HabitsStore()
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun readFrom(input: InputStream): HabitsStore =
        try {
            json.decodeFromString(input.readBytes().decodeToString())
        } catch (e: Exception) {
            defaultValue  // Equivalente al catch del localStorage
        }

    override suspend fun writeTo(t: HabitsStore, output: OutputStream) {
        output.write(json.encodeToString(t).encodeToByteArray())
    }
}
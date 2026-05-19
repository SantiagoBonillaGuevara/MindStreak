package com.example.mindstreak.data.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val university: String,
    val avatarEmoji: String,
    val levelId: Int, // Cambiado de level a levelId para producción
    val xp: Int,
    val totalStreak: Int,
    val bestStreak: Int,
    val totalHabitsCompleted: Int,
    val joinDate: String,
) {
    // NUEVO: Métodos de conveniencia calculados dinámicamente en el modelo de dominio para no romper las UIs
    fun getLevelStartXp(): Int {
        val l = levelId
        return 100 * (l - 1) * (l - 1) + 200 * (l - 1)
    }

    fun getNextLevelRequiredXp(): Int {
        val l = levelId
        return 100 * l * l + 200 * l
    }

    // Devuelve cuánta XP tiene el usuario dentro de su nivel actual (Ej: 250 XP ganados en el nivel 3)
    val xpInCurrentLevel: Int
        get() = xp - getLevelStartXp()

    // Devuelve el total neta requerida SOLO para este nivel (Sustituto de la antigua variable next_level_xp en UI)
    val nextLevelXpNeta: Int
        get() = getNextLevelRequiredXp() - getLevelStartXp()

    // Progreso porcentual ideal para el ProgressRing o sliders de UI (rango 0.0f a 1.0f)
    val levelProgressPercentage: Float
        get() {
            val totalNeeded = nextLevelXpNeta
            if (totalNeeded <= 0) return 1.0f
            return (xpInCurrentLevel.toFloat() / totalNeeded.toFloat()).coerceIn(0.0f, 1.0f)
        }
}
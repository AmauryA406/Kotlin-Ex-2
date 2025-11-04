package com.tumme.scrudstudents.data.local.model

// Enum définissant les niveaux d'études universitaires disponibles.
enum class Level(val value: String, val displayName: String) {
    P1("P1", "Prépa 1"),
    P2("P2", "Prépa 2"),
    P3("P3", "Prépa 3"),
    B1("B1", "Bachelor 1"),
    B2("B2", "Bachelor 2"),
    B3("B3", "Bachelor 3"),
    A1("A1", "Advanced 1"),
    A2("A2", "Advanced 2"),
    A3("A3", "Advanced 3"),
    MS("MS", "Master"),
    PHD("PhD", "Doctorate");

    companion object {
        // Convertit une chaîne en Level (pour TypeConverter Room).
        fun fromString(value: String): Level {
            return entries.find { it.value == value } ?: B1
        }
    }
}
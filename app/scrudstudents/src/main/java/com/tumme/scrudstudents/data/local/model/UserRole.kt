package com.tumme.scrudstudents.data.local.model

// Enum définissant les différents rôles d'utilisateurs dans l'application.
enum class UserRole(val value: String) {
    STUDENT("Student"),    // Rôle étudiant
    TEACHER("Teacher");    // Rôle enseignant

    companion object {
        // Convertit une chaîne en UserRole (utile pour les TypeConverters Room).
        fun fromString(value: String): UserRole {
            return entries.find { it.value == value } ?: STUDENT
        }
    }
}
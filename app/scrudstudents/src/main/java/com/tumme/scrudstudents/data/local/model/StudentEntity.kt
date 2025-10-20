package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Entité Room représentant un étudiant dans la table "students" de la base de données.
@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val idStudent: Int, // Clé primaire unique identifiant l'étudiant.
    val lastName: String, // Nom de famille de l'étudiant.
    val firstName: String, // Prénom de l'étudiant.
    val dateOfBirth: Date, // Date de naissance (nécessite un TypeConverter pour Room).
    val gender: Gender // Genre de l'étudiant (enum).
)
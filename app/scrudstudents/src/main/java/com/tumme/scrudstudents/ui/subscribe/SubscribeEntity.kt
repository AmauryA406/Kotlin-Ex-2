package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.ColumnInfo

// Entité Room représentant une inscription (lien entre Student et Course) dans la table "subscribes".
@Entity(
    tableName = "subscribes",
    primaryKeys = ["studentId", "courseId"], // Clé primaire composite (plusieurs colonnes).
    foreignKeys = [
        // Clé étrangère vers Student : si l'étudiant est supprimé, ses inscriptions sont supprimées en cascade.
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["idStudent"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        // Clé étrangère vers Course : si le cours est supprimé, ses inscriptions sont supprimées en cascade.
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["idCourse"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("courseId")] // Index pour optimiser les requêtes sur ces colonnes.
)
data class SubscribeEntity(
    val studentId: Int, // ID de l'étudiant (clé étrangère).
    val courseId: Int, // ID du cours (clé étrangère).
    val score: Float // Note obtenue par l'étudiant dans ce cours.
)
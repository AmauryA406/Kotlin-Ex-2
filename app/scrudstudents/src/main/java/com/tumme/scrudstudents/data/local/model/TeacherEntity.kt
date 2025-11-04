package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true) val idTeacher: Int = 0,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val department: String = ""
)
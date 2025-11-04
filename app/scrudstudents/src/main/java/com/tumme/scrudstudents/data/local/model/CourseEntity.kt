package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = TeacherEntity::class,
            parentColumns = ["idTeacher"],
            childColumns = ["teacherId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("teacherId")]
)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val idCourse: Int = 0,
    val nameCourse: String,
    val ectsCourse: Float,
    val levelCourse: LevelCourse,
    val teacherId: Int? = null
)
package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherDao {

    @Query("SELECT * FROM teachers ORDER BY lastName, firstName")
    fun getAllTeachers(): Flow<List<TeacherEntity>>

    @Query("SELECT * FROM teachers WHERE idTeacher = :id")
    suspend fun getTeacherById(id: Int): TeacherEntity?

    @Query("SELECT * FROM teachers WHERE email = :email")
    suspend fun getTeacherByEmail(email: String): TeacherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacher: TeacherEntity): Long

    @Update
    suspend fun update(teacher: TeacherEntity)

    @Delete
    suspend fun delete(teacher: TeacherEntity)
}
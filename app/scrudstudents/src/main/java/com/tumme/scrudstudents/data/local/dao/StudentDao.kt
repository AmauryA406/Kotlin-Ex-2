package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE idStudent = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE email = :email")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long

    @Delete
    suspend fun delete(student: StudentEntity)
}
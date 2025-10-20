package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) définissant les opérations de base de données pour les étudiants.
@Dao
interface StudentDao {

    // Récupère tous les étudiants sous forme de Flow réactif, triés par nom puis prénom.
    @Query("SELECT * FROM students ORDER BY lastName, firstName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    // Insère ou remplace un étudiant de manière asynchrone (fonction suspend).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    // Supprime un étudiant de la base de données.
    @Delete
    suspend fun delete(student: StudentEntity)

    // Récupère un étudiant spécifique par son ID, retourne null si non trouvé.
    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1")
    suspend fun getStudentById(id: Int): StudentEntity?
}
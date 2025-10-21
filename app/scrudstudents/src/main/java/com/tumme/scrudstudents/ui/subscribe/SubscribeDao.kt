package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) définissant les opérations de base de données pour les inscriptions.
@Dao
interface SubscribeDao {

    // Récupère toutes les inscriptions sous forme de Flow réactif.
    @Query("SELECT * FROM subscribes")
    fun getAllSubscribes(): Flow<List<SubscribeEntity>>

    // Récupère les inscriptions d'un étudiant spécifique.
    @Query("SELECT * FROM subscribes WHERE studentId = :studentId")
    fun getSubscribesByStudent(studentId: Int): Flow<List<SubscribeEntity>>

    // Récupère les inscriptions pour un cours spécifique.
    @Query("SELECT * FROM subscribes WHERE courseId = :courseId")
    fun getSubscribesByCourse(courseId: Int): Flow<List<SubscribeEntity>>

    // Insère ou remplace une inscription de manière asynchrone (fonction suspend).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscribe: SubscribeEntity)

    // Supprime une inscription de la base de données.
    @Delete
    suspend fun delete(subscribe: SubscribeEntity)
}
package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import kotlinx.coroutines.flow.Flow

// Repository centralisant l'accès aux données pour les entités Student, Course et Subscribe.
class SCRUDRepository(
    private val studentDao: StudentDao, // DAO pour l'accès aux étudiants.
    private val courseDao: CourseDao, // DAO pour l'accès aux cours.
    private val subscribeDao: SubscribeDao // DAO pour l'accès aux inscriptions.
) {
    // ========== Students ==========

    // Retourne un Flow de tous les étudiants pour observation réactive.
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()

    // Insère un étudiant dans la base de données.
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)

    // Supprime un étudiant de la base de données.
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)

    // Récupère un étudiant par son ID.
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    // ========== Courses ==========

    // Retourne un Flow de tous les cours.
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()

    // Insère un cours dans la base de données.
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)

    // Supprime un cours de la base de données.
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)

    // Récupère un cours par son ID.
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    // ========== Subscribes ==========

    // Retourne un Flow de toutes les inscriptions.
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()

    // Retourne un Flow des inscriptions d'un étudiant spécifique.
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)

    // Retourne un Flow des inscriptions pour un cours spécifique.
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)

    // Insère une inscription dans la base de données.
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)

    // Supprime une inscription de la base de données.
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)
}
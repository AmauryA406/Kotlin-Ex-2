package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.*
import com.tumme.scrudstudents.data.local.model.*
import kotlinx.coroutines.flow.Flow

class SCRUDRepository(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao
) {
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()

    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)

    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)

    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()

    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)

    suspend fun updateTeacher(teacher: TeacherEntity) = teacherDao.update(teacher)

    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)

    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)

    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()

    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)

    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)

    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()

    fun getAllSubscribesWithDetails(): Flow<List<SubscribeWithDetails>> = subscribeDao.getAllSubscribesWithDetails()

    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)

    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)

    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)

    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)
}
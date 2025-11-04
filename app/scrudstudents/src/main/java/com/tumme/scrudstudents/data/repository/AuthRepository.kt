package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.UserRole

class AuthRepository(
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao
) {

    data class AuthResult(
        val success: Boolean,
        val role: UserRole? = null,
        val userId: Int? = null,
        val message: String = ""
    )

    suspend fun loginStudent(email: String, password: String): AuthResult {
        val student = studentDao.getStudentByEmail(email)

        return if (student != null && student.password == password) {
            AuthResult(
                success = true,
                role = UserRole.STUDENT,
                userId = student.idStudent,
                message = "Welcome ${student.firstName}!"
            )
        } else {
            AuthResult(success = false, message = "Invalid email or password")
        }
    }

    suspend fun loginTeacher(email: String, password: String): AuthResult {
        val teacher = teacherDao.getTeacherByEmail(email)

        return if (teacher != null && teacher.password == password) {
            AuthResult(
                success = true,
                role = UserRole.TEACHER,
                userId = teacher.idTeacher,
                message = "Welcome ${teacher.firstName}!"
            )
        } else {
            AuthResult(success = false, message = "Invalid email or password")
        }
    }

    suspend fun registerStudent(student: StudentEntity): AuthResult {
        return try {
            val existing = studentDao.getStudentByEmail(student.email)
            if (existing != null) {
                return AuthResult(success = false, message = "Email already exists")
            }

            val id = studentDao.insert(student)
            AuthResult(
                success = true,
                role = UserRole.STUDENT,
                userId = id.toInt(),
                message = "Registration successful!"
            )
        } catch (e: Exception) {
            AuthResult(success = false, message = "Error: ${e.message}")
        }
    }

    suspend fun registerTeacher(teacher: TeacherEntity): AuthResult {
        return try {
            val existing = teacherDao.getTeacherByEmail(teacher.email)
            if (existing != null) {
                return AuthResult(success = false, message = "Email already exists")
            }

            val id = teacherDao.insert(teacher)
            AuthResult(
                success = true,
                role = UserRole.TEACHER,
                userId = id.toInt(),
                message = "Registration successful!"
            )
        } catch (e: Exception) {
            AuthResult(success = false, message = "Error: ${e.message}")
        }
    }
}
package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tumme.scrudstudents.data.local.model.Level
import com.tumme.scrudstudents.data.local.model.UserRole
import com.tumme.scrudstudents.ui.auth.LoginScreen
import com.tumme.scrudstudents.ui.auth.RegisterScreen
import com.tumme.scrudstudents.ui.student.*
import com.tumme.scrudstudents.ui.teacher.*
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val STUDENT_HOME = "student_home/{studentId}/{level}"
    const val STUDENT_COURSES = "student_courses/{studentId}/{level}"
    const val STUDENT_MY_SUBSCRIPTIONS = "student_my_subscriptions/{studentId}"
    const val STUDENT_GRADES = "student_grades/{studentId}"
    const val STUDENT_FINAL_GRADE = "student_final_grade/{studentId}/{level}"

    const val TEACHER_HOME = "teacher_home/{teacherId}"
    const val TEACHER_MY_COURSES = "teacher_my_courses/{teacherId}"
    const val TEACHER_DECLARE_COURSES = "teacher_declare_courses/{teacherId}"
    const val TEACHER_STUDENTS = "teacher_students/{teacherId}"
    const val TEACHER_GRADE_ENTRY = "teacher_grade_entry/{teacherId}"

    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    const val COURSE_LIST = "course_list"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"

    const val SUBSCRIBE_LIST = "subscribe_list"
    const val SUBSCRIBE_FORM = "subscribe_form"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role, userId ->
                    when (role) {
                        UserRole.STUDENT -> navController.navigate("student_home/$userId/B1") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                        UserRole.TEACHER -> navController.navigate("teacher_home/$userId") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { role, userId ->
                    when (role) {
                        UserRole.STUDENT -> navController.navigate("student_home/$userId/B1") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                        UserRole.TEACHER -> navController.navigate("teacher_home/$userId") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            "student_home/{studentId}/{level}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("level") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val levelStr = backStackEntry.arguments?.getString("level") ?: "B1"

            StudentHomeScreen(
                onNavigateToCourses = {
                    navController.navigate("student_courses/$studentId/$levelStr")
                },
                onNavigateToMySubscriptions = {
                    navController.navigate("student_my_subscriptions/$studentId")
                },
                onNavigateToMyGrades = {
                    navController.navigate("student_grades/$studentId")
                },
                onNavigateToFinalGrade = {
                    navController.navigate("student_final_grade/$studentId/$levelStr")
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            "student_courses/{studentId}/{level}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("level") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val levelStr = backStackEntry.arguments?.getString("level") ?: "B1"
            val level = Level.fromString(levelStr)

            StudentCoursesScreen(
                studentId = studentId,
                studentLevel = level,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "student_my_subscriptions/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0

            StudentMySubscriptionsScreen(
                studentId = studentId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "student_grades/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0

            StudentGradesScreen(
                studentId = studentId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "student_final_grade/{studentId}/{level}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("level") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val levelStr = backStackEntry.arguments?.getString("level") ?: "B1"
            val level = Level.fromString(levelStr)

            StudentFinalGradeScreen(
                studentId = studentId,
                studentLevel = level,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "teacher_home/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0

            TeacherHomeScreen(
                onNavigateToMyCourses = {
                    navController.navigate("teacher_my_courses/$teacherId")
                },
                onNavigateToDeclareCourses = {
                    navController.navigate("teacher_declare_courses/$teacherId")
                },
                onNavigateToStudents = {
                    navController.navigate("teacher_students/$teacherId")
                },
                onNavigateToGradeEntry = {
                    navController.navigate("teacher_grade_entry/$teacherId")
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            "teacher_my_courses/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0

            TeacherMyCoursesScreen(
                teacherId = teacherId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "teacher_declare_courses/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0

            TeacherDeclareCoursesScreen(
                teacherId = teacherId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "teacher_students/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0

            TeacherStudentListScreen(
                teacherId = teacherId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "teacher_grade_entry/{teacherId}",
            arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
        ) { backStackEntry ->
            val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: 0

            TeacherGradeEntryScreen(
                teacherId = teacherId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.STUDENT_LIST) {
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") }
            )
        }

        composable(Routes.STUDENT_FORM) {
            StudentFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0
            StudentDetailScreen(studentId = id, onBack = { navController.popBackStack() })
        }

        composable(Routes.COURSE_LIST) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") }
            )
        }

        composable(Routes.COURSE_FORM) {
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            "course_detail/{courseId}",
            arguments = listOf(navArgument("courseId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseDetailScreen(courseId = id, onBack = { navController.popBackStack() })
        }

        composable(Routes.SUBSCRIBE_LIST) {
            SubscribeListScreen(
                onNavigateToForm = { navController.navigate(Routes.SUBSCRIBE_FORM) }
            )
        }

        composable(Routes.SUBSCRIBE_FORM) {
            SubscribeFormScreen(onSaved = { navController.popBackStack() })
        }
    }
}
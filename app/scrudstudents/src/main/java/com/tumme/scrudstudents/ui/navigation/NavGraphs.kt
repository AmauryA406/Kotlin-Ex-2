package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen

// Objet contenant les constantes des routes de navigation.
object Routes {
    // Routes Students (Partie 1)
    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    // Routes Courses (Partie 2)
    const val COURSE_LIST = "course_list"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"

    // Routes Subscribes (Partie 3)
    const val SUBSCRIBE_LIST = "subscribe_list"
    const val SUBSCRIBE_FORM = "subscribe_form"
}

// Composable définissant le graphe de navigation de l'application.
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Démarre sur la liste des inscriptions (Partie 3) - Changez selon vos besoins.
    NavHost(navController, startDestination = Routes.SUBSCRIBE_LIST) {

        // ========== STUDENTS (Partie 1) ==========

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

        // ========== COURSES (Partie 2) ==========

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
            CourseDetailScreen(
                courseId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // ========== SUBSCRIBES (Partie 3) ==========

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
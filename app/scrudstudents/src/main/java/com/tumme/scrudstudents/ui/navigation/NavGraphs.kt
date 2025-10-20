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

// Objet contenant les constantes des routes de navigation.
object Routes {
    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    // Routes pour les cours (ajoutées pour la Partie 2).
    const val COURSE_LIST = "course_list"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"
}

// Composable définissant le graphe de navigation de l'application.
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Démarre sur la liste des cours (changez pour STUDENT_LIST si vous préférez).
    NavHost(navController, startDestination = Routes.COURSE_LIST) {

        // ========== STUDENTS ==========

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

        // Destination pour la liste des cours.
        composable(Routes.COURSE_LIST) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) }, // Navigue vers le formulaire.
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") } // Navigue vers les détails.
            )
        }

        // Destination pour le formulaire d'ajout de cours.
        composable(Routes.COURSE_FORM) {
            CourseFormScreen(onSaved = { navController.popBackStack() }) // Retourne à l'écran précédent après sauvegarde.
        }

        // Destination pour les détails d'un cours avec argument courseId de type Int.
        composable(
            "course_detail/{courseId}",
            arguments = listOf(navArgument("courseId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0 // Récupère l'ID depuis les arguments.
            CourseDetailScreen(
                courseId = id,
                onBack = { navController.popBackStack() } // Retourne à l'écran précédent.
            )
        }
    }
}
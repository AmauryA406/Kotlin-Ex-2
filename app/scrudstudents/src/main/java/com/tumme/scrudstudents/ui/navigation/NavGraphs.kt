package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen

// Objet contenant les constantes des routes de navigation.
object Routes {
    const val STUDENT_LIST = "student_list" // Route pour la liste des étudiants.
    const val STUDENT_FORM = "student_form" // Route pour le formulaire d'ajout.
    const val STUDENT_DETAIL = "student_detail/{studentId}" // Route pour les détails avec paramètre ID.
}

// Composable définissant le graphe de navigation de l'application.
@Composable
fun AppNavHost() {
    val navController = rememberNavController() // Contrôleur de navigation.

    // NavHost définit les destinations et leur écran associé.
    NavHost(navController, startDestination = Routes.STUDENT_LIST) {

        // Destination pour la liste des étudiants.
        composable(Routes.STUDENT_LIST) {
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) }, // Navigue vers le formulaire.
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") } // Navigue vers les détails avec l'ID.
            )
        }

        // Destination pour le formulaire d'ajout.
        composable(Routes.STUDENT_FORM) {
            StudentFormScreen(onSaved = { navController.popBackStack() }) // Retourne à l'écran précédent après sauvegarde.
        }

        // Destination pour les détails d'un étudiant avec argument studentId de type Int.
        composable(
            "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0 // Récupère l'ID depuis les arguments.
            StudentDetailScreen(
                studentId = id,
                onBack = { navController.popBackStack() } // Retourne à l'écran précédent.
            )
        }
    }
}
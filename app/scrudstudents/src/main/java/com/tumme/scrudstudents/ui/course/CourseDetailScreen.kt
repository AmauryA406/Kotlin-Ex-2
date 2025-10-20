package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity

// Composable affichant les détails d'un cours spécifique.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int, // ID du cours à afficher.
    viewModel: CourseListViewModel = hiltViewModel(), // ViewModel injecté par Hilt.
    onBack: () -> Unit = {} // Callback pour retourner à l'écran précédent.
) {
    // État local nullable pour stocker le cours récupéré, initialement null.
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    // LaunchedEffect se déclenche une seule fois au montage du composable (ou si courseId change).
    LaunchedEffect(courseId) {
        course = viewModel.findCourse(courseId) // Récupère le cours de manière asynchrone.
    }

    // Structure d'écran avec TopAppBar et bouton retour.
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Course Details") }, // Titre de la barre supérieure.
            navigationIcon = {
                IconButton(onClick = onBack) { // Bouton de retour.
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Affiche "Loading..." si le cours n'est pas encore chargé.
            if (course == null) {
                CircularProgressIndicator()
                Text("Loading...")
            } else {
                // Affiche les informations du cours une fois chargé.
                Text("ID: ${course!!.idCourse}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Text("Name: ${course!!.nameCourse}", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("ECTS: ${course!!.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("Level: ${course!!.levelCourse.value}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale

// Composable affichant les détails d'un étudiant spécifique.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Int, // ID de l'étudiant à afficher.
    viewModel: StudentListViewModel = hiltViewModel(), // ViewModel injecté par Hilt.
    onBack: ()->Unit = {} // Callback pour retourner à l'écran précédent.
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formateur de date.

    // État local nullable pour stocker l'étudiant récupéré, initialement null.
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    // LaunchedEffect se déclenche une seule fois au montage du composable (ou si studentId change).
    LaunchedEffect(studentId) {
        student = viewModel.findStudent(studentId) // Récupère l'étudiant de manière asynchrone.
    }

    // Structure d'écran avec TopAppBar et bouton retour.
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Student details") }, // Titre de la barre supérieure.
            navigationIcon = {
                IconButton(onClick = onBack) { // Bouton de retour.
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Affiche "Loading..." si l'étudiant n'est pas encore chargé.
            if (student == null) {
                Text("Loading...")
            } else {
                // Affiche les informations de l'étudiant une fois chargé.
                Text("ID: ${student!!.idStudent}")
                Text("Name: ${student!!.firstName} ${student!!.lastName}")
                Text("DOB: ${sdf.format(student!!.dateOfBirth)}")
                Text("Gender: ${student!!.gender.value}")
            }
        }
    }
}
package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// Composable affichant la liste des cours avec TopAppBar et bouton flottant d'ajout.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    viewModel: CourseListViewModel = hiltViewModel(), // ViewModel injecté automatiquement par Hilt.
    onNavigateToForm: () -> Unit = {}, // Callback pour naviguer vers le formulaire.
    onNavigateToDetail: (Int) -> Unit = {} // Callback pour naviguer vers les détails d'un cours.
) {
    // Observe le StateFlow courses et déclenche une recomposition à chaque changement.
    val courses by viewModel.courses.collectAsState()

    // Structure d'écran Material3 avec barre supérieure et bouton flottant.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Courses") }) // Barre de titre.
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) { // Bouton pour ajouter un cours.
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Liste scrollable affichant chaque cours.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { course -> // Itère sur la liste et recompose uniquement les éléments modifiés.
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onNavigateToDetail(course.idCourse) }, // Navigue vers les détails au clic.
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = course.nameCourse,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ECTS: ${course.ectsCourse} | Level: ${course.levelCourse.value}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(onClick = { viewModel.deleteCourse(course) }) { // Supprime le cours.
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
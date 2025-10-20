package com.tumme.scrudstudents.ui.student

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// Composable affichant la liste des étudiants avec TopAppBar et bouton flottant d'ajout.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    viewModel: StudentListViewModel = hiltViewModel(), // ViewModel injecté automatiquement par Hilt.
    onNavigateToForm: () -> Unit = {}, // Callback pour naviguer vers le formulaire.
    onNavigateToDetail: (Int) -> Unit = {} // Callback pour naviguer vers les détails d'un étudiant.
) {
    // Observe le StateFlow students et déclenche une recomposition à chaque changement.
    val students by viewModel.students.collectAsState()

    val coroutineScope = rememberCoroutineScope() // Scope pour lancer des coroutines dans le Composable.

    // Structure d'écran Material3 avec barre supérieure et bouton flottant.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Students") }) // Barre de titre.
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) { // Bouton pour ajouter un étudiant.
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            // En-tête du tableau avec les colonnes.
            TableHeader(cells = listOf("DOB","Last", "First", "Gender", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f))

            Spacer(modifier = Modifier.height(8.dp))

            // Liste scrollable affichant chaque étudiant sous forme de ligne.
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student -> // Itère sur la liste et recompose uniquement les éléments modifiés.
                    StudentRow(
                        student = student,
                        onEdit = { /* navigate to form prefilled (not implemented here) */ },
                        onDelete = { viewModel.deleteStudent(student) }, // Supprime l'étudiant via le ViewModel.
                        onView = { onNavigateToDetail(student.idStudent) }, // Navigue vers les détails.
                        onShare = { /* share intent */ }
                    )
                }
            }
        }
    }
}
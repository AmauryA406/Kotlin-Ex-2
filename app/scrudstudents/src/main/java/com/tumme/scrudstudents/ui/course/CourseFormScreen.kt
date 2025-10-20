package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse

// Composable affichant un formulaire pour créer un nouveau cours avec validation.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFormScreen(
    viewModel: CourseListViewModel = hiltViewModel(), // ViewModel injecté par Hilt.
    onSaved: () -> Unit = {} // Callback appelé après sauvegarde pour retourner à l'écran précédent.
) {
    // États locaux pour chaque champ du formulaire.
    var id by remember { mutableStateOf((0..10000).random()) } // ID généré aléatoirement.
    var nameCourse by remember { mutableStateOf("") } // État pour le nom du cours.
    var ectsCourse by remember { mutableStateOf("") } // État pour les ECTS (texte pour validation).
    var levelCourse by remember { mutableStateOf(LevelCourse.B1) } // État pour le niveau avec valeur par défaut.
    var errorMessage by remember { mutableStateOf("") } // État pour afficher les erreurs de validation.

    // Liste des niveaux disponibles depuis l'enum.
    val levels = LevelCourse.entries
    var expanded by remember { mutableStateOf(false) } // État pour le menu déroulant.

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Course", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // Champ de texte pour le nom du cours.
        OutlinedTextField(
            value = nameCourse,
            onValueChange = { nameCourse = it },
            label = { Text("Course Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // Champ de texte pour les ECTS.
        OutlinedTextField(
            value = ectsCourse,
            onValueChange = { ectsCourse = it },
            label = { Text("ECTS Credits") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // Menu déroulant pour sélectionner le niveau.
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = levelCourse.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                levels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.value) },
                        onClick = {
                            levelCourse = level
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Affiche le message d'erreur si présent.
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
        }

        // Bouton de sauvegarde avec validation (ECTS > 0 comme demandé dans le PDF).
        Button(
            onClick = {
                val ects = ectsCourse.toFloatOrNull()
                if (nameCourse.isBlank()) {
                    errorMessage = "Course name cannot be empty"
                } else if (ects == null || ects <= 0) {
                    errorMessage = "ECTS must be a positive number" // Validation demandée.
                } else {
                    errorMessage = ""
                    val course = CourseEntity(
                        idCourse = id,
                        nameCourse = nameCourse,
                        ectsCourse = ects,
                        levelCourse = levelCourse
                    )
                    viewModel.insertCourse(course) // Insère le cours via le ViewModel.
                    onSaved() // Exécute le callback pour fermer le formulaire.
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Course")
        }
    }
}
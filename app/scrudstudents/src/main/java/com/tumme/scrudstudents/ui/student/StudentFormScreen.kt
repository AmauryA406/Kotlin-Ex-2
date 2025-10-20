package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity

// Composable affichant un formulaire pour créer un nouvel étudiant.
@Composable
fun StudentFormScreen(
    viewModel: StudentListViewModel = hiltViewModel(), // ViewModel injecté par Hilt.
    onSaved: ()->Unit = {} // Callback appelé après sauvegarde pour retourner à l'écran précédent.
) {
    // États locaux pour chaque champ du formulaire, remember conserve les valeurs lors des recompositions.
    var id by remember { mutableStateOf((0..10000).random()) } // ID généré aléatoirement.
    var lastName by remember { mutableStateOf("") } // État pour le nom de famille.
    var firstName by remember { mutableStateOf("") } // État pour le prénom.
    var dobText by remember { mutableStateOf("2000-01-01") } // État pour la date de naissance au format texte.
    var gender by remember { mutableStateOf(Gender.NotConcerned) } // État pour le genre avec valeur par défaut.

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formateur de date.

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Champ de texte pour le nom avec liaison bidirectionnelle (two-way binding).
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(Modifier.height(8.dp))

        // Champ de texte pour le prénom.
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))

        // Champ de texte pour la date de naissance.
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
        Spacer(Modifier.height(8.dp))

        // Sélecteur de genre avec des boutons pour chaque option.
        Row {
            listOf(Gender.Male, Gender.Female, Gender.NotConcerned).forEach { g->
                Button(onClick = { gender = g }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(g.value)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Bouton de sauvegarde qui crée un StudentEntity et l'insère via le ViewModel.
        Button(onClick = {
            val dob = dateFormat.parse(dobText) ?: Date() // Parse la date ou utilise la date actuelle par défaut.
            val student = StudentEntity(
                idStudent = id,
                lastName = lastName,
                firstName = firstName,
                dateOfBirth = dob,
                gender = gender
            )
            viewModel.insertStudent(student) // Insère l'étudiant via le ViewModel.
            onSaved() // Exécute le callback pour fermer le formulaire.
        }) {
            Text("Save")
        }
    }
}
package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeListViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    val students by viewModel.allStudents.collectAsState()
    val courses by viewModel.allCourses.collectAsState()

    var selectedStudentId by remember { mutableStateOf<Int?>(null) }
    var selectedCourseId by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var expandedStudent by remember { mutableStateOf(false) }
    var expandedCourse by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Subscribe", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expandedStudent,
            onExpandedChange = { expandedStudent = !expandedStudent }
        ) {
            OutlinedTextField(
                value = students.find { it.idStudent == selectedStudentId }?.let { "${it.firstName} ${it.lastName}" } ?: "Select Student",
                onValueChange = {},
                readOnly = true,
                label = { Text("Student") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStudent) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedStudent,
                onDismissRequest = { expandedStudent = false }
            ) {
                students.forEach { student ->
                    DropdownMenuItem(
                        text = { Text("${student.firstName} ${student.lastName}") },
                        onClick = {
                            selectedStudentId = student.idStudent
                            expandedStudent = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedCourse,
            onExpandedChange = { expandedCourse = !expandedCourse }
        ) {
            OutlinedTextField(
                value = courses.find { it.idCourse == selectedCourseId }?.nameCourse ?: "Select Course",
                onValueChange = {},
                readOnly = true,
                label = { Text("Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCourse) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCourse,
                onDismissRequest = { expandedCourse = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourseId = course.idCourse
                            expandedCourse = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Score") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val scoreFloat = score.toFloatOrNull()
                when {
                    selectedStudentId == null -> errorMessage = "Please select a student"
                    selectedCourseId == null -> errorMessage = "Please select a course"
                    scoreFloat == null -> errorMessage = "Please enter a valid score"
                    else -> {
                        errorMessage = ""
                        val subscribe = SubscribeEntity(
                            studentId = selectedStudentId!!,
                            courseId = selectedCourseId!!,
                            score = scoreFloat
                        )
                        viewModel.insertSubscribe(subscribe)
                        onSaved()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Subscribe")
        }
    }
}
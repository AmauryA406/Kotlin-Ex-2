package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherDeclareCoursesViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    val allCourses = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun assignCourse(course: CourseEntity, teacherId: Int) {
        viewModelScope.launch {
            val updatedCourse = course.copy(teacherId = teacherId)
            repository.insertCourse(updatedCourse)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDeclareCoursesScreen(
    teacherId: Int,
    viewModel: TeacherDeclareCoursesViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val allCourses by viewModel.allCourses.collectAsState()
    val availableCourses = allCourses.filter { it.teacherId == null }

    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Declare Courses") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = {
            if (showSuccessMessage) {
                Snackbar(
                    action = {
                        TextButton(onClick = { showSuccessMessage = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text("Course assigned successfully!")
                }
            }
        }
    ) { padding ->
        if (availableCourses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No available courses to declare")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableCourses) { course ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            selectedCourse = course
                            showDialog = true
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                course.nameCourse,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("ECTS: ${course.ectsCourse}")
                            Text("Level: ${course.levelCourse.name}")
                            Text(
                                "No teacher assigned",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        if (showDialog && selectedCourse != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Assign to ${selectedCourse?.nameCourse}?") },
                text = { Text("Do you want to teach this course?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedCourse?.let { course ->
                                viewModel.assignCourse(course, teacherId)
                                showSuccessMessage = true
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Assign")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
package com.tumme.scrudstudents.ui.student

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
import com.tumme.scrudstudents.data.local.model.Level
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentCoursesViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    val courses = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun subscribeToCourse(subscribe: SubscribeEntity) {
        viewModelScope.launch {
            repository.insertSubscribe(subscribe)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCoursesScreen(
    studentId: Int,
    studentLevel: Level,
    viewModel: StudentCoursesViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val allCourses by viewModel.courses.collectAsState()
    val availableCourses = allCourses.filter { it.levelCourse.name == studentLevel.value }

    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Courses (${studentLevel.displayName})") },
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
                    Text("Successfully subscribed!")
                }
            }
        }
    ) { padding ->
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
                        Text(course.nameCourse, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ECTS: ${course.ectsCourse}", style = MaterialTheme.typography.bodyMedium)
                        Text("Level: ${course.levelCourse.name}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        if (showDialog && selectedCourse != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Subscribe to ${selectedCourse?.nameCourse}?") },
                text = { Text("Do you want to enroll in this course?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedCourse?.let { course ->
                                viewModel.subscribeToCourse(
                                    SubscribeEntity(
                                        studentId = studentId,
                                        courseId = course.idCourse,
                                        score = -1f
                                    )
                                )
                                showSuccessMessage = true
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Subscribe")
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
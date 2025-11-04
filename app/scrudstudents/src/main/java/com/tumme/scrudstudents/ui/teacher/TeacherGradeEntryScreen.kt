package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherGradeEntryViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    val allSubscriptions = repository.getAllSubscribesWithDetails()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCourses = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateGrade(studentId: Int, courseId: Int, newScore: Float) {
        viewModelScope.launch {
            val subscribe = SubscribeEntity(
                studentId = studentId,
                courseId = courseId,
                score = newScore
            )
            repository.insertSubscribe(subscribe)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherGradeEntryScreen(
    teacherId: Int,
    viewModel: TeacherGradeEntryViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val allCourses by viewModel.allCourses.collectAsState()
    val allSubscriptions by viewModel.allSubscriptions.collectAsState()

    val myCourses = allCourses.filter { it.teacherId == teacherId }
    val myCourseIds = myCourses.map { it.idCourse }.toSet()
    val myStudents = allSubscriptions.filter { it.courseId in myCourseIds }

    var selectedCourseId by remember { mutableStateOf<Int?>(null) }
    var gradeInputs by remember { mutableStateOf<Map<Pair<Int, Int>, String>>(emptyMap()) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val filteredStudents = if (selectedCourseId != null) {
        myStudents.filter { it.courseId == selectedCourseId }
    } else {
        myStudents
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grade Entry") },
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
                    Text("Grades saved successfully!")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (myCourses.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = if (selectedCourseId == null) "Select a course"
                        else myCourses.find { it.idCourse == selectedCourseId }?.nameCourse ?: "Select a course",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Course") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        myCourses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course.nameCourse) },
                                onClick = {
                                    selectedCourseId = course.idCourse
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (selectedCourseId == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Please select a course to enter grades")
                }
            } else if (filteredStudents.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No students enrolled in this course")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredStudents) { subscription ->
                        val key = Pair(subscription.studentId, subscription.courseId)
                        val currentGrade = gradeInputs[key] ?:
                        if (subscription.score >= 0) subscription.score.toString() else ""

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        subscription.studentName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        subscription.courseName,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                OutlinedTextField(
                                    value = currentGrade,
                                    onValueChange = { value ->
                                        gradeInputs = gradeInputs + (key to value)
                                    },
                                    label = { Text("Grade") },
                                    placeholder = { Text("/20") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.width(100.dp)
                                )

                                IconButton(
                                    onClick = {
                                        val grade = currentGrade.toFloatOrNull()
                                        if (grade != null && grade in 0f..20f) {
                                            viewModel.updateGrade(
                                                subscription.studentId,
                                                subscription.courseId,
                                                grade
                                            )
                                            showSuccessMessage = true
                                        }
                                    },
                                    enabled = currentGrade.toFloatOrNull()?.let { it in 0f..20f } == true
                                ) {
                                    Icon(Icons.Default.Save, "Save")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
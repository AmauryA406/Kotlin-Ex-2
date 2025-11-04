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
import com.tumme.scrudstudents.data.local.model.SubscribeWithDetails
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TeacherStudentListViewModel @Inject constructor(
    repository: SCRUDRepository
) : ViewModel() {

    val allSubscriptions = repository.getAllSubscribesWithDetails()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCourses = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentListScreen(
    teacherId: Int,
    viewModel: TeacherStudentListViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val allCourses by viewModel.allCourses.collectAsState()
    val allSubscriptions by viewModel.allSubscriptions.collectAsState()

    val myCourses = allCourses.filter { it.teacherId == teacherId }
    val myCourseIds = myCourses.map { it.idCourse }.toSet()
    val myStudents = allSubscriptions.filter { it.courseId in myCourseIds }

    var selectedCourseId by remember { mutableStateOf<Int?>(null) }

    val filteredStudents = if (selectedCourseId != null) {
        myStudents.filter { it.courseId == selectedCourseId }
    } else {
        myStudents
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Students") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
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
                        value = if (selectedCourseId == null) "All Courses"
                        else myCourses.find { it.idCourse == selectedCourseId }?.nameCourse ?: "All Courses",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filter by Course") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Courses") },
                            onClick = {
                                selectedCourseId = null
                                expanded = false
                            }
                        )
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

            if (filteredStudents.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No students enrolled yet")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredStudents) { subscription ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    subscription.studentName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    subscription.courseName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (subscription.score >= 0) {
                                    Text(
                                        "Grade: %.1f/20".format(subscription.score),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (subscription.score >= 10)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text(
                                        "Not graded yet",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
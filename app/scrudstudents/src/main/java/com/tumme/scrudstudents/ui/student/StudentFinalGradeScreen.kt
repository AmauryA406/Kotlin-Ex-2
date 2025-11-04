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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GradeDetail(
    val courseName: String,
    val ects: Float,
    val score: Float,
    val weightedScore: Float
)

@HiltViewModel
class StudentFinalGradeViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {

    suspend fun calculateWeightedGrade(studentId: Int, level: Level): Pair<Float?, List<GradeDetail>> {
        val subscriptions = repository.getSubscribesByStudent(studentId).first()
        val details = mutableListOf<GradeDetail>()

        var totalWeightedScore = 0f
        var totalEcts = 0f

        for (subscribe in subscriptions) {
            if (subscribe.score >= 0) {
                val course = repository.getCourseById(subscribe.courseId)
                if (course != null && course.levelCourse.name == level.value) {
                    val weighted = subscribe.score * course.ectsCourse
                    totalWeightedScore += weighted
                    totalEcts += course.ectsCourse

                    details.add(
                        GradeDetail(
                            courseName = course.nameCourse,
                            ects = course.ectsCourse,
                            score = subscribe.score,
                            weightedScore = weighted
                        )
                    )
                }
            }
        }

        val finalGrade = if (totalEcts > 0) totalWeightedScore / totalEcts else null
        return Pair(finalGrade, details)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFinalGradeScreen(
    studentId: Int,
    studentLevel: Level,
    viewModel: StudentFinalGradeViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    var finalGrade by remember { mutableStateOf<Float?>(null) }
    var gradeDetails by remember { mutableStateOf<List<GradeDetail>>(emptyList()) }

    LaunchedEffect(studentId, studentLevel) {
        val (grade, details) = viewModel.calculateWeightedGrade(studentId, studentLevel)
        finalGrade = grade
        gradeDetails = details
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Final Grade (${studentLevel.displayName})") },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        "Weighted Average",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (finalGrade != null) {
                        Text(
                            "%.2f / 20".format(finalGrade),
                            style = MaterialTheme.typography.displayLarge,
                            color = if (finalGrade!! >= 10)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            "No grades yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            if (gradeDetails.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Grade Details",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gradeDetails) { detail ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    detail.courseName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("ECTS: ${detail.ects}")
                                    Text("Grade: %.1f/20".format(detail.score))
                                }
                                Text(
                                    "Weighted: %.2f".format(detail.weightedScore),
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
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    studentId: Int,
    viewModel: StudentSubscriptionsViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val allSubscriptions by viewModel.allSubscriptions.collectAsState()
    val mySubscriptions = allSubscriptions.filter { it.studentId == studentId }
    val gradedSubscriptions = mySubscriptions.filter { it.score >= 0 }

    val averageGrade = if (gradedSubscriptions.isNotEmpty()) {
        gradedSubscriptions.map { it.score }.average().toFloat()
    } else {
        0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Grades") },
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Statistics",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total courses: ${mySubscriptions.size}")
                    Text("Graded courses: ${gradedSubscriptions.size}")
                    if (gradedSubscriptions.isNotEmpty()) {
                        Text(
                            "Average: %.2f/20".format(averageGrade),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (gradedSubscriptions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No grades available yet")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gradedSubscriptions) { subscription ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        subscription.courseName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Text(
                                    "%.1f/20".format(subscription.score),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (subscription.score >= 10)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
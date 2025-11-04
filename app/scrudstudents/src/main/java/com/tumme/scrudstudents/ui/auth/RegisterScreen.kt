package com.tumme.scrudstudents.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: (UserRole, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    var selectedGender by remember { mutableStateOf<Gender>(Gender.entries.first()) }
    var selectedLevel by remember { mutableStateOf<Level>(Level.B1) }
    var department by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userId by viewModel.userId.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success && userRole != null && userId != null) {
            onRegisterSuccess(userRole!!, userId!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Account", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = selectedRole == UserRole.STUDENT,
                    onClick = { selectedRole = UserRole.STUDENT },
                    label = { Text("Student") }
                )
                FilterChip(
                    selected = selectedRole == UserRole.TEACHER,
                    onClick = { selectedRole = UserRole.TEACHER },
                    label = { Text("Teacher") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedRole == UserRole.STUDENT) {
                var expandedGender by remember { mutableStateOf(false) }
                var expandedLevel by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = { expandedGender = it }
                ) {
                    OutlinedTextField(
                        value = selectedGender.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false }
                    ) {
                        Gender.entries.forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender.name) },
                                onClick = {
                                    selectedGender = gender
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedLevel,
                    onExpandedChange = { expandedLevel = it }
                ) {
                    OutlinedTextField(
                        value = selectedLevel.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Level") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLevel,
                        onDismissRequest = { expandedLevel = false }
                    ) {
                        Level.entries.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level.displayName) },
                                onClick = {
                                    selectedLevel = level
                                    expandedLevel = false
                                }
                            )
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Department") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Loading) {
                CircularProgressIndicator()
            }

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (selectedRole == UserRole.STUDENT) {
                        val student = StudentEntity(
                            email = email,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = Date(),
                            gender = selectedGender,
                            level = selectedLevel
                        )
                        viewModel.registerStudent(student)
                    } else {
                        val teacher = TeacherEntity(
                            email = email,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            department = department
                        )
                        viewModel.registerTeacher(teacher)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank() &&
                        firstName.isNotBlank() && lastName.isNotBlank() &&
                        authState !is AuthState.Loading
            ) {
                Text("Register")
            }
        }
    }
}
package com.tumme.scrudstudents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.UserRole
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    fun loginStudent(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.loginStudent(email, password)

            if (result.success) {
                _userRole.value = result.role
                _userId.value = result.userId
                _authState.value = AuthState.Success(result.message)
            } else {
                _authState.value = AuthState.Error(result.message)
            }
        }
    }

    fun loginTeacher(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.loginTeacher(email, password)

            if (result.success) {
                _userRole.value = result.role
                _userId.value = result.userId
                _authState.value = AuthState.Success(result.message)
            } else {
                _authState.value = AuthState.Error(result.message)
            }
        }
    }

    fun registerStudent(student: StudentEntity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.registerStudent(student)

            if (result.success) {
                _userRole.value = result.role
                _userId.value = result.userId
                _authState.value = AuthState.Success(result.message)
            } else {
                _authState.value = AuthState.Error(result.message)
            }
        }
    }

    fun registerTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.registerTeacher(teacher)

            if (result.success) {
                _userRole.value = result.role
                _userId.value = result.userId
                _authState.value = AuthState.Success(result.message)
            } else {
                _authState.value = AuthState.Error(result.message)
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        _userRole.value = null
        _userId.value = null
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
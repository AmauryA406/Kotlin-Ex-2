package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel gérant la logique et l'état de l'écran de liste des étudiants, injecté par Hilt.
@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val repo: SCRUDRepository // Repository injecté pour accéder aux données.
) : ViewModel() {

    // StateFlow privé convertissant le Flow du repository en état observable avec valeur initiale vide.
    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // StateFlow public exposé à l'UI pour observer la liste des étudiants.
    val students: StateFlow<List<StudentEntity>> = _students

    // SharedFlow privé pour émettre des événements UI ponctuels (messages de confirmation).
    private val _events = MutableSharedFlow<String>()

    // SharedFlow public en lecture seule pour l'UI.
    val events = _events.asSharedFlow()

    // Supprime un étudiant et émet un événement de confirmation.
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        repo.deleteStudent(student)
        _events.emit("Student deleted")
    }

    // Insère un étudiant et émet un événement de confirmation.
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        repo.insertStudent(student)
        _events.emit("Student inserted")
    }

    // Recherche et retourne un étudiant par son ID de manière asynchrone.
    suspend fun findStudent(id: Int) = repo.getStudentById(id)
}
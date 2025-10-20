package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel gérant la logique et l'état de l'écran de liste des cours, injecté par Hilt.
@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repo: SCRUDRepository // Repository injecté pour accéder aux données.
) : ViewModel() {

    // StateFlow privé convertissant le Flow du repository en état observable avec valeur initiale vide.
    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // StateFlow public exposé à l'UI pour observer la liste des cours.
    val courses: StateFlow<List<CourseEntity>> = _courses

    // SharedFlow privé pour émettre des événements UI ponctuels (messages de confirmation).
    private val _events = MutableSharedFlow<String>()

    // SharedFlow public en lecture seule pour l'UI.
    val events = _events.asSharedFlow()

    // Supprime un cours et émet un événement de confirmation.
    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        repo.deleteCourse(course)
        _events.emit("Course deleted")
    }

    // Insère un cours et émet un événement de confirmation.
    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        repo.insertCourse(course)
        _events.emit("Course inserted")
    }

    // Recherche et retourne un cours par son ID de manière asynchrone.
    suspend fun findCourse(id: Int) = repo.getCourseById(id)
}
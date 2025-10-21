package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.SubscribeWithDetails
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _subscribesWithDetails: StateFlow<List<SubscribeWithDetails>> =
        repo.getAllSubscribesWithDetails().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val subscribesWithDetails: StateFlow<List<SubscribeWithDetails>> = _subscribesWithDetails

    val allStudents: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allCourses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun deleteSubscribe(subscribe: SubscribeWithDetails) = viewModelScope.launch {
        val entity = SubscribeEntity(subscribe.studentId, subscribe.courseId, subscribe.score)
        repo.deleteSubscribe(entity)
        _events.emit("Subscribe deleted")
    }

    fun insertSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.insertSubscribe(subscribe)
        _events.emit("Subscribe inserted")
    }
}
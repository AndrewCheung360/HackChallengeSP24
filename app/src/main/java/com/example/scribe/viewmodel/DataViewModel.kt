package com.example.scribe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scribe.data.model.CourseData
import com.example.scribe.data.model.NoteData
import com.example.scribe.data.model.UserData
import com.example.scribe.data.repositories.DataRepository
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataViewModel(private val repository: DataRepository): ViewModel(){
    private val _courses = MutableStateFlow<List<CourseData>>(emptyList())
    val courses = _courses.asStateFlow()

    private val _notes = MutableStateFlow<List<NoteData>>(emptyList())
    val notes = _notes.asStateFlow()

    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users = _users.asStateFlow()


    init {
        fetchCourses()
        fetchNotes()
        fetchUsers()
    }

    private fun fetchCourses() {
        viewModelScope.launch(Dispatchers.IO) {
            val courses = repository.getCourses()
            _courses.emit(courses)
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = repository.getUsers()
            _users.emit(users)
        }
    }

    private fun fetchNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = repository.getNotes()
            _notes.emit(notes)
        }
    }

    fun getPdfByNoteId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPdfByNoteId(id)
        }
    }

    fun getNotesByCourseId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNotesByCourseId(id)
        }
    }

    fun getCourseById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCourseById(id)
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserById(id)
        }
    }





}
package com.example.scribe.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel: ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _course = MutableStateFlow(allCourse)
    val course = searchText
        .combine(_course) { text, course ->
            if(text.isBlank()) {
                course
            } else {
                course.filter {
                    it.doesSearchExist(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _course.value
        )

    fun searchTextValue(text: String) {
        _searchText.value = text
    }


}

data class Course(
    val courseName: String
) {
    fun doesSearchExist(query: String): Boolean {
        val combinations = listOf(
            courseName,
        )

        return combinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}


private val allCourse = listOf(
    Course(
        courseName = "Intro to Python"
    ),
    Course(
        courseName = "Object-Oriented Programming"
    )
)
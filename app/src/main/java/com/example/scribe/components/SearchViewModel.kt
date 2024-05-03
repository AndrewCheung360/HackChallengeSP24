package com.example.scribe.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scribe.ui.theme.BlueEnd
import com.example.scribe.ui.theme.BlueStart
import com.example.scribe.ui.theme.GreenEnd
import com.example.scribe.ui.theme.GreenStart
import com.example.scribe.ui.theme.OrangeEnd
import com.example.scribe.ui.theme.OrangeStart
import com.example.scribe.ui.theme.PurpleEnd
import com.example.scribe.ui.theme.PurpleStart
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

    private val _displayedCourses = MutableStateFlow<List<Course>>(emptyList())
    val displayCourses = _displayedCourses.asStateFlow()

    val course = searchText
        .combine(_course) { text, course ->
            if(text.isBlank()) {
                emptyCourse
            } else {
                course.asSequence()
                    .map { it to it.doesSearchExist(text) }
                    .filter { it.second }
                    .sortedByDescending { it.second }
                    .take(5)
                    .map {it.first}
                    .toList()
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

    fun addCourse(courseName: String) {
        val course = allCourse.find { it.courseName == courseName }
        if (course != null) {
            _displayedCourses.value = _displayedCourses.value + course
        }
    }


}


private val emptyCourse = emptyList<Course>(

)



private val allCourse = listOf(
    Course(
        courseName = "Intro to Python",
        code = "CS 1110",
        semester = "Fall 2021",
        color = getGradient(PurpleStart, PurpleEnd)
    ),
    Course(
        courseName = "Object-Oriented Programming",
        code = "CS 2110",
        semester = "Spring 2022",
        color = getGradient(BlueStart, BlueEnd)
    ),
    Course(
        courseName = "Data Structures and Functional Programming",
        code = "CS 3110",
        semester = "Fall 2022",
        color = getGradient(OrangeStart, OrangeEnd)
    ),
    Course(
        courseName = "Computer Organization",
        code = "CS 3410",
        semester = "Spring 2023",
        color = getGradient(GreenStart, GreenEnd)
    ),
    Course(
        courseName = "algo",
        code = "CS 4820",
        semester = "Spring 2023",
        color = getGradient(GreenStart, GreenEnd)
    )
)
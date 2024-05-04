package com.example.scribe.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scribe.components.home.getGradient
import com.example.scribe.data.Course
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel: ViewModel() {

    //search bar instances
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _userCourses = MutableStateFlow<List<Course>>(emptyList())
    val userCourses = _userCourses.asStateFlow()

    private val courses = MutableStateFlow(allCourses)
    val searchCourses = searchText
            .combine(courses) { text, course ->
            if(text.isBlank()) {
                emptyList()
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
            courses.value
        )


    fun searchTextValue(text: String) {
        _searchText.value = text
    }

    fun getCourseList(): StateFlow<List<Course>> {
        return courses
    }

    fun addCourse(courseName: String) {
        val course = courses.value.find { it.courseName == courseName }
        if (course != null) {
            _userCourses.value += course
        }
    }

}


val allCourses = mutableStateListOf(
    Course(
        id = 1, courseName = "Intro to Python", code = "CS 1110", semester = "Fall 2021", color = getGradient(
            PurpleStart, PurpleEnd
        )
    ),
    Course(
        id = 2, courseName = "Object-Oriented Programming", code = "CS 2110", semester = "Spring 2022", color = getGradient(
            BlueStart, BlueEnd
        )
    ),
    Course(
        id = 3, courseName = "Data Structures and Functional Programming", code = "CS 3110", semester = "Fall 2022", color = getGradient(
            OrangeStart, OrangeEnd
        )
    ),
    Course(
        id = 4, courseName = "Computer Organization", code = "CS 3410", semester = "Spring 2023", color = getGradient(
            GreenStart, GreenEnd
        )
    ),
    Course(
        id = 5,
        courseName = "Introduction to Analysis of Algorithms",
        code = "CS 4820",
        semester = "Spring 2023",
        color = getGradient(BlueStart, BlueEnd)
    ),
    Course(
        id = 6,
        courseName = "Introduction to Machine Learning",
        code = "CS 4780",
        semester = "Spring 2023",
        color = getGradient(OrangeStart, OrangeEnd)
    ),
    Course(
        id = 7,
        courseName = "Introduction to Computer Vision",
        code = "CS 4670",
        semester = "Spring 2023",
        color = getGradient(GreenStart, GreenEnd)
    ),
    Course(
        id = 8,
        courseName = "Introduction to Natural Language Processing",
        code = "CS 4740",
        semester = "Spring 2023",
        color = getGradient(PurpleStart, PurpleEnd)
    ),
)
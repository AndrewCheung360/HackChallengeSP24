package com.example.scribe.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    val courses = courses1

}
val courses1 = mutableStateListOf(
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
)
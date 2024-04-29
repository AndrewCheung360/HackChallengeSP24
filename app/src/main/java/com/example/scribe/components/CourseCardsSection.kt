package com.example.scribe.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.scribe.ui.theme.BlueEnd
import com.example.scribe.ui.theme.BlueStart
import com.example.scribe.ui.theme.GreenEnd
import com.example.scribe.ui.theme.GreenStart
import com.example.scribe.ui.theme.OrangeEnd
import com.example.scribe.ui.theme.OrangeStart
import com.example.scribe.ui.theme.PurpleEnd
import com.example.scribe.ui.theme.PurpleStart


@Composable
fun CourseCardsSection() {
    LazyRow{
        items(courses.size) { index ->
            CourseCard(index, courses)
        }
    }
}

val courses = listOf(
    Course(
        courseName = "Intro to Python", code = "CS 1110", semester = "Fall 2021", color = getGradient(PurpleStart, PurpleEnd)
    ),
    Course(
        courseName = "Object-Oriented Programming", code = "CS 2110", semester = "Spring 2022", color = getGradient(BlueStart, BlueEnd)
    ),
    Course(
        courseName = "Data Structures and Functional Programming", code = "CS 3110", semester = "Fall 2022", color = getGradient(OrangeStart, OrangeEnd)
    ),
    Course(
        courseName = "Computer Organization", code = "CS 3410", semester = "Spring 2023", color = getGradient(GreenStart, GreenEnd)
    ),
)

fun getGradient(
    startColor: Color,
    endColor: Color,
): Brush {
    return Brush.horizontalGradient(
        colors = listOf(startColor, endColor)
    )
}


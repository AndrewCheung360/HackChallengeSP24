package com.example.scribe.components.course

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun CourseNotes(courseId: Int) {
    Text(text = courseId.toString())
}
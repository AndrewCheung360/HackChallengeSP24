package com.example.scribe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CourseCard(index: Int, courses: List<Course>) {

    val course = courses[index]
    var lastItemPaddingEnd = 0.dp
    if (index == courses.size - 1) {
        lastItemPaddingEnd = 16.dp
    }

    Box(
        modifier = Modifier.padding(start = 16.dp, end = lastItemPaddingEnd)
            .clip(RoundedCornerShape(24.dp))
            .size(200.dp, 250.dp)
            .background(course.color)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = course.courseName,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = course.code,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Text(
                text = course.semester,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }



}
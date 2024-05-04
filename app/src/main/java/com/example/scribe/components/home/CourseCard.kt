package com.example.scribe.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scribe.models.Course

@Composable
fun CourseCard(course: Course, selectedCourse: (Int) -> Unit) {



    Box(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(course.color)
            .fillMaxWidth()
            .height(160.dp)
            .clickable { selectedCourse(course.id) }
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
        }
    }



}
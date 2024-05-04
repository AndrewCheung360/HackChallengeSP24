package com.example.scribe.components.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scribe.data.Course


@Composable
fun CourseCardsSection(selectedCourse: (Int) -> Unit, courses: List<Course>){
    val userCourses = remember { courses }
    LazyColumn(modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(8.dp).padding(bottom = 40.dp)){
        items(courses.size) { index ->
            CourseCard(userCourses[index], selectedCourse)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}



fun getGradient(
    startColor: Color,
    endColor: Color,
): Brush {
    return Brush.horizontalGradient(
        colors = listOf(startColor, endColor)
    )
}


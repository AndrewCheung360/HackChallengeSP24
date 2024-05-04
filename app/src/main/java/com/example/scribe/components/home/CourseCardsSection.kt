package com.example.scribe.components.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.scribe.viewmodel.MainViewModel


@Composable
//fun CourseCardsSection(selectedCourse: (Int) -> Unit, courses: List<Course>){
//
//    LazyColumn(modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(8.dp).padding(bottom = 40.dp)){
//        items(courses.size) { index ->
//            CourseCard(courses[index], selectedCourse)
//            Spacer(modifier = Modifier.height(4.dp))
//        }
//    }
//}

fun CourseCardsSection(selectedCourse: (Int) -> Unit, viewModel: MainViewModel){
    val courses by viewModel.userCourses.collectAsState()
    LazyColumn(modifier = androidx.compose.ui.Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .padding(bottom = 40.dp)){
        items(courses.size) { index ->
            CourseCard(courses[index], selectedCourse)

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


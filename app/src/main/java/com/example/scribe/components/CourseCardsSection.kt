package com.example.scribe.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.scribe.components.home.CourseCard
import com.example.scribe.components.home.SearchViewModel
import com.example.scribe.data.Course
import com.example.scribe.ui.theme.BlueEnd
import com.example.scribe.ui.theme.BlueStart
import com.example.scribe.ui.theme.GreenEnd
import com.example.scribe.ui.theme.GreenStart
import com.example.scribe.ui.theme.OrangeEnd
import com.example.scribe.ui.theme.OrangeStart
import com.example.scribe.ui.theme.PurpleEnd
import com.example.scribe.ui.theme.PurpleStart


@Composable
fun CourseCardsSection(viewModel: SearchViewModel, selectedCourse: (Int) -> Unit) {
    val courses by viewModel.displayCourses.collectAsState()
    LazyRow{
        items(courses.size) { index ->
            CourseCard(courses[index], selectedCourse)
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

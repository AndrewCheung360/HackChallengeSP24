package com.example.scribe.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scribe.data.Course

@Composable
fun MainScreen(name : String,
               avatar: String,
               searchText: String,
               courses: SnapshotStateList<Course>,
               searchViewModel: SearchViewModel,
               selectedCourse: (Int) -> Unit
)
{
    val searchCourses by searchViewModel.course.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        GreetingSection(name = name, avatar = avatar)

        Spacer(modifier = Modifier.padding(8.dp))

        SearchBar(searchText = searchText, courses = searchCourses, viewModel = searchViewModel)

//        Spacer(modifier = Modifier.padding(16.dp))

        CourseCardsSection(selectedCourse = selectedCourse, courses = courses)





    }
}
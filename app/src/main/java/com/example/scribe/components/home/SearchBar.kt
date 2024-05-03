package com.example.scribe.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scribe.data.Course

@Composable
fun SearchBar(searchText: String, courses: List<Course>, viewModel: SearchViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchText,
            onValueChange = { viewModel.searchTextValue(it) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
        FloatingActionButton(
            onClick = { viewModel.addCourse(searchText) },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Course")
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(courses) { course ->
            Button(onClick = { viewModel.searchTextValue(course.courseName) }) {
                Text(
                    text = course.courseName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }

        }
    }


}

//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                TextField(
//                    value = searchText,
//                    onValueChange = viewModel::searchTextValue,
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = { Text(text = "Search") }
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                ) {
//                    items(courses) { course ->
//                        Text(
//                            text = course.courseName,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 6.dp)
//                        )
//
//                    }
//                }
//
//            }
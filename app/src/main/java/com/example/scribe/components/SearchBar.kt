package com.example.scribe.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(searchText: String, viewModel: SearchViewModel) {
    TextField(
        value = searchText,
        onValueChange = { viewModel.searchTextValue(it) },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
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
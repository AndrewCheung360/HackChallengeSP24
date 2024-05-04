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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scribe.models.Course
import com.example.scribe.viewmodel.MainViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.layout.BoxScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SearchBar(searchText: String, courses: List<Course>, viewModel: MainViewModel) {
    val searchCourses = remember { viewModel.searchCourses.value }
    val showMessage = remember { mutableStateOf(false) }

    val message by viewModel.message.collectAsState()

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchText,
            onValueChange = { viewModel.searchTextValue(it) },
            label = { Text("Click to Add Courses") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
        FloatingActionButton(
            onClick = {
                viewModel.addCourse(searchText)
                viewModel.searchTextValue("")
                showMessage.value = true
                viewModel.viewModelScope.launch{
                    delay(3000L)
                    showMessage.value = false
                }

                      },
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
    if (showMessage.value) {
        Snackbar(
            action = {
                Button(onClick = { showMessage.value = false }) {
                    Text("OK")
                }
            }
        ) {
            Text(text = message)
        }
    }


}


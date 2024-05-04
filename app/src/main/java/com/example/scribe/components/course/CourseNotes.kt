package com.example.scribe.components.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scribe.models.Note
import com.example.scribe.viewmodel.MainViewModel

@Composable
fun CourseNotes(courseId: Int, viewModel: MainViewModel, selectedNote: (Int, Int) -> Unit) {
    val course = viewModel.getCourse(courseId)
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(course.notes.size) { index ->
            val note = course.notes[index]
            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedNote(
                            courseId,
                            note.id)
                    },
                onFavoriteClick = {

                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
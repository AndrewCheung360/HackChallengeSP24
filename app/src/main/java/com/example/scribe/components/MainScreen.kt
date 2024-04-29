package com.example.scribe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(name : String, avatar: String, searchText: String, searchViewModel: SearchViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        GreetingSection(name = name, avatar = avatar)

        Spacer(modifier = Modifier.padding(8.dp))

        SearchBar(searchText = searchText, viewModel = searchViewModel)

        Spacer(modifier = Modifier.padding(16.dp))

        CourseCardsSection()





    }
}
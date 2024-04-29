package com.example.scribe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(name : String, avatar: String){
    Column(modifier = Modifier
        .fillMaxSize().padding(8.dp)) {
        GreetingSection(name = name, avatar = avatar)

    }
}
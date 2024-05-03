package com.example.scribe.components.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun GreetingSection(name: String, avatar: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Column(verticalArrangement = Arrangement.Center,
            ) {
            Text(text = "Hello,",
                style = MaterialTheme.typography.bodyLarge,)
            Row(verticalAlignment = Alignment.CenterVertically,){
                Text(text = name,
                    style = MaterialTheme.typography.headlineLarge,)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "\uD83D\uDC4B", style = MaterialTheme.typography.headlineLarge)
            }

        }
        AsyncImage(
            model = avatar,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape).border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
        )
    }
}
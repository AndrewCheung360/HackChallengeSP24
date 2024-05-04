package com.example.scribe.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scribe.components.auth.GoogleSignInButton
import com.example.scribe.components.auth.SignOutButton
import com.example.scribe.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, onSignOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "User Name: hi")
        Text(text = "Number of Posts: 10")


        Spacer(modifier = Modifier.height(16.dp))
        SignOutButton(viewModel = viewModel, onSignOut)
    }

}
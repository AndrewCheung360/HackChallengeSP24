package com.example.scribe.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scribe.viewmodel.MainViewModel
import io.github.jan.supabase.SupabaseClient

@Composable
fun SignInScreen(viewModel: MainViewModel, onSignIn: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignInButton(viewModel = viewModel, onSignIn)
    }

}

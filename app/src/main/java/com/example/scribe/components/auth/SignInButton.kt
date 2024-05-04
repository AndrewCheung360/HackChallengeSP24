package com.example.scribe.components.auth


import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.scribe.R
import com.example.scribe.viewmodel.MainViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun GoogleSignInButton(viewModel: MainViewModel, onSignIn : () -> Unit){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        viewModel.signIn(context, coroutineScope, onSignIn)
    }

    Button(onClick = onClick) {
        Text("Sign in with Google")
    }
}

@Composable
fun SignOutButton(viewModel: MainViewModel, onSignOut: () -> Unit){
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        viewModel.signOut(coroutineScope, onSignOut)
    }

    Button(onClick = onClick) {
        Text("Sign out")
    }
}
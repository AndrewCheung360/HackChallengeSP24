package com.example.scribe.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scribe.viewmodel.MainViewModel
import io.github.jan.supabase.SupabaseClient
import androidx.compose.material.Text
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.scribe.R

@Composable
fun SignInScreen(viewModel: MainViewModel, onSignIn: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "Welcome to Scribe",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        Image(painter = painterResource(id = R.drawable.undraw3), contentDescription = "Image", modifier = Modifier.height(300.dp).width(300.dp))

        GoogleSignInButton(viewModel = viewModel, onSignIn)
    }

}

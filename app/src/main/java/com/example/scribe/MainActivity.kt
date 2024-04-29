package com.example.scribe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.scribe.components.GreetingSection
import com.example.scribe.ui.theme.ScribeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name = "John Doe"
            val avatar = "https://pbs.twimg.com/profile_images/1699115602839764992/d3uthjYq_400x400.jpg"

            val navController = rememberNavController()

            ScribeTheme {

                Scaffold (bottomBar = { BottomNavigationBar(navController)}){

                    MainNavigation(navController, name, avatar)

                }

            }
        }
    }
}

package com.example.scribe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.scribe.ui.theme.ScribeTheme
import com.example.scribe.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name = "John Doe"
            val avatar = "https://pbs.twimg.com/profile_images/1699115602839764992/d3uthjYq_400x400.jpg"

            val navController = rememberNavController()
            val mainViewModel = viewModel<MainViewModel>()
            val searchText by mainViewModel.searchText.collectAsState()
            val courses by mainViewModel.searchCourses.collectAsState()



            ScribeTheme {


                Scaffold (bottomBar = { BottomNavigationBar(navController) }){

                    MainNavigation(navController, name, avatar, searchText, courses, mainViewModel)


                }
            }
        }
    }
}




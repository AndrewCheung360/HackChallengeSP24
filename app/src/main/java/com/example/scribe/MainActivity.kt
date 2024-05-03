package com.example.scribe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.scribe.components.GreetingSection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.scribe.components.BottomNavigationBar
import com.example.scribe.components.MainNavigation
import com.example.scribe.components.SearchViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.scribe.ui.theme.ScribeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name = "John Doe"
            val avatar = "https://pbs.twimg.com/profile_images/1699115602839764992/d3uthjYq_400x400.jpg"

            val navController = rememberNavController()
            val searchViewModel = viewModel<SearchViewModel>()
            val searchText by searchViewModel.searchText.collectAsState()
            val courses by searchViewModel.course.collectAsState()



            ScribeTheme {


                Scaffold (bottomBar = { BottomNavigationBar(navController)}){

                    MainNavigation(navController, name, avatar, searchText, courses, searchViewModel)


                }
            }
        }
    }
}




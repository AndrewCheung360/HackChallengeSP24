package com.example.scribe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.scribe.navigation.BottomNavigationBar
import com.example.scribe.navigation.MainNavigation
import com.example.scribe.ui.theme.ScribeTheme
import com.example.scribe.viewmodel.MainViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest




class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val mainViewModel = viewModel<MainViewModel>()
            val supabase = mainViewModel.supabase
            val user by mainViewModel.user.collectAsState()
            val name by mainViewModel.name.collectAsState()
            val avatar by mainViewModel.avatar.collectAsState()

            val navController = rememberNavController()

            val searchText by mainViewModel.searchText.collectAsState()
            val courses by mainViewModel.searchCourses.collectAsState()




            ScribeTheme {




                    MainNavigation(navController, name, avatar, searchText, courses, mainViewModel, supabase)



            }
        }
    }
}






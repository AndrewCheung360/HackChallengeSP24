package com.example.scribe.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.scribe.FilePicker
import com.example.scribe.components.auth.SignInScreen
import com.example.scribe.components.course.CourseNotes
import com.example.scribe.components.home.MainScreen
import com.example.scribe.models.Course
import com.example.scribe.viewmodel.MainViewModel
import com.example.scribe.components.profile.ProfileScreen
import io.github.jan.supabase.SupabaseClient


sealed class  BottomScreen(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {
    object Home : BottomScreen(
        route = "Home",
        icon = Icons.Default.Home,
        label = "Home"
    )

    object Upload : BottomScreen(
        route = "Upload",
        icon = Icons.Default.Add,
        label = "Upload"
    )

    object Profile : BottomScreen(
        route = "Profile",
        icon = Icons.Default.Person,
        label = "Profile"
    )
}

val screenList = listOf(
    BottomScreen.Home,
    BottomScreen.Upload,
    BottomScreen.Profile
)

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(
    navController: NavHostController,
    name: String, avatar: String,
    searchText: String,
    courses: List<Course>,
    mainViewModel: MainViewModel,
    supabase: SupabaseClient
)
{
    val actions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {


        composable("sign_in"){
            SignInScreen(viewModel = mainViewModel, actions.toHome)
        }

        composable(BottomScreen.Home.route) {
            Scaffold (bottomBar = {
                BottomNavigationBar(navController)
             }) {
                MainScreen(
                    name = name,
                    avatar = avatar,
                    searchText = searchText,
                    courses = courses,
                    mainViewModel = mainViewModel,
                    selectedCourse = actions.selectedCourse
                )
            }

        }

        composable(BottomScreen.Upload.route) {
            val coroutineScope = rememberCoroutineScope()
            val mimeTypeFilter = arrayOf("image/jpeg", "image/png", "image/gif", "image/jpg", "application/pdf")

            val selectProfileActivity = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()) { /*...*/ }
            val selectPhotoActivity = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenMultipleDocuments()) { /*...*/ }

            val profileImageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
            val photoImageBitmap = remember { mutableStateListOf<ImageBitmap>() }


            Scaffold (bottomBar = {
                BottomNavigationBar(navController)
            }) {
                Text("Upload")
                FilePicker(
                    coroutineScope = coroutineScope,
                    mimeTypeFilter = mimeTypeFilter,
                    selectProfileActivity = selectProfileActivity,
                    selectPhotoActivity = selectPhotoActivity,
                    profileImageBitmap = profileImageBitmap,
                    photoImageBitmap = photoImageBitmap
                )
            }
        }

        composable(BottomScreen.Profile.route) {
            Scaffold (bottomBar = {
                BottomNavigationBar(navController)
            }) {
                ProfileScreen(viewModel = mainViewModel, actions.toSignIn)
            }
        }

        composable(
            "courses/{courseId}",
            arguments = listOf(
                navArgument("courseId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            CourseNotes(
                courseId = arguments.getInt("courseId"))
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    BottomNavigation {
        screenList.forEach {
            BottomNavigationItem(
                icon = {Icon(it.icon, contentDescription = null)},
                selected = destination?.route == it.route,
                onClick = {
                    navController.navigate(it.route)
                },
                label = {Text(it.label)}
            )
        }
    }
}

private class AppActions(
    navController: NavHostController
) {
    val selectedCourse: (Int) -> Unit = { courseId: Int ->
        navController.navigate("courses/$courseId")
    }

    val toHome : () -> Unit = {
        navController.navigate(BottomScreen.Home.route)
    }

    val toSignIn : () -> Unit = {
        navController.navigate("sign_in")
    }
    val backToHome: () -> Unit = {
        navController.navigate(BottomScreen.Home.route) {
            popUpTo(BottomScreen.Home.route) {
                inclusive = true
            }
        }
    }
}
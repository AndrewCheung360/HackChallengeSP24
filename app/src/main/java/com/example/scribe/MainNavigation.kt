package com.example.scribe

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.scribe.components.course.CourseNotes
import com.example.scribe.components.home.MainScreen
import com.example.scribe.data.Course
import com.example.scribe.viewmodel.MainViewModel
import kotlinx.coroutines.flow.StateFlow

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

@Composable
fun MainNavigation(
    navController: NavHostController,
    name: String, avatar: String,
    searchText: String,
    courses: StateFlow<List<Course>>,
    mainViewModel: MainViewModel
)
{
    val actions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable(BottomScreen.Home.route) {

            MainScreen(name = name, avatar = avatar, searchText = searchText, courses = courses, mainViewModel = mainViewModel, selectedCourse = actions.selectedCourse)


        }

        composable(BottomScreen.Upload.route) {

        }

        composable(BottomScreen.Profile.route) {

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
    val backToHome: () -> Unit = {
        navController.navigate(BottomScreen.Home.route) {
            popUpTo(BottomScreen.Home.route) {
                inclusive = true
            }
        }
    }
}
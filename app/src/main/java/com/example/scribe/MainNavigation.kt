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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
fun MainNavigation(navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(BottomScreen.Home.route) {
            Text("Home")
        }

        composable(BottomScreen.Upload.route) {

        }

        composable(BottomScreen.Profile.route) {

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
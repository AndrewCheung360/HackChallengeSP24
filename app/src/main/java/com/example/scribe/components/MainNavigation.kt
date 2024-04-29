package com.example.scribe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

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


//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                TextField(
//                    value = searchText,
//                    onValueChange = viewModel::searchTextValue,
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = { Text(text = "Search") }
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                ) {
//                    items(courses) { course ->
//                        Text(
//                            text = course.courseName,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 6.dp)
//                        )
//
//                    }
//                }
//
//            }

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
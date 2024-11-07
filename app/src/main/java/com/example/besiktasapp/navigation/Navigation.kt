package com.example.besiktasapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.besiktasapp.screens.MainScreen
import com.example.besiktasapp.screens.NewsScreen
import com.example.besiktasapp.screens.ProfileScreen
import com.example.besiktasapp.screens.SettingsScreen
import com.example.besiktasapp.viewmodels.MainViewModel



sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object News : Screen("news")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { MainScreen(viewModel = viewModel) }
        composable(Screen.News.route) { NewsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}

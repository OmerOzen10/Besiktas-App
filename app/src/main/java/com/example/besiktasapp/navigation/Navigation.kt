package com.example.besiktasapp.navigation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.besiktasapp.MainActivity
import com.example.besiktasapp.screens.MainScreen
import com.example.besiktasapp.screens.NewsScreen
import com.example.besiktasapp.screens.ProfileScreen
import com.example.besiktasapp.screens.SettingsScreen
import com.example.besiktasapp.viewmodels.MainViewModel



sealed class Screen(val route: String) {
    object Home : Screen("home")
    object News : Screen("news")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel // ViewModel'ı buradan geçin
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

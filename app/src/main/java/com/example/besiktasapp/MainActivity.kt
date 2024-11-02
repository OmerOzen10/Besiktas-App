package com.example.besiktasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.besiktasapp.components.BottomNavBar
import com.example.besiktasapp.navigation.SetupNavGraph
import com.example.besiktasapp.screens.MainScreen
import com.example.besiktasapp.ui.theme.BesiktasAppTheme
import com.example.besiktasapp.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BesiktasAppTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { BottomNavBar(navController) }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            SetupNavGraph(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

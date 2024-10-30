package com.example.besiktasapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.besiktasapp.models.Player
import com.example.besiktasapp.sealed.DataState
import com.example.besiktasapp.ui.theme.BesiktasAppTheme
import com.example.besiktasapp.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    data class BottomNavigationItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val hasNews: Boolean,
        val badgeCount: Int? = null,
        val route : String
    )

    sealed class Screen (val route: String){
        object Home : Screen("home")
        object News : Screen("news")
        object Profile : Screen("profile")
        object Settings : Screen("settings")

    }
    @Composable
    fun HomeScreen() {
        Text(text = "Home Page")
    }
    @Composable
    fun NewsScreen() {
        // Content for News Screen
        Text(text = "News Screen")
    }

    @Composable
    fun ProfileScreen() {
        // Content for Profile Screen
        Text(text = "Profile Screen")
    }

    @Composable
    fun SettingsScreen() {
        // Content for Settings Screen
        Text(text = "Settings Screen")
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavigationItem(
                    title = "Ana Sayfa",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon =  Icons.Outlined.Home,
                    hasNews = false,
                    route = Screen.Home.route
                ),
                BottomNavigationItem(
                    title = "Haberler",
                    selectedIcon = Icons.Filled.Notifications,
                    unselectedIcon =  Icons.Outlined.Notifications,
                    hasNews = false,
                    badgeCount = 15,
                    route = Screen.News.route
                ),
                BottomNavigationItem(
                    title = "Profil",
                    selectedIcon = Icons.Filled.AccountCircle,
                    unselectedIcon =  Icons.Outlined.AccountCircle,
                    hasNews = false,
                    route = Screen.Profile.route
                ),
                BottomNavigationItem(
                    title = "Ayarlar",
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon =  Icons.Outlined.Settings,
                    hasNews = true,
                    route = Screen.Settings.route
                ),
            )
            var selectedItemIndex by rememberSaveable {
                mutableStateOf(0)
            }
            Surface (
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                              selectedItemIndex = index
                                        navController.navigate(item.route) {
                                            launchSingleTop = true
                                            }

                                        //navController.navigate(item.title)
                                    },
                                    label = {
                                            Text(text = item.title)
                                    },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Badge {
                                                        Text(text = item.badgeCount.toString())
                                                    }
                                                } else if (item.hasNews){
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                }else item.unselectedIcon ,
                                                contentDescription =  item.title
                                            )
                                        }
                                    })
                            }

                        }
                    }
                ) {innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { HomeScreen() }
                        composable(Screen.News.route) { NewsScreen() }
                        composable(Screen.Profile.route) { ProfileScreen() }
                        composable(Screen.Settings.route) { SettingsScreen() }
                    }

                    val responseState = viewModel.response.value

                    // Main content for the activity
                    Box(modifier = Modifier.fillMaxSize()) {

                        Column {
                            Row {
                                val painter = painterResource(id = R.drawable.besiktas3)
                                val title: String = "BEŞİKTAŞ APP"
                                Strip(painter = painter, contentDescription = "Besiktas", title = title)
                            }

                            // Using LazyVerticalGrid instead of LazyColumn
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2), // 2 columns for side by side layout
                                modifier = Modifier.fillMaxSize()
                            ) {


                                // Show the list of players below the header
                                if (responseState is DataState.Success) {
                                    items(responseState.data) { player ->
                                        CardItem(player)
                                    }
                                }
                            }

                            // Loading and error handling outside the LazyColumn
                            when (responseState) {
                                is DataState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                is DataState.Failure -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = responseState.message,
                                            fontSize = MaterialTheme.typography.headlineMedium.fontSize
                                        )
                                    }
                                }
                                else -> {
                                    // Handle any other states if necessary
                                }
                            }

                        }

                    }
                }
            }
        }
    }

    private @Composable
    fun CardItem(player: Player) {
        val screenHeight = (LocalConfiguration.current.screenHeightDp).dp
        val imageHeight = screenHeight - 80.dp
        val myCustomFont = FontFamily(
            Font(R.font.customfont)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight / 2)
                .padding(4.dp), // Adding some padding between cards
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberImagePainter(player.image),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = 300f
                            )
                        )
                )
                Text(
                    text = player.name ?: "Unknown Player",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    fontFamily = myCustomFont,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
    @Composable
    fun Strip(
        painter: Painter,
        contentDescription: String,
        modifier: Modifier = Modifier,
        title: String
    ){
        Box(modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Black)
        ){

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp),
                    contentScale = ContentScale.Crop
                )

                Box (
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center)
                ){
                    val myCustomFont = FontFamily(
                        Font(R.font.customfont)
                    )
                    Text(
                        title,
                        style = TextStyle(fontFamily = myCustomFont,
                            color = Color.White,
                            fontSize = 36.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    }
}

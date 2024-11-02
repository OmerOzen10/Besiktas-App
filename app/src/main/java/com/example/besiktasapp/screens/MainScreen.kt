package com.example.besiktasapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.besiktasapp.models.Player
import com.example.besiktasapp.sealed.DataState
import com.example.besiktasapp.R
import com.example.besiktasapp.viewmodels.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val responseState = viewModel.response.value

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row {
            val painter = painterResource(id = R.drawable.besiktas3)
            val title: String = "BEŞİKTAŞ APP"
            Strip(painter = painter, contentDescription = "Besiktas", title = title)
        }

        // Grid of Player Cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            if (responseState is DataState.Success) {
                items(responseState.data) { player ->
                    CardItem(player)
                }
            }
        }

        // Loading and Error Handling
        when (responseState) {
            is DataState.Loading -> LoadingIndicator()
            is DataState.Failure -> ErrorMessage(responseState.message)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
    }
}

@Composable
fun CardItem(player: Player) {
    val screenHeight = (LocalConfiguration.current.screenHeightDp).dp
    val imageHeight = screenHeight - 80.dp
    val myCustomFont = FontFamily(Font(R.font.customfont))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight / 2)
            .padding(4.dp),
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
                            colors = listOf(Color.Transparent, Color.Black),
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
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Black)
    ) {
        Row(
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

            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center)
            ) {
                val myCustomFont = FontFamily(Font(R.font.customfont))
                Text(
                    title,
                    style = TextStyle(
                        fontFamily = myCustomFont,
                        color = Color.White,
                        fontSize = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

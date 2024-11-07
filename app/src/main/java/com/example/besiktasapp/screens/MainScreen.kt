package com.example.besiktasapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(player: Player) {
    val screenHeight = (LocalConfiguration.current.screenHeightDp).dp
    val imageHeight = screenHeight - 80.dp
    val myCustomFont = FontFamily(Font(R.font.customfont))
    var isClicked by remember { mutableStateOf(false) }
    val blurAmount by animateDpAsState(
        targetValue = if (isClicked) 16.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )
    val alphaValue by animateFloatAsState(
        targetValue = if (isClicked) 0f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    val cardHeight by animateDpAsState(
        targetValue = if (isClicked) 600.dp else 0.dp, // Increase height when clicked
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight / 2)
            .padding(4.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            isClicked = !isClicked
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image
            Image(
                painter = rememberImagePainter(player.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurAmount)
            )

            // Gradient Box overlay for text visibility
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

            // Player name at the bottom of the image
            Text(
                text = player.name ?: "Unknown Player",
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .alpha(alphaValue),
                fontFamily = myCustomFont,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            // Show additional card with text when clicked
            if (isClicked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "${player.name}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 20.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .padding(bottom = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Pozisyon: ${player.position}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White,
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Uyruk: ${player.nationality}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Doğum Tarihi: ${player.birthDate}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Ayak: ${player.foot}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Boy-Kilo: ${player.body}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Piyasa Değeri: ${player.marketValue}",
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                }
            }

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

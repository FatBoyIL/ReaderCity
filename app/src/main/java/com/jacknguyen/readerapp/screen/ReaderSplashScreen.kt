package com.jacknguyen.readerapp.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jacknguyen.readerapp.R
import com.jacknguyen.readerapp.components.ReaderAppLogo
import com.jacknguyen.readerapp.navigation.ReaderScreen
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }
    val offsetY = remember { Animatable(50f) }
    // Gradient động
    val infiniteTransition = rememberInfiniteTransition(label = "bg_gradient")
    val bgColorTop by infiniteTransition.animateColor(
        initialValue = Color(0xFF00c6ff),
        targetValue = Color(0xFF7F00FF),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bg_top"
    )
    val bgColorBottom by infiniteTransition.animateColor(
        initialValue = Color(0xFF004e92),
        targetValue = Color(0xFF00C9A7),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bg_bottom"
    )

    // Animation cho Text
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(20f) }

    // Chạy animation ban đầu
    LaunchedEffect(true) {
        scale.animateTo(1f, tween(1000))
        offsetY.animateTo(0f, tween(1000))
        textAlpha.animateTo(1f, tween(1000, delayMillis = 500))
        textOffsetY.animateTo(0f, tween(1000, delayMillis = 500))

        delay(3000L)
        // Skip tới homeScreen nếu đã login
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(ReaderScreen.LoginScreen.name) {
                popUpTo(ReaderScreen.SplashScreen.name) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(ReaderScreen.ReaderHomeScreen.name) {
                popUpTo(ReaderScreen.SplashScreen.name) {
                    inclusive = true
                }
            }
        }
    }

    // Giao diện
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(bgColorTop, bgColorBottom)
                    )
                )
        ) {
            // Sóng nền
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.BottomCenter)
            ) {
                val width = size.width
                val height = size.height
                val path = Path().apply {
                    moveTo(0f, height * 0.3f)
                    quadraticTo(width * 0.5f, height * 0.8f, width, height * 0.4f)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(path, brush = Brush.horizontalGradient(
                    listOf(Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.1f))
                ))
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo có scale-in
                Image(
                    painter = painterResource(id = R.drawable.logohuybeo2),
                    contentDescription = "App Logo",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale.value)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .shadow(6.dp, CircleShape)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Tên app với hiệu ứng scale + dịch chuyển
                ReaderAppLogo(modifier = Modifier, textcolor = Color.White)

                Spacer(modifier = Modifier.height(32.dp))

                // Text với hiệu ứng và style đẹp
                Text(
                    text = "Reading journey begins now. Enjoy your new app!",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .offset(y = textOffsetY.value.dp)
                        .alpha(textAlpha.value),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily.SansSerif,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE0E0E0), // Light Silver
                                Color(0xFFC0C0C0), // Classic Silver
                                Color(0xFFDCDCDC)  // Gainsboro
                            )
                        ),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.4f),
                            offset = Offset(1f, 1f),
                            blurRadius = 4f
                        )
                    )
                )




            }
        }
    }
}
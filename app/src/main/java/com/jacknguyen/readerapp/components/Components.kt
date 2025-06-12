package com.jacknguyen.readerapp.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.jacknguyen.readerapp.R
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.navigation.ReaderScreen

@Composable
fun ReaderAppLogo(modifier: Modifier, textcolor: Color) {
    Text(
        text = "Reader City",
        style = MaterialTheme.typography.headlineLarge.copy(
            color = textcolor,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = Color.Gray,
                offset = Offset(2f, 2f),
                blurRadius = 6f
            )
        )
    )
}

@Composable
fun ListCard(
    book: BookReaderApp,
    onPress: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(16.dp)
            .width(200.dp)
            .height(250.dp)
            .clickable { onPress.invoke(book.title.toString()) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        book.photoUrl.toString().replace(
                            "http://",
                            "https://"
                        )),
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color(0x1AFF0000), // đỏ trong suốt
                                shape = CircleShape
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite, // Có thể dùng Filled, Outlined hoặc Sharp
                            contentDescription = "Favorite Icon",
                            tint = Color(0xFFE53935), // đỏ đậm, bắt mắt hơn
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    BookRating(rating = 4.5)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = book.title ?: "Unknown Title",
                fontSize = 20.sp, // to hơn
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp)
            )

            Text(
                text = book.authors ?: "Unknown Author",
                fontSize = 16.sp, // to hơn
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, bottom = 4.dp)
            )


            Spacer(modifier = Modifier.height(12.dp))

            Box(

                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                RoundedButton(
                    label = "Reading",
                )
            }

        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.logohuybeo),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}
@Composable
fun BookRating(rating: Double) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Transparent, // Gradient thay thế màu nền
        shadowElevation = 6.dp,
        modifier = Modifier
            .padding(top = 4.dp)
            .wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFA000), // cam đậm
                            Color(0xFFFFD54F)  // vàng nhạt
                        )
                    ),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format("%.1f", rating),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
@Composable
fun RoundedButton(
    label: String = "Reading",
    modifier: Modifier = Modifier,
    onPress: () -> Unit = {}
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "ButtonScaleAnimation"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(50)) // Viên thuốc
            .clickable(
                onClick = {
                    pressed = true
                    onPress()
                    pressed = false
                }
            ),
        color = Color.Transparent,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(46.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4BBB4E), // xanh lá đậm
                            Color(0xFF4C86D7)  // xanh dương đậm
                        )
                    ),
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}



///Build for input email function
@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun FABContent(onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4FC3F7), // Xanh dương tươi
                        Color(0xFF81C784), // Xanh lá cây sáng
                        Color(0xFF00796B)  // Xanh đậm (teal)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .shadow(10.dp, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = { onTap() },
            shape = CircleShape,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a Book",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(navController: NavController, title: String, showProfile: Boolean = true,icon:ImageVector?=null,onPressedSituationButton:() -> Unit = {}) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Home, // Thay đổi từ Icons.Default.Favorite
                        contentDescription = "Logo Icon",
                        modifier = Modifier
                            .size(32.dp) // Tăng kích thước từ 24.dp thành 32.dp
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2196F3).copy(alpha = 0.2f))
                            .padding(4.dp),
                        tint = Color(0xFF2196F3)
                    )
                }
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Situation Icon",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onPressedSituationButton.invoke()},
                        tint = Color(0xFF2196F3)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color(0xFF2196F3),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreen.LoginScreen.name)
                }
            }) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp, // Thay đổi từ Icons.Default.Home
                        contentDescription = "Logout",
                        modifier = Modifier.size(32.dp), // Tăng kích thước từ 24.dp thành 32.dp
                        tint = Color(0xFF2196F3)
                    )
                }else {
                    //empty icon if showProfile is false
                    Icon(
                        imageVector = Icons.Default.Star, // Hoặc một icon khác nếu cần
                        contentDescription = "Empty Icon",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Transparent // Hoặc một màu khác nếu muốn
                    )
                }
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
@Composable
fun TitleOfBook(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}
//Build for input global function
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),

        )
}
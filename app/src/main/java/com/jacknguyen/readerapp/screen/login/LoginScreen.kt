package com.jacknguyen.readerapp.screen.login

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacknguyen.readerapp.components.EmailInput
import com.jacknguyen.readerapp.navigation.ReaderScreen
import com.jacknguyen.readerapp.components.ReaderAppLogo
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun LoginScreen(navController: NavController,viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm = rememberSaveable{ mutableStateOf(true) }
    // Gradient nền
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF74ebd5),
                        Color(0xFFACB6E5)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
        ) {
            ReaderAppLogo(modifier = Modifier.padding(bottom = 24.dp), textcolor = Color.White)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showLoginForm.value) {
                        UserForm(
                            loading = false,
                            isCreateAccount = false
                        ) {
                          email, password -> viewModel.signInWithEmailAndPassword(email,password){
                              navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                        }

                        }
                    }
                    else{
                        UserForm(loading = false,isCreateAccount = true){
                                email, password -> viewModel.createUserWithEmailAndPassword(email,password){
                                    navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                                }
                        }
                    }
                    Row {
                        val text = if (showLoginForm.value) "Sign Up" else "Login"
                        Text(
                            text = "New User? ",
                            modifier = Modifier
                                .padding(start = 4.dp),

                        )
                        Text(text, modifier = Modifier.clickable {
                            showLoginForm.value = !showLoginForm.value

                        },color = Color(0xFF1565C0),
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
@ExperimentalStdlibApi
@Preview(showBackground = true)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount){
            AnimatedGradientText()

        }
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions(
                onNext = {
                    passwordFocusRequest.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordInput(
            modifier = Modifier
                .focusRequester(passwordFocusRequest),
            labelId = "Password",
            passwordState = password,
            passwordVisible = passwordVisible,
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        SubmitButton(
            textId = if(isCreateAccount) "Create Account" else "Login",
            validInputs = valid,
            loading = loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}
@Composable
fun AnimatedGradientText() {
    var size by remember { mutableStateOf(IntSize.Zero) }

    // Infinite animation for gradient shift
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4000f, // loop range
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "offset"
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFff9a9e), // Hồng nhạt
            Color(0xFFfad0c4), // Cam nhạt
            Color(0xFFfad0c4), // Trùng để tạo hiệu ứng mượt
            Color(0xFFfbc2eb), // Hồng tím
            Color(0xFFa18cd1)  // Tím than
        ),
        start = Offset(animatedOffset, 0f),
        end = Offset(animatedOffset + size.width.toFloat(), size.height.toFloat())
    )

    BasicText(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = gradientBrush,
                    fontSize = androidx.compose.material3.MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append("Hãy nhập email và password ít nhất 6 ký tự")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                size = coordinates.size
            },
        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center
        )
    )
}
@Composable
fun SubmitButton(textId: String,
                 validInputs: Boolean,
                 loading: Boolean,
                 modifier: Modifier,
                 shape: RoundedCornerShape,
                 onClicked: () -> Unit) {
    //make button invincible when loading
Button(onClick = onClicked, enabled = !loading && validInputs) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
    } else {
        Text(text = textId)
    }
}
}


@Composable
fun PasswordInput(
    modifier: Modifier,
    labelId: String,
    imeAction: ImeAction = ImeAction.Done,
    passwordState: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    enabled: Boolean,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisible.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisible = passwordVisible)
        },
        keyboardActions = onAction,
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun PasswordVisibility(passwordVisible: MutableState<Boolean>) {
    val visible = passwordVisible.value
    IconButton(onClick = { passwordVisible.value = !visible }) {
        Icon(
            imageVector = if (visible) Icons.Default.Lock else Icons.Default.Face,
            contentDescription = "Toggle Password Visibility"
        )
    }
}




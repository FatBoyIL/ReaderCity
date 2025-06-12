package com.jacknguyen.readerapp.screen.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.jacknguyen.readerapp.components.InputField
import com.jacknguyen.readerapp.components.RatingBar
import com.jacknguyen.readerapp.components.ReaderAppBar
import com.jacknguyen.readerapp.components.RoundedButton
import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.navigation.ReaderScreen
import com.jacknguyen.readerapp.screen.home.ReaderHomeScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(navController: NavHostController, bookItemId: String,viewmodel: ReaderHomeScreenViewModel= hiltViewModel()) {
    Scaffold(topBar = {
        ReaderAppBar(title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController){
            navController.popBackStack()
        }
    }) {
        paddingValue ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                val bookInfo = produceState<DataOrException<List<BookReaderApp>, Boolean, Exception>>(initialValue = DataOrException(data = emptyList(), true, Exception(""))){
                    value = viewmodel.dataViewModel.value
                }.value

                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                }else {
                  ShowBookUpdate(bookInfo = viewmodel.dataViewModel.value, bookItemId = bookItemId)

                    ShowSimpleForm(book = viewmodel.dataViewModel.value.data?.first{
                            book -> book.googleBookId == bookItemId
                    }!!,navController = navController)
                }
            }
        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun ShowSimpleForm(book: BookReaderApp,
                   navController: NavHostController) {

    val isFinishedReading = remember {
        mutableStateOf(false)
    }
    val ratingVal = remember {
        mutableStateOf(book.rating?.toInt() ?: 0)
    }
    val isStartedReading = remember {
        mutableStateOf(false)
    }
    val notesText = remember {
        mutableStateOf(book.notes ?: "")
    }
    SimpleForm(
        textState = notesText
    )
    Row(modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        TextButton(onClick = { isStartedReading.value = true},enabled = book.finishedReading == null) {
            if (book.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }

            }

        }


        Spacer(modifier = Modifier.height(4.dp))

        TextButton(onClick = { isFinishedReading.value = true },
            enabled = book.finishedReading == null) {
            if (book.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                }else {
                    Text(text = "Finished Reading!")
                }
            }else {
                Text(text = "Finished on: ${book.finishedReading!!}")
            }

        }

    }
    Text("Rating:")
    book.rating?.toInt().let {
        RatingBar(rating = ratingVal.value) {
            rating -> ratingVal.value = rating
            Log.d("BookUpdateScreen", "Rating Value: $rating")
        }
    }
    Row {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading
        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value
        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value).toMap()
        RoundedButton(label = "Update"){
            if (bookUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        task ->
                        android.widget.Toast.makeText(
                            navController.context,
                            "Book Updated Successfully!",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                         Log.d("Update", "ShowSimpleForm: ${task.result.toString()}")
                    }.addOnFailureListener{
                        Log.w("Error", "Error updating document" , it)
                    }
            }
        }
        Spacer(modifier = Modifier.width(50.dp))
        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value){
            ShowAlertDialog(
                onDismiss = { openDialog.value = false },
                onConfirm = {
                    FirebaseFirestore.getInstance()
                        .collection("books")
                        .document(book.id!!)
                        .delete()
                        .addOnCompleteListener {
                            android.widget.Toast.makeText(
                                navController.context,
                                "Book Deleted Successfully!",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                        }.addOnFailureListener {
                            Log.w("Error", "Error deleting document", it)
                        }
                }
            )
        }
        RoundedButton("Delete"){
            openDialog.value = true

        }
    }

    }

@Composable
fun ShowAlertDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .animateContentSize(),
        title = {
            Text(
                text = "Delete Book",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this book? This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .scale(1f)
                    .animateContentSize()
            ) {
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.animateContentSize()
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}

@ExperimentalComposeUiApi
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    textState: androidx.compose.runtime.MutableState<String>
){
    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textState.value) { textState.value.trim().isNotEmpty() }
        InputField(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textState,
            labelId = "Enter Your thoughts",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid)return@KeyboardActions
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<BookReaderApp>,
        Boolean, Exception>, bookItemId: String) {
    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null) {
            Column(modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                CardListItem(book = bookInfo.data!!.first{mBook ->
                    Log.d("BookUpdateScreen", "Book ID: ${mBook.googleBookId}, Book Item ID: $bookItemId")
                    mBook.googleBookId == bookItemId

                }, onPressDetails = {})

            }
        }

    }

}

@Composable
fun CardListItem(book: BookReaderApp, onPressDetails: () -> Unit) {
    Card(modifier = Modifier
        .padding(
            start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp
        )
        .clip(RoundedCornerShape(20.dp))
        .clickable { },
        ) {
        Row(horizontalArrangement = Arrangement.Start) {
            val imageUrl = book.photoUrl.toString()
                .replace("http://", "https://")
                .ifEmpty { "https://res.cloudinary.com/dlty5lwzh/image/upload/v1748810622/cld-sample-5.jpg" }
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null ,
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    ))
            Column {
                Text(text = book.title.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

                Text(text = book.authors.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 0.dp))

                Text(text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 8.dp))

            }

        }




    }
}

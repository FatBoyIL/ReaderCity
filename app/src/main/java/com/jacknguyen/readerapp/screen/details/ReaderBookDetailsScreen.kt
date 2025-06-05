package com.jacknguyen.readerapp.screen.details

import android.annotation.SuppressLint
import android.text.Html
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacknguyen.readerapp.R
import com.jacknguyen.readerapp.components.ReaderAppBar
import com.jacknguyen.readerapp.data.Resource
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.navigation.ReaderScreen
import com.jacknguyen.readerapp.repository.BookRepository
import javax.inject.Inject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderBookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: ReaderBookDetailsViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.navigate(ReaderScreen.SearchScreen.name)
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInformation =
                    produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                        value = viewModel.getBookDetails(bookId)
                    }.value
                if (bookInformation.data == null) {
                    CircularProgressIndicator()
                } else {
                    ShowBookDetails(bookInformation.data,navController)


                }
                Log.d("BookDetails", "Book ID: $bookId")
            }


        }
    }
}

@Composable
//data = bookInformation.data
private fun ShowBookDetails(data: Item,navController: NavController) {
    // Animation for card entrance
    val animatedAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedAlpha.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .alpha(animatedAlpha.value)
            .scale(animateFloatAsState(if (animatedAlpha.value == 1f) 1f else 0.95f).value),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                // Book Image and Title Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Book Image
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                shape = CircleShape
                            )
                            .animateContentSize(),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        val imageUrl = data.volumeInfo.imageLinks.thumbnail
                            .replace("http://", "https://")
                            .ifEmpty { "https://res.cloudinary.com/dlty5lwzh/image/upload/v1748810622/cld-sample-5.jpg" }
                            ?: "https://res.cloudinary.com/dlty5lwzh/image/upload/v1748810622/cld-sample-5.jpg"

                        Image(
                            painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription = "Book Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Book Title with Gradient
                    Text(
                        text = data.volumeInfo.title ?: "No Title",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            item {
                // Authors with Icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Authors",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = data.volumeInfo.authors?.joinToString(", ") ?: "No Authors",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            item {
                val formattedDate = try {
                    val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    data.volumeInfo.publishedDate?.let { dateStr ->
                        outputFormat.format(inputFormat.parse(dateStr) ?: return@let "Unknown")
                    } ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }
                // Publish Date with Chip-like style
                Surface(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = formattedDate,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                // Categories with individual Chip-like style, max 2 per row
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 2
                ) {
                    val categories = data.volumeInfo.categories
                        ?.joinToString(", ")
                        ?.split("/")
                        ?.map { it.trim() }
                        ?.filter { it.isNotEmpty() }
                        ?: listOf("No Category")

                    categories.forEach { category ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .clickable { Log.d("Category Clicked", category) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Page Count
                Text(
                    text = "Pages: ${data.volumeInfo.pageCount ?: "Unknown"}",
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                // Description with Expandable Text and Buttons
                var expanded by remember { mutableStateOf(false) }
                val description = Html.fromHtml(
                    data.volumeInfo.description ?: "No Description",
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()

                Column {
                    Text(
                        text = description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (expanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (description.length > 100) {
                        Text(
                            text = if (expanded) "Show Less" else "Show More",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalIndication.current
                                ) { expanded = !expanded }
                                .padding(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    // Save and Cancel Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {

                            },
                            modifier = Modifier
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Cancel",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp
                            )
                        }
                        Button(
                            onClick = {
                                val dataFB = FirebaseFirestore.getInstance()
                                val book = BookReaderApp(
                                    title = data.volumeInfo.title,
                                    authors = data.volumeInfo.authors.toString(),
                                    notes = "",
                                    photoUrl = data.volumeInfo.imageLinks.thumbnail,
                                    categories = data.volumeInfo.categories.toString(),
                                    publishedDate = data.volumeInfo.publishedDate,
                                    pageCount = data.volumeInfo.pageCount.toString(),
                                    rating = 0.0,
                                    description = data.volumeInfo.description,
                                    googleBookId = data.id,
                                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

                                )
                                SaveToFirebase(book, navController )
                            },
                            modifier = Modifier,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Save",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

fun SaveToFirebase(book: BookReaderApp,navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    if (db.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                dbCollection.document(documentId).update("id", documentId)
                    .addOnCompleteListener {
                        navController.popBackStack()
                        Log.d(
                            "FirebaseHUY",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                    }
                    .addOnFailureListener {
                        Log.w("FirebaseHUY", "Error updating document", it)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firebase", "Error adding document", e)
                    }
            }
    }
}

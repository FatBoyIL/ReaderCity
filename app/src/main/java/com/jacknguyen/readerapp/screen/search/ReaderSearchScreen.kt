package com.jacknguyen.readerapp.screen.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.query
import coil3.compose.rememberAsyncImagePainter
import com.jacknguyen.readerapp.components.InputField
import com.jacknguyen.readerapp.components.ReaderAppBar
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.navigation.ReaderScreen
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderSearchScreen(
    navController: NavController,viewModelSearch: ReaderSearchViewModel = hiltViewModel()
)
{
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Searching Your Book",
            navController = navController,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            showProfile = false
        ){
//            //onPress will active this
//            navController.navigate(ReaderScreen.ReaderHomeScreen.name)
            navController.popBackStack()

        }
    }){
         paddingValue ->
            Column(modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()) {
                SearchForm(modifier = Modifier.padding(16.dp),
                    hint = "Search for books, authors, genres...", viewModel = viewModelSearch,){
                    query -> viewModelSearch.searchBooks(query)
                }
                Spacer(modifier = Modifier.height(16.dp))
                BookSearchList(navController = navController,viewModelSearch)
            }


    }

}

@Composable
fun BookSearchList(navController: NavController,viewModel :ReaderSearchViewModel = hiltViewModel()) {
    val listOfBooks = viewModel.listOfBooks
    LazyColumn (modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp) ){
        items(listOfBooks){
            book -> BookRow(book,navController)

        }
    }

}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {


            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh bìa sách
            val imgUrl = book.volumeInfo.imageLinks.smallThumbnail.replace("http://", "https://").ifEmpty { "https://res.cloudinary.com/dlty5lwzh/image/upload/v1748810622/cld-sample-5.jpg" }
            Log.d("BookRow", "Image URL: $imgUrl")
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imgUrl),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin sách
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.volumeInfo.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Author: ${book.volumeInfo.authors ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go to Details",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    viewModel: ReaderSearchViewModel,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isValid = remember(searchQuery.value) {
        searchQuery.value.trim().isNotEmpty()
    }

    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(0.95f)
            .height(56.dp)
            .clip(RoundedCornerShape(50)),
        placeholder = {
            Text(
                text = hint,
                color = Color.Gray,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp),
                    strokeWidth = 2.dp
                )
            } else if (searchQuery.value.isNotEmpty()) {
                IconButton(onClick = {
                    searchQuery.value = ""// Clear search results when clearing the query
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Clear",)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        keyboardActions = KeyboardActions(
            onDone = {
                if (!isValid) return@KeyboardActions
                onSearch(searchQuery.value.trim())
                searchQuery.value = ""
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray
        )
    )
}





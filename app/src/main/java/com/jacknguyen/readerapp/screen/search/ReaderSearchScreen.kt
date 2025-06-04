package com.jacknguyen.readerapp.screen.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
            Column(modifier = Modifier.padding(paddingValue).fillMaxSize()) {
                SearchForm(modifier = Modifier.padding(16.dp),
                    hint = "Search for books, authors, genres...", viewModel = viewModelSearch,){
                    query -> viewModelSearch.searchBooks(query)
                }
                Spacer(modifier = Modifier.height(16.dp))
                BookSearchList(navController = navController)
            }


    }

}

@Composable
fun BookSearchList(navController: NavController) {
    val listOfBooks = listOf(
        BookReaderApp(
            id = "1",
            title = "The Great Gatsby",
            authors = "F. Scott Fitzgerald",
            notes = "A classic novel set in the 1920s."
        ),
        BookReaderApp(
            id = "2",
            title = "To Kill a Mockingbird",
            authors = "Harper Lee",
            notes = "A novel about racial injustice in the Deep South."
        ),
        BookReaderApp(
            id = "3",
            title = "1984",
            authors = "George Orwell",
            notes = "A dystopian novel about totalitarianism and surveillance."
        ),
        BookReaderApp(
            id = "4",
            title = "Pride and Prejudice",
            authors = "Jane Austen",
            notes = "A romantic novel that critiques the British landed gentry."
        ),
        //create 10 more fake data
        BookReaderApp(
            id = "5",
            title = "Pride and Prejudice",
            authors = "Jane Austen",
            notes = "A romantic novel that critiques the British landed gentry."
        ),
        BookReaderApp(
            id = "6",
            title = "The Catcher in the Rye",
            authors = "J.D. Salinger",
            notes = "A novel about teenage angst and alienation."
        ),
        BookReaderApp(
            id = "7",
            title = "The Lord of the Rings",
            authors = "J.R.R. Tolkien",
            notes = "An epic fantasy novel about the quest to destroy a powerful ring."),
        BookReaderApp(
            id = "8",
            title = "Pride2 and Prejudice1",
            authors = "Jane2 Austen",
            notes = "A romantic novel thats1 critiques the British landed gentry."
        ),
        BookReaderApp(
            id = "9",
            title = "The Hobbit",
            authors = "J.R.R. Tolkien",
            notes = "A fantasy novel about the adventures of Bilbo Baggins.")

        )
    LazyColumn (modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp) ){
        items(listOfBooks){
            book -> BookRow(book,navController)

        }
    }

}

@Composable
fun BookRow(book: BookReaderApp, navController: NavController) {
    Card(modifier = Modifier.clickable {  }.fillMaxWidth(), shape = RectangleShape) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imgUrl = "https://res.cloudinary.com/dlty5lwzh/image/upload/v1748810622/cld-sample-5.jpg"
            Image(painter = rememberAsyncImagePainter(model = imgUrl),contentDescription = "Book Cover",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)

                    ))
            Spacer(modifier = Modifier.width(16.dp))
           Column {
               Text(text = book.title.toString(), overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,)
                Text(text = "Author: ${book.authors}", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
           }
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





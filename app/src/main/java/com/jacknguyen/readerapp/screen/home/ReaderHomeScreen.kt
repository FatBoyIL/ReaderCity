package com.jacknguyen.readerapp.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jacknguyen.readerapp.components.FABContent
import com.jacknguyen.readerapp.components.ListCard
import com.jacknguyen.readerapp.components.ReaderAppBar
import com.jacknguyen.readerapp.components.TitleOfBook
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.navigation.ReaderScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavController, viewModel: ReaderHomeScreenViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { ReaderAppBar(title = "Reader City", navController = navController) },
        floatingActionButton = { FABContent { navController.navigate(ReaderScreen.SearchScreen.name)} },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            HomeContent(navController, viewModel)
        }
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: ReaderHomeScreenViewModel) {
    //fake data BookReaderApp
    var listOfBooks = emptyList<BookReaderApp>()
    val userName = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "Guest"
    val userData = FirebaseAuth.getInstance().currentUser

    if (!viewModel.dataViewModel.value.data.isNullOrEmpty()){
        listOfBooks = viewModel.dataViewModel.value.data!!.toList().filter{
            mBook ->
            mBook.userId == userData?.uid.toString()
        }
        Log.d("contetn", "listOfBooks: $listOfBooks")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleOfBook(title = "Reading Books")

            Card(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(ReaderScreen.ReaderStatsScreen.name)
                            },
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3)),
                                            start = Offset.Zero,
                                            end = Offset.Infinite
                                        )
                                    )
                                    .border(2.dp, Color(0xFF1976D2), CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile Icon",
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = userName,
                                fontSize = 18.sp,
                                color = Color(0xFF2196F3),
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }

        Spacer(modifier = Modifier.height(16.dp))
        ReadingRightNowArea(listOfBooks = listOfBooks,
            navController =navController )
        TitleOfBook(title = "Reading List")
        BookListArea(listOfBooks = listOfBooks,
            navController = navController)
//        BookListArea(
//            listOfBooks = listOfBooks,
//            navController = navController
//        )
    }
}

@Composable
fun ReadingRightNowArea(listOfBooks: List<BookReaderApp>,
                        navController: NavController) {

    BookListArea(listOfBooks= listOfBooks,navController = navController)

}

@Composable
fun BookListArea(listOfBooks: List<BookReaderApp>, navController: NavController) {
    Log.d("BookListArea", "listOfBooks: $listOfBooks")
    HorizontalScrollableComponent(
        listOfBooks
    ){
        navController.navigate(
            ReaderScreen.UpdateScreen.name + "/${it}"
        )
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<BookReaderApp>,
                                  onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
            .horizontalScroll(scrollState),
    ) {
        for (book in listOfBooks) {
            ListCard(book = book){
                onCardPressed(book.googleBookId.toString())
            }
        }
    }
}













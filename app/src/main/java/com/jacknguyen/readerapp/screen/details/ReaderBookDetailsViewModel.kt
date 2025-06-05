package com.jacknguyen.readerapp.screen.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknguyen.readerapp.data.Resource
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderBookDetailsViewModel @Inject constructor(private val bookRepository: BookRepository) : ViewModel() {

    suspend fun getBookDetails(bookId: String): Resource<Item> {
        return bookRepository.getBookInfo(bookId)
    }
}

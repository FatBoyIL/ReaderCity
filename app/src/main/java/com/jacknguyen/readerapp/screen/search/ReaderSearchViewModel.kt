package com.jacknguyen.readerapp.screen.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.data.Resource
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderSearchViewModel @Inject constructor(private val bookRepository: BookRepository):ViewModel() {
    var listOfBooks : List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)


    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("flutter")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()){
                return@launch
            }
            try {
                //when like a switch case
                when(val response = bookRepository.getAllBooks(query)) {
                    is Resource.Success -> {
                        listOfBooks = response.data!!
                        if (listOfBooks.isNotEmpty()){
                            isLoading = false
                        }
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "searchBooks: Failed getting books", )
                    }
                    else -> {isLoading = false}
                }

            }catch (exception: Exception){
                isLoading = false
                Log.d("Network", "searchBooks: ${exception.message.toString()}")
            }

        }


    }

}
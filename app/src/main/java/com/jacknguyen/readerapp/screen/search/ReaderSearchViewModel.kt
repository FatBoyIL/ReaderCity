package com.jacknguyen.readerapp.screen.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderSearchViewModel @Inject constructor(private val bookRepository: BookRepository):ViewModel() {
    private val listOfBooks : MutableState<DataOrException<List<Item>, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init{
        searchBooks("vàng")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch() {
            if(query.isEmpty()){
                return@launch
            }
            listOfBooks.value.loading = true
            listOfBooks.value = bookRepository.getAllBooks(query)
            Log.d("DATASS", "searchBooks: ${listOfBooks.value.data.toString()}")
            if (listOfBooks.value.data.toString().isNotEmpty()) {
                listOfBooks.value.loading = false
            } else {
                listOfBooks.value.e = Exception("No Books Found")
            }
        }
    }

}
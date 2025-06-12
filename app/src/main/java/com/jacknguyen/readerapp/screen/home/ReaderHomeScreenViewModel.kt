package com.jacknguyen.readerapp.screen.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.model.AccessInfo
import com.jacknguyen.readerapp.model.BookReaderApp
import com.jacknguyen.readerapp.repository.BookRepository
import com.jacknguyen.readerapp.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderHomeScreenViewModel @Inject constructor(private val repository: FireRepository):ViewModel() {
     val dataViewModel : MutableState<DataOrException<List<BookReaderApp>, Boolean, Exception>> = mutableStateOf(
        DataOrException(
            data = listOf(),
            loading = true,
            e = Exception("")
        ))
    init {
        getAllBooksFromFireBaseDB()
    }

    private fun getAllBooksFromFireBaseDB() {
        viewModelScope.launch {
            dataViewModel.value.loading = true
            dataViewModel.value = repository.getAllBooksFromFireBase()
            if (dataViewModel.value.data.isNullOrEmpty()) {
                dataViewModel.value.loading = false
            }
            Log.d("aaa", "getAllBooksFromFireBaseDB: ${dataViewModel.value.data?.toList().toString()}")
        }
    }
}
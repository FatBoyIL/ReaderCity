package com.jacknguyen.readerapp.repository

import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.model.BookAPI
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.network.BookApiInterface
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BookApiInterface) {

    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    suspend fun getAllBooks(query: String): DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(query).items
            if(dataOrException.data!!.isNotEmpty()) {
                dataOrException.loading = false
            }

        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {

        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
           if (bookInfoDataOrException.data.toString().isNotEmpty()) {
                bookInfoDataOrException.loading = false
            }
        } catch (e: Exception) {
            bookInfoDataOrException.e = e
        }
        return bookInfoDataOrException
    }

}
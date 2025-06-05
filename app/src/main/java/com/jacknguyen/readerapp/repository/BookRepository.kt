package com.jacknguyen.readerapp.repository

import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.data.Resource
import com.jacknguyen.readerapp.model.BookAPI
import com.jacknguyen.readerapp.model.Item
import com.jacknguyen.readerapp.network.BookApiInterface
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BookApiInterface) {


    suspend fun getAllBooks(query: String): Resource<List<Item>> {
      return try {
            Resource.Loading(true)
            val itemList = api.getAllBooks(query).items
            if (itemList.isNotEmpty()) {
                Resource.Loading(false)
            }
            Resource.Success(itemList)

        } catch (e: Exception) {
            Resource.Error("Error fetching books: ${e.message.toString()}")
        }

    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {

       val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)
        } catch (e: Exception) {
           return  Resource.Error("Error fetching book info: ${e.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(response)
    }

}
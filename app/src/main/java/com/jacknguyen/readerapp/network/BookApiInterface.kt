package com.jacknguyen.readerapp.network

import com.jacknguyen.readerapp.model.BookAPI
import com.jacknguyen.readerapp.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApiInterface {
    //Example URL = https://www.googleapis.com/books/v1/volumes?q=flutter
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): BookAPI

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String):Item
}
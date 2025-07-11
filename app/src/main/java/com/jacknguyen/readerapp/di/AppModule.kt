package com.jacknguyen.readerapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.jacknguyen.readerapp.network.BookApiInterface
import com.jacknguyen.readerapp.repository.BookRepository
import com.jacknguyen.readerapp.repository.FireRepository
import com.jacknguyen.readerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provides the BookRepository and BookApiInterface as singletons ensure they using 1 api
    @Singleton
    @Provides
    fun provideFireBookRepository()= FireRepository(
        query = FirebaseFirestore.getInstance().collection("books")
    )
    @Singleton
    @Provides
    fun provideBookApiInterface():BookApiInterface {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiInterface::class.java)
    }
}
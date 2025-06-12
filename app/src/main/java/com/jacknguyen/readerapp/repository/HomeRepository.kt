package com.jacknguyen.readerapp.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.jacknguyen.readerapp.data.DataOrException
import com.jacknguyen.readerapp.model.BookReaderApp
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val query : Query) {
    suspend fun getAllBooksFromFireBase () : DataOrException<List<BookReaderApp>, Boolean, Exception> {
        val dataOrException = DataOrException<List<BookReaderApp>, Boolean, Exception>()
        try {

            dataOrException.loading = true
            dataOrException.data= query.get().await().map {
                queryDocumentSnapshot ->
                queryDocumentSnapshot.toObject(BookReaderApp::class.java)
            }
            if (dataOrException.data.isNullOrEmpty()) {
                dataOrException.loading = false
            }

        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
            dataOrException.loading = false
        }
        return dataOrException
    }
}

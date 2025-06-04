package com.jacknguyen.readerapp.model

data class BookAPI(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)
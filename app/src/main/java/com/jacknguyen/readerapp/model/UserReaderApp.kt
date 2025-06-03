package com.jacknguyen.readerapp.model

data class UserReaderApp(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String
) {
    fun toMap(): Map<String, Any> {
        return mutableMapOf(
            // Các trường trong "user" phải giống trên dataBase
            "userId" to this.userId,
            "user_name" to this.displayName,
            "avatar_url" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession
        )

    }
}
package com.aqeel.bookfinder.repo

import com.aqeel.bookfinder.api.BooksApi
import com.aqeel.bookfinder.api.RetrofitClient
import com.aqeel.bookfinder.model.BookResponse
import retrofit2.Response

class BookRepository(
    private val api: BooksApi = RetrofitClient.api
) {
    suspend fun getBooks(
        query: String,
        startIndex: Int,
        maxResults: Int
    ): Response<BookResponse> {
        return api.searchBooks(
            query = query,
            startIndex = startIndex,
            maxResults = maxResults
        )
    }
}

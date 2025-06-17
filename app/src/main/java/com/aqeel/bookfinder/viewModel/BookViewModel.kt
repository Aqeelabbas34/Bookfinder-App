package com.aqeel.bookfinder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aqeel.bookfinder.model.BookItem
import com.aqeel.bookfinder.model.BookResponse
import com.aqeel.bookfinder.repo.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _books = MutableLiveData<List<BookItem>>(emptyList())
    val books: LiveData<List<BookItem>> = _books

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _noResults = MutableLiveData(false)
    val noResults: LiveData<Boolean> = _noResults

    // --- NEW featured list ---
    private val _featured = MutableLiveData<List<BookItem>>(emptyList())
    val featured: LiveData<List<BookItem>> = _featured


    // pagination state
    private var currentQuery = ""
    private var currentPage = 0
    private val pageSize = 10
    private var isLastPage = false
    private val currentList = mutableListOf<BookItem>()

    fun loadFeatured() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // e.g. using “android” as featured query, or replace with your pick
                val resp = repository.getBooks(
                    query = "android",
                    startIndex = 0,
                    maxResults = pageSize
                )
                if (resp.isSuccessful) {
                    _featured.value = resp.body()?.items.orEmpty()
                }
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    /** Start a fresh search */
    fun search(query: String) {
        currentQuery = query
        currentPage = 0
        isLastPage = false
        currentList.clear()
        _books.value = emptyList()
        loadNextPage()
    }

    /** Fetch next page from the repository */
    fun loadNextPage() {
        if (isLastPage) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getBooks(
                    query = currentQuery,
                    startIndex = currentPage * pageSize,
                    maxResults = pageSize
                )

                if (response.isSuccessful) {
                    // safely unwrap BookResponse and its items list
                    val bookResponse: BookResponse? = response.body()
                    val newItems: List<BookItem> = bookResponse?.items.orEmpty()

                    if (currentPage == 0 && newItems.isEmpty()) {
                        // no results on first page
                        _noResults.value = true
                    } else {
                        _noResults.value = false
                        currentList.addAll(newItems)
                        _books.value = currentList.toList()
                        currentPage++

                        // if fewer than pageSize returned → last page
                        if (newItems.size < pageSize) {
                            isLastPage = true
                        }
                    }
                }
            } catch (e: Exception) {

            } finally {
                _loading.value = false
            }
        }
    }
}

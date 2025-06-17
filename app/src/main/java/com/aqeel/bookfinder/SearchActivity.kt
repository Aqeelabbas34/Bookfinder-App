package com.aqeel.bookfinder

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.TextView
import com.aqeel.bookfinder.adapter.BookAdapter
import com.aqeel.bookfinder.viewModel.BookViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: BookAdapter
    private val viewModel: BookViewModel by viewModels()

    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.svSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.rvSearchResults)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarSearch)
        val noResultsText = findViewById<TextView>(R.id.tvNoResultsSearch)

        // 1) Set up adapter with empty list
        adapter = BookAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 2) Observe LiveData but do NOT trigger search until user submits
        viewModel.books.observe(this) { list ->
            adapter.update(list)
        }
        viewModel.loading.observe(this) { loading ->
            isLoading = loading
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.noResults.observe(this) { noResults ->
            noResultsText.visibility = if (noResults) View.VISIBLE else View.GONE
        }

        // 3) Pagination on scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (!rv.canScrollVertically(1) && !isLoading && !isLastPage) {
                    viewModel.loadNextPage()
                }
            }
        })

        // 4) Handle user search submissions
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotBlank()) {
                    // reset pagination state
                    isLastPage = false
                    adapter.update(emptyList())
                    viewModel.search(query)
                    searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(newText: String) = false
        })

        // 5) Ensure the SearchView is empty and focused correctly
        searchView.setQuery("", false)
        noResultsText.visibility = View.GONE
    }
}

package com.aqeel.bookfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqeel.bookfinder.adapter.BookAdapter
import com.aqeel.bookfinder.viewModel.BookViewModel
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private lateinit var featuredAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // — setup featured RecyclerView —
        featuredAdapter = BookAdapter(mutableListOf())
        val rvFeatured = findViewById<RecyclerView>(R.id.rvFeatured)
        rvFeatured.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFeatured.adapter = featuredAdapter

        // observe featured list
        viewModel.featured.observe(this) { list ->
            featuredAdapter.update(list)
        }

        // load featured on startup
        viewModel.loadFeatured()

        // open SearchActivity
        findViewById<Button>(R.id.btnSearchNavigate).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }
}

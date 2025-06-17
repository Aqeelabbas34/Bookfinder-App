package com.aqeel.bookfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aqeel.bookfinder.R
import com.aqeel.bookfinder.model.BookItem
import com.bumptech.glide.Glide

class BookAdapter(private val items: MutableList<BookItem>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView  = view.findViewById(R.id.tvTitle)
        val author: TextView = view.findViewById(R.id.tvAuthor)
        val desc: TextView   = view.findViewById(R.id.tvDescription)
        val image: ImageView = view.findViewById(R.id.ivThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = items[position]
        holder.title.text  = book.volumeInfo.title
        holder.author.text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown"
        holder.desc.text   = book.volumeInfo.description ?: "No Description"
        Glide.with(holder.itemView)
            .load(book.volumeInfo.imageLinks?.thumbnail)
            .into(holder.image)
    }

    fun update(newList: List<BookItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}

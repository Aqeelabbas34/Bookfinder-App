package com.aqeel.bookfinder.model

data class BookResponse(val items: List<BookItem>?)

data class BookItem(val volumeInfo: VolumeInfo)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(val thumbnail: String?)

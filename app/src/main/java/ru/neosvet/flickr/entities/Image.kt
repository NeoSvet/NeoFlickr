package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image")
data class ImageItem(
    @PrimaryKey val url: String,
    val path: String
)
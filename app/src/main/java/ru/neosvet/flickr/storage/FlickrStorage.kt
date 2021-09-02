package ru.neosvet.flickr.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.neosvet.flickr.entities.GalleryItem
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.entities.InfoItem
import ru.neosvet.flickr.entities.PhotoItem
import java.lang.IllegalStateException

@androidx.room.Database(
    entities = [
        GalleryItem::class,
        PhotoItem::class,
        InfoItem::class,
        ImageItem::class
    ],
    version = 1
)
abstract class FlickrStorage : RoomDatabase() {
    abstract val galleryDao: GalleryDao
    abstract val photoDao: PhotoDao
    abstract val infoDao: InfoDao
    abstract val imageDao: ImageDao

    companion object {
        private const val DB_NAME = "database.db"
        private var instance: FlickrStorage? = null
        fun getInstance() = instance ?: throw IllegalStateException("Database has not been created")
        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, FlickrStorage::class.java, DB_NAME).build()
            }
        }
    }
}
package ru.neosvet.flickr.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.neosvet.flickr.entities.GalleryItem
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.entities.InfoItem
import ru.neosvet.flickr.entities.PhotoItem

@androidx.room.Database(
    entities = [
        GalleryItem::class,
        PhotoItem::class,
        InfoItem::class,
        ImageItem::class
    ],
    version = 2
)
abstract class FlickrStorage : RoomDatabase() {
    abstract val galleryDao: GalleryDao
    abstract val photoDao: PhotoDao
    abstract val infoDao: InfoDao
    abstract val imageDao: ImageDao

    companion object {
        private const val DB_NAME = "database.db"

        fun get(context: Context) =
            Room.databaseBuilder(context, FlickrStorage::class.java, DB_NAME)
                .addMigrations(Storage1to2Migration)
                .build()
    }

}
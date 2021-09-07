package ru.neosvet.flickr.storage

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Storage1to2Migration : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE InfoItem")
        database.execSQL("ALTER TABLE PhotoItem RENAME TO Photo")
        database.execSQL("CREATE TABLE Info(photoId TEXT NOT NULL PRIMARY KEY, owner TEXT NOT NULL, date TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, FOREIGN KEY(`photoId`) REFERENCES `Photo`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
        database.execSQL("ALTER TABLE GalleryItem RENAME TO Gallery")
        database.execSQL("ALTER TABLE ImageItem RENAME TO Image")
    }
}
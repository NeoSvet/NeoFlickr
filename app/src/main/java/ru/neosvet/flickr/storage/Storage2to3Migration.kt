package ru.neosvet.flickr.storage

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Storage2to3Migration : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Image ADD COLUMN uri TEXT")
    }
}
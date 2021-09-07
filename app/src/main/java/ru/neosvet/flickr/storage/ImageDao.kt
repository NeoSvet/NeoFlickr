package ru.neosvet.flickr.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.ImageItem

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageItem): Completable

    @Update
    fun update(image: ImageItem): Completable

    @Delete
    fun delete(image: ImageItem): Completable

    @Query("SELECT * FROM Image")
    fun getAll(): Single<List<ImageItem>>

    @Query("SELECT * FROM Image WHERE url = :url")
    fun get(url: String): Single<ImageItem>
}
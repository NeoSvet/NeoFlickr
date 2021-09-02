package ru.neosvet.flickr.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.GalleryItem

@Dao
interface GalleryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gallery: GalleryItem): Completable

    @Update
    fun update(gallery: GalleryItem): Completable

    @Delete
    fun delete(gallery: GalleryItem): Completable

    @Query("SELECT * FROM GalleryItem")
    fun getAll(): Single<List<GalleryItem>>

    @Query("SELECT * FROM GalleryItem WHERE name = :name AND page = :page")
    fun get(name: String, page: Int): Observable<GalleryItem>
}
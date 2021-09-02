package ru.neosvet.flickr.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.PhotoItem

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photo: PhotoItem): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photos: List<PhotoItem>): Completable

    @Query("UPDATE PhotoItem SET urlBig = :urlBig WHERE id=:photoId")
    fun updateUrlBig(photoId: String, urlBig: String): Completable

    @Update
    fun update(photo: PhotoItem): Completable

    @Delete
    fun delete(photo: PhotoItem): Completable

    @Query("SELECT * FROM PhotoItem")
    fun getAll(): Single<List<PhotoItem>>

    @Query("SELECT * FROM PhotoItem WHERE id = :id LIMIT 1")
    fun get(id: String): Single<PhotoItem>

    @Query("SELECT * FROM PhotoItem WHERE id IN (:ids)")
    fun get(ids: List<String>): Observable<List<PhotoItem>>
}
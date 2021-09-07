package ru.neosvet.flickr.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.InfoItem

@Dao
interface InfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(info: InfoItem): Completable

    @Update
    fun update(info: InfoItem): Completable

    @Delete
    fun delete(info: InfoItem): Completable

    @Query("SELECT * FROM Info")
    fun getAll(): Single<List<InfoItem>>

    @Query("SELECT * FROM Info WHERE photoId = :photoId LIMIT 1")
    fun get(photoId: String): Observable<InfoItem>
}
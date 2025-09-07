package com.andro_sk.eventnotes.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andro_sk.eventnotes.data.local.database.entities.PhotoEntity

@Dao
interface EventsPhotosDao {
    @Query("SELECT * FROM events_photos WHERE event_id = :eventId")
    suspend fun fetchPhotos(eventId: String): List<PhotoEntity>

    @Upsert
    suspend fun upsertPhoto(event: List<PhotoEntity>): List<Long>

    @Query("DELETE FROM events_photos WHERE id NOT IN (:photoId) AND event_id = :eventId")
    suspend fun deletePhotoByEventId(photoId: List<String>, eventId: String): Int

    @Query("DELETE FROM events_photos")
    suspend fun deleteAllPhotos()
}
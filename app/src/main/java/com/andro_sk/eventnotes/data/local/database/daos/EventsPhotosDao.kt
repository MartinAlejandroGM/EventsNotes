package com.andro_sk.eventnotes.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andro_sk.eventnotes.data.local.database.entities.PhotoEntity

@Dao
interface EventsPhotosDao {
    @Query("SELECT * FROM events_photos WHERE id")
    suspend fun fetchEvents(): List<PhotoEntity>

    @Upsert
    suspend fun insertEvent(event: PhotoEntity): Long

    @Query("DELETE FROM events_photos WHERE id = :eventId")
    suspend fun deletePhotoByEventId(eventId: String): Int

    @Query("DELETE FROM events_photos")
    suspend fun deleteAll()
}
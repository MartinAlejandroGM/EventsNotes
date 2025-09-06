package com.andro_sk.eventnotes.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.andro_sk.eventnotes.data.local.database.entities.EventsEntity

@Dao
interface EventsDao {

    @Query("SELECT * FROM events ORDER BY event_date")
    suspend fun fetchEvents(): List<EventsEntity>

    @Upsert
    suspend fun insertEvent(event: EventsEntity): Long

    @Update
    suspend fun updateEvent(event: EventsEntity): Int

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: String): Int

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}
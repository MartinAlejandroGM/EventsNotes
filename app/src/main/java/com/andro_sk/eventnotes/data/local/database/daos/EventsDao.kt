package com.andro_sk.eventnotes.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.andro_sk.eventnotes.data.local.database.entities.EventsEntity
import com.andro_sk.eventnotes.data.local.database.pojo.EventWithDetails

@Dao
interface EventsDao {

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun fetchEvents(): List<EventWithDetails>

    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun fetchEventById(eventId: String): List<EventWithDetails>

    @Upsert
    suspend fun upsertEvent(event: EventsEntity): Long

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: String): Int

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}
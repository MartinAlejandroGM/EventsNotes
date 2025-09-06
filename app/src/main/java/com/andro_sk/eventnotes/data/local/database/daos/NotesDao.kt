package com.andro_sk.eventnotes.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andro_sk.eventnotes.data.local.database.entities.NotesEntity

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE event_id = :eventId")
    suspend fun fetchNotesByEventId(eventId: String): List<NotesEntity>

    @Upsert
    suspend fun upsertNotes(event: List<NotesEntity>): List<Long>

    @Query("DELETE FROM notes WHERE id NOT IN (:noteIds) AND event_id = :eventId")
    suspend fun deleteNotes(noteIds: List<String>, eventId: String)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
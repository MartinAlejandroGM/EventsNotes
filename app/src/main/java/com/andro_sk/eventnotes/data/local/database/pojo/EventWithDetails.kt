package com.andro_sk.eventnotes.data.local.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.andro_sk.eventnotes.data.local.database.entities.EventsEntity
import com.andro_sk.eventnotes.data.local.database.entities.NotesEntity
import com.andro_sk.eventnotes.data.local.database.entities.PhotoEntity

data class EventWithDetails(
    @Embedded
    val event: EventsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_id"
    )
    val notes: List<NotesEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "event_id"
    )
    val photos: List<PhotoEntity>
)
package com.andro_sk.eventnotes.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = EventsEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class NotesEntity(
    @ColumnInfo(name = "event_id", index = true) val eventId: String,
    @ColumnInfo(name = "description") val description: String,
    @PrimaryKey(autoGenerate = false) var id: String
)
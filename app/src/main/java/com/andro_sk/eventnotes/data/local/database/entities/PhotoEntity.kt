package com.andro_sk.eventnotes.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "events_photos",
    foreignKeys = [
        ForeignKey(
            entity = EventsEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhotoEntity(
    @ColumnInfo(name = "event_id", index = true) val tripId: Long,
    @PrimaryKey(autoGenerate = true) var id: Long? = null
)
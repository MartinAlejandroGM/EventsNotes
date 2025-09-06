package com.andro_sk.eventnotes.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "events"
)
data class EventsEntity(
    @ColumnInfo(name = "eventTittle") val eventTittle: String,
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP") var createdAt: Date? = null,
    @ColumnInfo(name = "modified_at", defaultValue = "CURRENT_TIMESTAMP") var modifiedAt: Date? = null,
    @ColumnInfo(name = "event_date") var eventDate: Date
)
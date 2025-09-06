package com.andro_sk.eventnotes.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "events"
)
data class EventsEntity(
    @ColumnInfo(name = "eventTittle") val eventTittle: String,
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo(name = "event_date") var eventDate: String
)
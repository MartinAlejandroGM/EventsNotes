package com.andro_sk.eventnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andro_sk.eventnotes.data.local.database.Converters.DateConverter
import com.andro_sk.eventnotes.data.local.database.daos.EventsDao
import com.andro_sk.eventnotes.data.local.database.daos.EventsPhotosDao
import com.andro_sk.eventnotes.data.local.database.daos.NotesDao
import com.andro_sk.eventnotes.data.local.database.entities.EventsEntity
import com.andro_sk.eventnotes.data.local.database.entities.NotesEntity
import com.andro_sk.eventnotes.data.local.database.entities.PhotoEntity

@TypeConverters(DateConverter::class)
@Database(
    entities = [
        EventsEntity::class,
        PhotoEntity::class,
        NotesEntity::class
               ],
    version = 1,
    exportSchema = true
)
abstract class EventsDatabase : RoomDatabase() {
    abstract fun fetchEventDao(): EventsDao
    abstract fun photosDao(): EventsPhotosDao
    abstract fun notesDao(): NotesDao
}
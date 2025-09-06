package com.andro_sk.eventnotes.di

import android.content.Context
import androidx.room.Room
import com.andro_sk.eventnotes.data.local.database.EventsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context) : EventsDatabase {
        return Room.databaseBuilder(
            context,
            EventsDatabase::class.java,
            "events_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEventsDao(database: EventsDatabase) = database.fetchEventDao()

    @Provides
    @Singleton
    fun provideNotesDao(database: EventsDatabase) = database.notesDao()

    @Provides
    @Singleton
    fun providePhotosDao(database: EventsDatabase) = database.photosDao()
}
package com.andro_sk.eventnotes.di

import android.content.Context
import com.andro_sk.eventnotes.data.local.database.daos.EventsDao
import com.andro_sk.eventnotes.data.local.database.daos.EventsPhotosDao
import com.andro_sk.eventnotes.data.local.database.daos.NotesDao
import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import com.andro_sk.eventnotes.data.local.repository.EventNotesDbRepositoryImpl
import com.andro_sk.eventnotes.domain.contracts.DataStoreRepository
import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesEventsRepository(eventsDao: EventsDao, notesDao: NotesDao, photosDao: EventsPhotosDao): EventsRepository =
        EventNotesDbRepositoryImpl(eventsDao = eventsDao, notesDao = notesDao, photosDao = photosDao)

    @Provides
    @Singleton
    fun providesEventRepository(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepositoryImpl(context)
}
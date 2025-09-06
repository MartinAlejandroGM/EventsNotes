package com.andro_sk.eventnotes.di

import com.andro_sk.eventnotes.data.local.repository.EventsRepositoryImpl
import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventsRepository(eventRepositoryImpl: EventsRepositoryImpl): EventsRepository
}
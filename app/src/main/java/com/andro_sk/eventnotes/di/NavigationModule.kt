package com.andro_sk.eventnotes.di

import android.content.Context
import com.andro_sk.eventnotes.data.local.navigation.NavigationBus
import com.andro_sk.eventnotes.data.local.repository.DataStoreRepositoryImpl
import com.andro_sk.eventnotes.domain.contracts.DataStoreRepository
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModules {
    @Provides
    @Singleton
    fun providesApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideNavigationBus(): NavigationBus {
        return NavigationBus()
    }

    @Provides
    @Singleton
    fun provideNavigationEmitter(navigationBus: NavigationBus): NavigationEmitter {
        return navigationBus
    }

    @Provides
    @Singleton
    fun provideNavigationReceiver(navigationBus: NavigationBus): NavigationReceiver {
        return navigationBus
    }

    @Provides
    @Singleton
    fun providesEventRepository(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepositoryImpl(context)
}
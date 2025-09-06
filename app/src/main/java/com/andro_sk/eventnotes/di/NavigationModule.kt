package com.andro_sk.eventnotes.di

import com.andro_sk.eventnotes.data.local.navigation.NavigationBus
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModules {

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
}
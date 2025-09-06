package com.andro_sk.eventnotes.domain.contracts

import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import kotlinx.coroutines.flow.Flow

interface NavigationReceiver {
    val navigation: Flow<NavigationAction>
}
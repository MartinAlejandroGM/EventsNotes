package com.andro_sk.eventnotes.domain.contracts

import com.andro_sk.eventnotes.data.local.navigation.NavigationAction

interface NavigationEmitter {
    suspend fun post(action: NavigationAction)
}
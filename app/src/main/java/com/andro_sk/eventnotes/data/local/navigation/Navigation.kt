package com.andro_sk.eventnotes.data.local.navigation

import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigationBus @Inject constructor() : NavigationEmitter, NavigationReceiver {

    private val _navigation = MutableSharedFlow<NavigationAction>()
    override val navigation = _navigation.asSharedFlow()

    override suspend fun post(action: NavigationAction) {
        _navigation.emit(action)
    }

}

sealed class NavigationAction {

    data class NavigateTo(val route: String, val clearBackStack: Boolean = false) :
        NavigationAction()

    data class NavigateToWithArgs(
        val route: String,
        val args: Map<String, String>,
        val clearBackStack: Boolean = false,
    ) : NavigationAction()

    data object NavigateBack : NavigationAction()

}
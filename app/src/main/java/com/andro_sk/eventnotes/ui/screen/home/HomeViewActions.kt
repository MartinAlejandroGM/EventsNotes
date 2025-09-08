package com.andro_sk.eventnotes.ui.screen.home

sealed class HomeViewActions {
    data class OnNavigateToAddEvent(val eventId: String = "") : HomeViewActions()
    data class OnNavigateToEventDetails(val eventId: String) : HomeViewActions()
    data class RemoveEventItem(val eventId: String) : HomeViewActions()
    data class OnChangeTheme(val isDarkModeOn: Boolean): HomeViewActions()
    object FilterEvents : HomeViewActions()
}
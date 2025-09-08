package com.andro_sk.eventnotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.use_cases.DeleteEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.FetchEventsUseCase
import com.andro_sk.eventnotes.domain.use_cases.GetUserPreferencesUseCase
import com.andro_sk.eventnotes.domain.use_cases.SaveFilterByUseCase
import com.andro_sk.eventnotes.ui.navigation.AppRoutesArgs
import com.andro_sk.eventnotes.ui.state.EventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val fetchEventsUseCase: FetchEventsUseCase,
    private val deleteEventByIdUseCase: DeleteEventByIdUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val saveFilterByUseCase: SaveFilterByUseCase,
    private val navigationEmitter: NavigationEmitter
) : ViewModel() {
    private val _contentState = MutableStateFlow<EventState>(EventState.Loading)
    val contentState = _contentState.asStateFlow()

    private var sortBy: String = "date"

    fun fetchContent() {
        viewModelScope.launch {
            _contentState.emit(EventState.Loading)
            getUserPreferencesUseCase.invoke().collect { response ->
                sortBy = response.filterBy
                fetchEvents(response.filterBy)
            }
        }
    }

    fun updateSortBy() {
        sortBy = if (sortBy == "date")
            "name"
        else "date"
        viewModelScope.launch {
            saveFilterByUseCase.invoke(sortBy)
            fetchEvents(sortBy)
        }
    }

    private suspend fun fetchEvents(sortBy: String) {
        val events = fetchEventsUseCase.invoke(sortBy)
        when (events) {
            is Response.Success -> {
                _contentState.emit(EventState.Content(events.data))
            }

            is Response.Error -> {
                _contentState.emit(EventState.Error)
            }
        }
    }

    fun navigateTo(route: String, eventId: String = "") {
         val typeOfNavigation = if (eventId.isNotBlank())
             NavigationAction.NavigateToWithArgs(
                 route = route,
                 args = mapOf(AppRoutesArgs.EVENT_ID to eventId),
             )
        else NavigationAction.NavigateTo(
             route = route)
        viewModelScope.launch {
            navigationEmitter.post(
                typeOfNavigation
            )
        }
    }

    fun deleteEventById(eventId: String) {
        viewModelScope.launch {
            _contentState.emit(EventState.Loading)
            val result = deleteEventByIdUseCase.invoke(eventId)
            when(result) {
                is Response.Success -> {
                    fetchEvents(sortBy)
                }
                is Response.Error -> {
                    _contentState.emit(EventState.Error)
                }
            }
        }
    }
}
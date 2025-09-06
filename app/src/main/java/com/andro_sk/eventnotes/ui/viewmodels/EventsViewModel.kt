package com.andro_sk.eventnotes.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro_sk.eventnotes.R
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.use_cases.DeleteEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.FetchEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.FetchEventsUseCase
import com.andro_sk.eventnotes.ui.navigation.AppRoutesArgs
import com.andro_sk.eventnotes.ui.state.AddEditDetailsState
import com.andro_sk.eventnotes.ui.state.DialogState
import com.andro_sk.eventnotes.ui.state.EventsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val fetchEventsUseCase: FetchEventsUseCase,
    private val deleteEventByIdUseCase: DeleteEventByIdUseCase,
    private val fetchEventByIdUseCase: FetchEventByIdUseCase,
    private val navigationEmitter: NavigationEmitter
) : ViewModel() {

    private val _eventName = mutableStateOf("")
    val eventName: State<String> get() = _eventName

    private val _eventDate = mutableStateOf("")
    val eventDate: State<String> get() = _eventDate

    private val _eventNotes = mutableStateListOf("")
    val eventNotes: List<String> get() = _eventNotes

    private val _photosUris = mutableStateSetOf<Uri?>()
    val photosUris: SnapshotStateSet<Uri?> get() = _photosUris

    fun onAddEventNote(note: String) {
        _eventNotes.add(note)
    }

    fun onRemoveEventNote(note: String) {
        _eventNotes.remove(note)
    }

    fun onEventTittleChanged(name: String) {
        _eventName.value = name
    }

    fun onEventDateChanged(eventDate: String) {
        _eventDate.value = eventDate
    }

    private val _events = MutableStateFlow(EventsState(isLoading = true))
    val events: StateFlow<EventsState>
        get() = _events

    private  val _event = MutableStateFlow(AddEditDetailsState(isLoading = true))
    val event: StateFlow<AddEditDetailsState>
        get() = _event

    fun fetchEvents(filterBy: String) {
        viewModelScope.launch {
            _events.value = EventsState(isLoading = true)
            fetchEventsUseCase.invoke(filterBy).collect { result ->
                when(result) {
                    is Response.Success -> {
                        _events.value = EventsState(isLoading = false, events = result.data)
                    }

                    is Response.Error -> {
                        _events.value = EventsState(isLoading = false, error = DialogState(
                            titleResId = R.string.error,
                            messageResId = R.string.error_fetch_events,
                            confirmText = R.string.retry,
                            dismissText = R.string.cancel,
                            onConfirm = {
                                onDismissDialog()
                                fetchEvents (filterBy)
                            },
                            onDismiss = { onDismissDialog() }
                        ))
                    }
                }
            }
        }
    }

    fun navigateTo(route: String, eventId: String = "", filterBy: String) {
        viewModelScope.launch {
            navigationEmitter.post(
                NavigationAction.NavigateToWithArgs(
                    route = route,
                    args = mapOf(AppRoutesArgs.EVENT_ID to eventId, AppRoutesArgs.FILTERED_BY to filterBy),
                )
            )
        }
    }

    fun onDismissDialog() {
        _events.value = EventsState(isLoading = false)
    }

    fun deleteEventById(eventId: String, filterBy: String) {
        viewModelScope.launch {
            _events.value = EventsState(isLoading = true)
            deleteEventByIdUseCase.invoke(eventId).collect { result ->
                when(result) {
                    is Response.Success -> {
                        fetchEvents(filterBy)
                    }

                    is Response.Error -> {
                        _events.value = EventsState(isLoading = false, error = DialogState(
                            titleResId = R.string.error,
                            messageResId = R.string.error_fetch_events,
                            confirmText = R.string.retry,
                            dismissText = R.string.cancel,
                            onConfirm = {
                                onDismissDialog()
                            },
                            onDismiss = { onDismissDialog() }
                        ))
                    }
                }
            }
        }
    }

    fun fetchEventById(eventId: String) {
        viewModelScope.launch {
            _event.value = AddEditDetailsState(isLoading = true)
            fetchEventByIdUseCase.invoke(eventId).collect { result ->
                when(result) {
                    is Response.Success -> {
                        _event.value = AddEditDetailsState(isLoading = false, event = result.data)
                    }

                    is Response.Error -> {
                        _event.value = AddEditDetailsState(isLoading = false, error = DialogState(
                            titleResId = R.string.error,
                            messageResId = R.string.error_fetch_events,
                            confirmText = R.string.retry,
                            dismissText = R.string.cancel,
                            onConfirm = {
                                onDismissDialog()
                            },
                            onDismiss = { onDismissDialog() }
                        ))
                    }
                }
            }
        }
    }

    fun addEvent() {

    }
}
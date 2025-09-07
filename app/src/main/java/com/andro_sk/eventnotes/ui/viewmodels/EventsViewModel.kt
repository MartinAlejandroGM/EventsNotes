package com.andro_sk.eventnotes.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro_sk.eventnotes.R
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.EventNote
import com.andro_sk.eventnotes.domain.models.EventPhoto
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.use_cases.DeleteEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.FetchEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.FetchEventsUseCase
import com.andro_sk.eventnotes.domain.use_cases.UpsertEventUseCase
import com.andro_sk.eventnotes.ui.navigation.AppRoutesArgs
import com.andro_sk.eventnotes.ui.state.AddEditDetailsState
import com.andro_sk.eventnotes.ui.state.DialogState
import com.andro_sk.eventnotes.ui.state.EventByIdState
import com.andro_sk.eventnotes.ui.state.EventsState
import com.andro_sk.eventnotes.ui.utils.generateRandomUUID
import com.andro_sk.eventnotes.ui.utils.toEventPhotosList
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
    private val upsertEventUseCase: UpsertEventUseCase,
    private val navigationEmitter: NavigationEmitter
) : ViewModel() {

    private val _eventName = mutableStateOf("")
    val eventName: State<String> get() = _eventName

    private val _eventDate = mutableStateOf("")
    val eventDate: State<String> get() = _eventDate

    private val _eventNotes = mutableStateListOf<EventNote>()
    val eventNotes: List<EventNote> get() = _eventNotes

    private val _photosUris = mutableStateListOf<EventPhoto>()
    val photosUris: List<EventPhoto> get() = _photosUris

    private var eventId: String = ""

    fun onAddPhotos(photoUris: List<Uri>) {
        _photosUris.addAll(photoUris.toEventPhotosList(_photosUris.map { it.uri }))
    }

    fun onRemovePhoto(photoUri: EventPhoto) {
        _photosUris.remove(photoUri)
    }

    fun onAddEventNote(note: EventNote) {
        _eventNotes.add(note)
    }

    fun onUpdateEventNote(note: EventNote) {
        _eventNotes[_eventNotes.indexOfFirst { it.id == note.id }] = note
    }

    fun onRemoveEventNote(noteId: String) {
        _eventNotes.removeAt(_eventNotes.indexOfFirst { it.id == noteId })
    }

    fun onEventTittleChanged(name: String) {
        _eventName.value = name
    }

    fun onEventDateChanged(eventDate: String) {
        _eventDate.value = eventDate
    }

    fun setAllNotes(eventNotes: List<EventNote>) {
        _eventNotes.clear()
        _eventNotes.addAll(eventNotes)
    }

    fun setEventId(eventId: String) {
        this.eventId = eventId
    }

    private val _events = MutableStateFlow(EventsState(isLoading = true))
    val events: StateFlow<EventsState>
        get() = _events

    private  val _event = MutableStateFlow(EventByIdState(isLoading = true))
    val event: StateFlow<EventByIdState>
        get() = _event

    private val _saveEvent = MutableStateFlow(AddEditDetailsState(isLoading = false))
    val saveEvent: StateFlow<AddEditDetailsState>
        get() = _saveEvent

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
            _event.value = EventByIdState(isLoading = true)
            fetchEventByIdUseCase.invoke(eventId).collect { result ->
                when(result) {
                    is Response.Success -> {
                        _event.value = EventByIdState(isLoading = false, event = result.data)
                    }

                    is Response.Error -> {
                        _event.value = EventByIdState(isLoading = false, error = DialogState(
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

    fun onBack() {
        viewModelScope.launch {
            navigationEmitter.post(NavigationAction.NavigateBack)
        }
    }

    fun onSaveEvent() {
        _saveEvent.value = AddEditDetailsState(isLoading = true)
        viewModelScope.launch {
            val event = EventModel(
                id = generateRandomUUID(),
                eventTittle = _eventName.value,
                date = _eventDate.value,
                imageUrl = _photosUris.firstOrNull()?.uri ?: Uri.EMPTY,
                eventPhotos = _photosUris,
                eventNotes = _eventNotes
            )
            upsertEventUseCase.invoke(event).collect { result ->
                when(result) {
                    is Response.Success -> {
                        _saveEvent.value = AddEditDetailsState(result =  result.data)
                    }

                    is Response.Error -> {
                        _saveEvent.value = AddEditDetailsState(error = DialogState(
                            titleResId = R.string.error,
                            messageResId = R.string.error_fetch_events,
                            confirmText = R.string.retry,
                            dismissText = R.string.cancel,
                            onConfirm = {},
                            onDismiss = {}
                        ))
                    }
                }
            }
        }
    }

    fun onUpdateEvent() {
        _saveEvent.value = AddEditDetailsState(isLoading = true)
        viewModelScope.launch {
            val event = EventModel(
                id = eventId,
                eventTittle = _eventName.value,
                date = _eventDate.value,
                imageUrl = _photosUris.firstOrNull()?.uri?: Uri.EMPTY,
                eventPhotos = _photosUris,
                eventNotes = _eventNotes
            )
            upsertEventUseCase.invoke(event).collect { result ->
                when(result) {
                    is Response.Success -> {
                        _saveEvent.value = AddEditDetailsState(result =  result.data)
                    }

                    is Response.Error -> {
                        _saveEvent.value = AddEditDetailsState(error = DialogState(
                            titleResId = R.string.error,
                            messageResId = R.string.error_fetch_events,
                            confirmText = R.string.retry,
                            dismissText = R.string.cancel,
                            onConfirm = {},
                            onDismiss = {}
                        ))
                    }
                }
            }
        }
    }
}
package com.andro_sk.eventnotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationEmitter
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.domain.use_cases.FetchEventByIdUseCase
import com.andro_sk.eventnotes.domain.use_cases.UpsertEventUseCase
import com.andro_sk.eventnotes.ui.state.EventDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertViewModel @Inject constructor(
    private val upsertEventUseCase: UpsertEventUseCase,
    private val navigationEmitter: NavigationEmitter,
    private val fetchEventByIdUseCase: FetchEventByIdUseCase,
): ViewModel() {
    private val _contentState = MutableStateFlow<EventDetailsState>(EventDetailsState.Loading)
    val contentState = _contentState.asStateFlow()

    fun changeContentStateForAddView() {
        viewModelScope.launch {
            _contentState.emit(EventDetailsState.Content(EventModel()))
        }
    }

    fun onUpsertEvent(eventModel: EventModel) {
        viewModelScope.launch {
            _contentState.emit(EventDetailsState.Loading)
            val result = upsertEventUseCase.invoke(eventModel)
            when(result) {
                is Response.Success -> {
                    _contentState.emit(EventDetailsState.UpsertEvent)
                }

                is Response.Error -> {
                    _contentState.emit(EventDetailsState.Error)
                }
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            navigationEmitter.post(NavigationAction.NavigateBack)
        }
    }

    fun fetchEventById(eventId: String) {
        viewModelScope.launch {
            _contentState.emit(EventDetailsState.Loading)
            val result = fetchEventByIdUseCase.invoke(eventId)
            when(result) {
                is Response.Success -> {
                    _contentState.emit(EventDetailsState.Content(result.data))
                }

                is Response.Error -> {
                    _contentState.emit(EventDetailsState.Error)
                }
            }
        }
    }
}
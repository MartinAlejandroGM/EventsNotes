package com.andro_sk.eventnotes.ui.state

import com.andro_sk.eventnotes.domain.models.EventModel

sealed interface EventState {
    data object Loading: EventState
    data class Content(val events: List<EventModel>): EventState
    data object Error: EventState
}
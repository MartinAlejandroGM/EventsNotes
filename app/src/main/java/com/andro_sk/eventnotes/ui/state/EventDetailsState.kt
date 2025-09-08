package com.andro_sk.eventnotes.ui.state

import com.andro_sk.eventnotes.domain.models.EventModel

interface EventDetailsState {
    data object Loading: EventDetailsState
    data class Content(val event: EventModel): EventDetailsState
    data object UpsertEvent: EventDetailsState
    data object Error: EventDetailsState
}
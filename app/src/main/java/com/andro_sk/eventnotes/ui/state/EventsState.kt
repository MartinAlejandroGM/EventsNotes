package com.andro_sk.eventnotes.ui.state

import com.andro_sk.eventnotes.domain.models.EventModel

data class EventsState(
    val isLoading: Boolean = false,
    val events: List<EventModel>? = null,
    val error: DialogState? = null
)

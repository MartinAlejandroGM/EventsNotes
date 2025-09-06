package com.andro_sk.eventnotes.ui.state

import com.andro_sk.eventnotes.domain.models.EventModel

data class EventByIdState (
    val isLoading: Boolean = false,
    val event: EventModel? = null,
    val error: DialogState? = null
)
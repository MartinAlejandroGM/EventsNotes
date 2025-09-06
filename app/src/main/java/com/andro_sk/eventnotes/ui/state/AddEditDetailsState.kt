package com.andro_sk.eventnotes.ui.state

data class AddEditDetailsState(
    val isLoading: Boolean = false,
    val result: Long? = null,
    val error: DialogState? = null
)

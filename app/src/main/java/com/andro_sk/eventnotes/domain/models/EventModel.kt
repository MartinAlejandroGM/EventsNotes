package com.andro_sk.eventnotes.domain.models

import android.net.Uri
import java.time.LocalDate

data class EventModel(
    val id: String? = null,
    val eventTittle: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val date: String = LocalDate.now().toString(),
    val eventPhotos: List<Uri> = arrayListOf(),
    val eventNotes: List<EventNote> = arrayListOf()
)

data class EventNote(
    val id: String = "",
    val description: String = ""
)

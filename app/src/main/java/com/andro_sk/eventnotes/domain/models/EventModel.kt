package com.andro_sk.eventnotes.domain.models

import android.net.Uri
import java.time.LocalDate

data class EventModel(
    val id: String = "",
    val eventTittle: String = "",
    val description: String = "",
    val imageUrl: Uri = Uri.EMPTY,
    val date: String = LocalDate.now().toString(),
    val eventPhotos: List<EventPhoto> = arrayListOf(),
    val eventNotes: List<EventNote> = arrayListOf()
)

data class EventNote(
    val id: String = "",
    val description: String = ""
)

data class EventPhoto(
    val id: String = "",
    val uri: Uri
)

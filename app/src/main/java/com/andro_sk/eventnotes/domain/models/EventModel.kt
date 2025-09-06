package com.andro_sk.eventnotes.domain.models

import android.net.Uri
import java.time.LocalDate

data class EventModel(
    val id: String? = null,
    val eventTittle: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val date: String = LocalDate.now().toString(),
    val eventPhotos: EventPhotos = EventPhotos()
)

data class EventPhotos(
    val id: String? = null,
    val urls: List<Uri> = arrayListOf()
)

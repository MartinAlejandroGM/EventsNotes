package com.andro_sk.eventnotes.ui.utils

import android.net.Uri
import com.andro_sk.eventnotes.domain.models.EventPhoto

fun List<Uri>.toEventPhotosList(currentUrisAdded: List<Uri>): List<EventPhoto>{
    val urisNotAddedYet = this.filterNot { it in currentUrisAdded }
    return urisNotAddedYet.map {
        EventPhoto(
            id = generateRandomUUID(),
            uri = it)
    }
}

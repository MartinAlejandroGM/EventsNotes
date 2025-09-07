package com.andro_sk.eventnotes.ui.utils

import android.net.Uri
import com.andro_sk.eventnotes.domain.models.EventNote
import com.andro_sk.eventnotes.domain.models.EventPhoto

sealed class AddUpdateDetailsEventViewActions() {
    object OnBack : AddUpdateDetailsEventViewActions()
    data class OnWriteName(val name: String) : AddUpdateDetailsEventViewActions()
    data class OnSelectDate(val selectedDate: String) : AddUpdateDetailsEventViewActions()
    data class OnUpdateNotes(val note: EventNote) : AddUpdateDetailsEventViewActions()
    data class OnAddNote(val note: EventNote): AddUpdateDetailsEventViewActions()
    data class OnRemoveNote(val id: String): AddUpdateDetailsEventViewActions()
    data class OnSelectCoverPhoto(val uris: List<Uri>) : AddUpdateDetailsEventViewActions()
    data class OnRemovePhoto(val photo: EventPhoto) : AddUpdateDetailsEventViewActions()
    object OnSave : AddUpdateDetailsEventViewActions()
}
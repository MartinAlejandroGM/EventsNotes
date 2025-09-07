package com.andro_sk.eventnotes.ui.core

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventPhoto

@Composable
fun PickMedia(
    photos: List<EventPhoto>,
    onAddPhotos: (uris: List<Uri>) -> Unit,
    onRemovePhoto: (EventPhoto) -> Unit
) {
    val context = LocalContext.current.applicationContext
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                val contentResolver = context.contentResolver
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                for (uri in uris)
                    contentResolver.takePersistableUriPermission(uri, takeFlags)

                onAddPhotos.invoke(uris)
            }
        }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
        ) {
            Button(
                onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.Photo,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.event_photos))
                }
            }
            photos.forEach { photo ->
                EventPhotoRow(
                    photo.uri,
                    onRemovePhoto = {
                        onRemovePhoto.invoke(photo)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PickMediaTwoPicksPreview() {
    PickMedia(
        photos = arrayListOf(
            EventPhoto(
                id = "",
                uri = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es".toUri()
            ),
            EventPhoto(
                id = "",
                uri = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es".toUri()
            )
        ),
        onRemovePhoto = {},
        onAddPhotos = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PickMediaOnePicksPreview() {
    PickMedia(
        photos = arrayListOf(
            EventPhoto(
                id = "",
                uri = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es".toUri()
            )
        ),
        onRemovePhoto = {},
        onAddPhotos = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PickMediaPreview() {
    PickMedia(
        photos = arrayListOf(),
        onRemovePhoto = {},
        onAddPhotos = {}
    )
}
package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.views.home.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: EventModel,
    onRemoveEvent: () -> Unit,
    onEditEvent: () -> Unit,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemoveEvent.invoke()
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEditEvent.invoke()
                    false
                }
                else -> {
                    false
                }
            }
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier,
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.next),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray)
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.add_event),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        },
    ) {
        Card(modifier, event) {
            onEditEvent.invoke()
        }
    }
}

@Preview
@Composable
fun EventCardPreview() {
    EventCard(
        event = EventModel(
            id = "1",
            eventTittle = "Peach's Party",
            imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es".toUri(),
            date = "01/10/2025"
        ),
        onRemoveEvent = {},
        onEditEvent = {}
    )
}
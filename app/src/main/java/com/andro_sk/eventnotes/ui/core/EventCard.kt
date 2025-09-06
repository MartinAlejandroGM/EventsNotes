package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.utils.getDescriptionDaysText
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: EventModel,
    onRemoveEvent: () -> Unit,
    onNavigateToEventDetails: () -> Unit,
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
        Card(
            modifier = modifier,
            onClick = {  onNavigateToEventDetails.invoke() },
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.BottomStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    GlideImage(
                        imageModel = { event.imageUrl },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        ),
                        loading = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        },
                        failure = {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    Icons.Filled.Build,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    "Image Not Available", style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color.Gray,
                                    )
                                )
                            }
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x8A000000), //87% opacity
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            event.eventTittle,
                            style = TextStyle(
                                color = Color.White
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                        )
                        Text(
                            event.date.getDescriptionDaysText(),
                            style = TextStyle(
                                color = Color.White
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W300,
                        )
                    }
                }
            }
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
            description = "3 Days left",
            imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
            date = "01/10/2025"
        ),
        onRemoveEvent = {},
        onEditEvent = {},
        onNavigateToEventDetails = {}
    )
}
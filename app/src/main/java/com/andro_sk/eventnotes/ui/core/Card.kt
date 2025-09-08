package com.andro_sk.eventnotes.ui.core

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.extension.getDescriptionDaysText
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun Card(
    modifier: Modifier = Modifier,
    event: EventModel,
    onClickCard: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = { onClickCard.invoke() },
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.BottomStart
        ) {
            CardGlideEvent(event.imageUrl)
            CardEventText(event.date, event.eventTittle)
        }
    }
}

@Composable
private fun CardGlideEvent(eventUrl: Uri){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        GlideImage(
            imageModel = { eventUrl },
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
}

@Composable
private fun CardEventText(eventDate: String, eventTittle: String) {
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
                eventTittle,
                style = TextStyle(
                    color = Color.White
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
            )
            Text(
                eventDate.getDescriptionDaysText(),
                style = TextStyle(
                    color = Color.White
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.W300,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPreview() {
    Card(
        modifier = Modifier,
        event = EventModel(),
        onClickCard = {}
    )
}
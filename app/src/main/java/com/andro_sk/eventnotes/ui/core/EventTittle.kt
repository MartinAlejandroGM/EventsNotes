package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andro_sk.eventnotes.R

@Composable
fun EventTittle(
    eventTittle: String? = null,
    onTittleChanged: (changedTittle: String) -> Unit
){
    CustomOutlinedTextField(
        value = eventTittle.orEmpty(),
        label = stringResource(R.string.event_name),
        onValueChange = { value ->
            onTittleChanged.invoke(value)
        },
    )
}

@Preview(showBackground = true)
@Composable
fun EventTittleAddPreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        EventTittle(onTittleChanged = {})
    }
}

@Preview(showBackground = true)
@Composable
fun EventTittleUpdatePreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        EventTittle("Event's Name", onTittleChanged = {})
    }
}
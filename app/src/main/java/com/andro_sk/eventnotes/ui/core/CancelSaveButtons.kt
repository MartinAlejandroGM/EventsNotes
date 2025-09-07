package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andro_sk.eventnotes.R

@Composable
fun CancelSaveButtons(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onCancelClick.invoke()
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onSaveClick.invoke()
                }
            ) {
                Text(stringResource(R.string.save_event))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelSaveButtonsPreview() {
    CancelSaveButtons(
        onCancelClick = {},
        onSaveClick = {}
    )
}
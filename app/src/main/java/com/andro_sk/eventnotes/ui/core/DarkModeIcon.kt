package com.andro_sk.eventnotes.ui.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DarkModeIcon(
    isDarkMode: Boolean,
    onChangeTheme: (Boolean) -> Unit
) {
    IconButton(
        onClick = {
            onChangeTheme.invoke(!isDarkMode)
        }
    ) {
        Icon(
            // Usa el ícono de luna si está en modo oscuro, de lo contrario usa el sol
            imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
            contentDescription = "Cambiar tema",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
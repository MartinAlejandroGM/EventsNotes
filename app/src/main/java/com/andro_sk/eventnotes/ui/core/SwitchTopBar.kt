package com.andro_sk.eventnotes.ui.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andro_sk.eventnotes.ui.viewmodels.UserSettingsViewModel

@Composable
fun ThemeToggleButton(
    settingsViewModel: UserSettingsViewModel
) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()
    IconButton(
        onClick = {
            settingsViewModel.updateTheme()
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
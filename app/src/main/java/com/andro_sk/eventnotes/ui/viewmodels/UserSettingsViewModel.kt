package com.andro_sk.eventnotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andro_sk.eventnotes.domain.use_cases.GetUserSettingsUseCase
import com.andro_sk.eventnotes.domain.use_cases.SaveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val saveThemeUseCase: SaveThemeUseCase
): ViewModel() {
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun updateTheme(){
        viewModelScope.launch {
            val isDark = _isDarkMode.value.not()
            // Le asignamos el nuevo valor
            _isDarkMode.value = isDark
            saveThemeUseCase.invoke(isDark)
        }
    }

    fun getUserSettings(){
        viewModelScope.launch {
            getUserSettingsUseCase.invoke().collect { response ->
                _isDarkMode.value = response.isDarkMode
            }
        }
    }
}
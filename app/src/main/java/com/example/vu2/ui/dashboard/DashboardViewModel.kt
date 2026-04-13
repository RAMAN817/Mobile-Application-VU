package com.example.vu2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vu2.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val entities: List<Map<String, String>>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadEntities(keypass: String) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val result = repository.getEntities(keypass)
            _uiState.value = if (result.isSuccess) {
                DashboardUiState.Success(result.getOrDefault(emptyList()))
            } else {
                DashboardUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

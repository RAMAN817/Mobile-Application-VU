package com.example.vu2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vu2.data.api.ApiService
import com.example.vu2.data.api.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class ValidationError(
        val usernameError: String? = null,
        val passwordError: String? = null
    ) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class Success(val keypass: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        val usernameError = if (username.isBlank()) "Username cannot be empty" else null
        val passwordError = when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters long"
            else -> null
        }

        if (usernameError != null || passwordError != null) {
            _uiState.value = LoginUiState.ValidationError(usernameError, passwordError)
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val request = LoginRequest(username, password)
                val response = apiService.login(request)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = LoginUiState.Success(response.body()!!.keypass)
                } else {
                    _uiState.value = LoginUiState.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Login failed: ${e.message}")
            }
        }
    }
}

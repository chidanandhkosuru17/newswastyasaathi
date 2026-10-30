package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.WorkerUser
import com.example.data.repository.AuthRepository
import com.example.data.sample.SyntheticData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val emailOrId: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val passwordStrength: AuthRepository.PasswordStrength = AuthRepository.PasswordStrength.WEAK,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
    val currentUser: StateFlow<WorkerUser?> = authRepository.currentUser

    fun onEmailOrIdChanged(value: String) {
        val trimmed = value.trim()
        val error = if (trimmed.isNotEmpty() && !authRepository.isValidEmailOrId(trimmed)) {
            "Invalid email format or ID (e.g. ASHA-101)"
        } else null
        _uiState.value = _uiState.value.copy(
            emailOrId = value,
            emailError = error,
            errorMessage = null
        )
    }

    fun onPasswordChanged(value: String) {
        val strength = authRepository.calculatePasswordStrength(value)
        val error = if (value.isNotEmpty() && value.length < 6) {
            "Password must be at least 6 characters"
        } else null
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordStrength = strength,
            passwordError = error,
            errorMessage = null
        )
    }

    fun onRememberMeChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = value)
    }

    fun fillDemoCredentials(index: Int = 0) {
        val worker = SyntheticData.demoWorkers.getOrElse(index) { SyntheticData.demoWorkers.first() }
        _uiState.value = _uiState.value.copy(
            emailOrId = worker.email,
            password = "AshaPass@123",
            passwordStrength = AuthRepository.PasswordStrength.STRONG,
            emailError = null,
            passwordError = null,
            errorMessage = null
        )
    }

    fun signIn(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!authRepository.isValidEmailOrId(state.emailOrId)) {
            _uiState.value = state.copy(emailError = "Please enter a valid email or Worker ID")
            return
        }
        if (state.password.length < 6) {
            _uiState.value = state.copy(passwordError = "Password must be at least 6 characters")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = authRepository.signIn(
                idOrEmail = state.emailOrId,
                password = state.password,
                rememberMe = state.rememberMe
            )
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Authentication failed"
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}

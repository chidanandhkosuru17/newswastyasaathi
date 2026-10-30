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
    val isSignUpMode: Boolean = false,
    val emailOrId: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val role: String = "ASHA Worker",
    val village: String = "Chandrapuri",
    val rememberMe: Boolean = true,
    val passwordStrength: AuthRepository.PasswordStrength = AuthRepository.PasswordStrength.WEAK,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
    val currentUser: StateFlow<WorkerUser?> = authRepository.currentUser

    fun setSignUpMode(isSignUp: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSignUpMode = isSignUp,
            errorMessage = null,
            successMessage = null
        )
    }

    fun onFullNameChanged(value: String) {
        val error = if (value.isNotBlank() && value.trim().length < 2) "Name is too short" else null
        _uiState.value = _uiState.value.copy(
            fullName = value,
            nameError = error,
            errorMessage = null
        )
    }

    fun onRoleChanged(value: String) {
        _uiState.value = _uiState.value.copy(role = value)
    }

    fun onVillageChanged(value: String) {
        _uiState.value = _uiState.value.copy(village = value)
    }

    fun onEmailOrIdChanged(value: String) {
        val trimmed = value.trim()
        val error = if (trimmed.isNotEmpty()) {
            if (_uiState.value.isSignUpMode) {
                if (!authRepository.isValidEmail(trimmed)) "Invalid email format (e.g. name@domain.com)" else null
            } else {
                if (!authRepository.isValidEmailOrId(trimmed)) "Invalid email or Worker ID (e.g. ASHA-101)" else null
            }
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
        val confirmError = if (_uiState.value.confirmPassword.isNotEmpty() && value != _uiState.value.confirmPassword) {
            "Passwords do not match"
        } else null
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordStrength = strength,
            passwordError = error,
            confirmPasswordError = confirmError,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChanged(value: String) {
        val error = if (value.isNotEmpty() && value != _uiState.value.password) {
            "Passwords do not match"
        } else null
        _uiState.value = _uiState.value.copy(
            confirmPassword = value,
            confirmPasswordError = error,
            errorMessage = null
        )
    }

    fun onRememberMeChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = value)
    }

    fun fillDemoCredentials(index: Int = 0) {
        val worker = SyntheticData.demoWorkers.getOrElse(index) { SyntheticData.demoWorkers.first() }
        _uiState.value = _uiState.value.copy(
            isSignUpMode = false,
            emailOrId = worker.email,
            password = "AshaPass@123",
            passwordStrength = AuthRepository.PasswordStrength.STRONG,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            nameError = null,
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

        _uiState.value = state.copy(isLoading = true, errorMessage = null, successMessage = null)
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

    fun signUp(onSuccess: () -> Unit) {
        val state = _uiState.value
        val trimmedName = state.fullName.trim()
        val trimmedEmail = state.emailOrId.trim()
        val trimmedPass = state.password

        if (trimmedName.isEmpty()) {
            _uiState.value = state.copy(nameError = "Please enter your full name")
            return
        }
        if (!authRepository.isValidEmail(trimmedEmail)) {
            _uiState.value = state.copy(emailError = "Please enter a valid email address (e.g. worker@health.org)")
            return
        }
        if (trimmedPass.length < 6) {
            _uiState.value = state.copy(passwordError = "Password must be at least 6 characters")
            return
        }
        if (state.confirmPassword != trimmedPass) {
            _uiState.value = state.copy(confirmPasswordError = "Passwords do not match")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null, successMessage = null)
        viewModelScope.launch {
            val result = authRepository.signUp(
                email = trimmedEmail,
                password = trimmedPass,
                fullName = trimmedName,
                role = state.role,
                village = state.village.ifBlank { "Chandrapuri" },
                rememberMe = state.rememberMe
            )
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Account registered on Supabase! Logged in as ${result.getOrNull()?.name}."
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Registration failed on Supabase"
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}

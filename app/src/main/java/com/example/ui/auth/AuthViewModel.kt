package com.example.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data object Loading : AuthState
    data class Authenticated(val uid: String, val email: String, val displayName: String) : AuthState
    data class Error(val message: String) : AuthState
}

data class AuthUiState(
    val authState: AuthState = AuthState.Unauthenticated,
    val isEmailVerified: Boolean = false,
    val isCheckingVerification: Boolean = false,
    val isSendingVerification: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val resetEmailSentMessage: String? = null,
    val snackbarMessage: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkCurrentUser()
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            val verified = authRepository.isEmailVerified()
            if (currentUser != null) {
                _uiState.update {
                    it.copy(
                        authState = AuthState.Authenticated(
                            uid = currentUser.uid,
                            email = currentUser.email,
                            displayName = currentUser.displayName
                        ),
                        email = currentUser.email,
                        isEmailVerified = verified
                    )
                }
            } else {
                _uiState.update { it.copy(authState = AuthState.Unauthenticated, isEmailVerified = false) }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null, resetEmailSentMessage = null) }
    }

    fun login(onSuccess: (isVerified: Boolean) -> Unit = {}) {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        var hasError = false
        var emailErr: String? = null
        var passErr: String? = null

        if (email.isBlank()) {
            emailErr = "Email address is required"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErr = "Please enter a valid email address"
            hasError = true
        }

        if (password.isBlank()) {
            passErr = "Password is required"
            hasError = true
        } else if (password.length < 6) {
            passErr = "Password must be at least 6 characters"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    emailError = emailErr,
                    passwordError = passErr,
                    snackbarMessage = emailErr ?: passErr
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(authState = AuthState.Loading, snackbarMessage = null) }
            val result = authRepository.signInWithEmailAndPassword(email, password)
            result.onSuccess { user ->
                val verified = authRepository.isEmailVerified()
                _uiState.update {
                    it.copy(
                        authState = AuthState.Authenticated(
                            uid = user.uid,
                            email = user.email,
                            displayName = user.displayName
                        ),
                        email = user.email,
                        password = "",
                        isEmailVerified = verified,
                        snackbarMessage = if (verified) "Welcome back, ${user.displayName}!" else "Please verify your email address to proceed."
                    )
                }
                onSuccess(verified)
            }.onFailure { error ->
                val errorMsg = error.localizedMessage ?: "Login failed. Please try again."
                _uiState.update {
                    it.copy(
                        authState = AuthState.Error(errorMsg),
                        snackbarMessage = errorMsg
                    )
                }
            }
        }
    }

    fun loginAsGuest(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(authState = AuthState.Loading, snackbarMessage = null) }
            val result = authRepository.signInAsGuest()
            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        authState = AuthState.Authenticated(
                            uid = user.uid,
                            email = user.email,
                            displayName = user.displayName
                        ),
                        isEmailVerified = true,
                        snackbarMessage = "Welcome, CET Aspirant!"
                    )
                }
                onSuccess()
            }.onFailure { error ->
                val errorMsg = error.localizedMessage ?: "Guest login failed. Please try again."
                _uiState.update {
                    it.copy(
                        authState = AuthState.Error(errorMsg),
                        snackbarMessage = errorMsg
                    )
                }
            }
        }
    }

    fun register(onSuccess: (isVerified: Boolean) -> Unit = {}) {
        val state = _uiState.value
        val name = state.name.trim()
        val email = state.email.trim()
        val password = state.password
        val confirmPassword = state.confirmPassword

        var hasError = false
        var nameErr: String? = null
        var emailErr: String? = null
        var passErr: String? = null
        var confirmErr: String? = null

        if (name.isBlank()) {
            nameErr = "Full name is required"
            hasError = true
        }

        if (email.isBlank()) {
            emailErr = "Email address is required"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErr = "Please enter a valid email address"
            hasError = true
        }

        if (password.isBlank()) {
            passErr = "Password is required"
            hasError = true
        } else if (password.length < 6) {
            passErr = "Password must be at least 6 characters"
            hasError = true
        }

        if (confirmPassword != password) {
            confirmErr = "Passwords do not match"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    emailError = emailErr,
                    passwordError = passErr,
                    confirmPasswordError = confirmErr,
                    snackbarMessage = nameErr ?: emailErr ?: passErr ?: confirmErr
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(authState = AuthState.Loading, snackbarMessage = null) }
            val result = authRepository.createUserWithEmailAndPassword(name, email, password)
            result.onSuccess { user ->
                val verified = authRepository.isEmailVerified()
                _uiState.update {
                    it.copy(
                        authState = AuthState.Authenticated(
                            uid = user.uid,
                            email = user.email,
                            displayName = user.displayName
                        ),
                        email = user.email,
                        password = "",
                        confirmPassword = "",
                        isEmailVerified = verified,
                        snackbarMessage = "Account created! Verification link sent to ${user.email}."
                    )
                }
                onSuccess(verified)
            }.onFailure { error ->
                val errorMsg = error.localizedMessage ?: "Registration failed. Please try again."
                _uiState.update {
                    it.copy(
                        authState = AuthState.Error(errorMsg),
                        snackbarMessage = errorMsg
                    )
                }
            }
        }
    }

    fun sendPasswordReset(targetEmail: String? = null) {
        val emailToSend = (targetEmail ?: _uiState.value.email).trim()
        if (emailToSend.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailToSend).matches()) {
            _uiState.update {
                it.copy(
                    emailError = "Please provide a valid email to reset password",
                    snackbarMessage = "Please enter a valid email address"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(authState = AuthState.Loading) }
            val result = authRepository.sendPasswordResetEmail(emailToSend)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        authState = AuthState.Unauthenticated,
                        resetEmailSentMessage = "Password reset email sent to $emailToSend",
                        snackbarMessage = "Password reset link sent to $emailToSend. Please check your inbox!"
                    )
                }
            }.onFailure { error ->
                val errorMsg = error.localizedMessage ?: "Could not send password reset email."
                _uiState.update {
                    it.copy(
                        authState = AuthState.Unauthenticated,
                        snackbarMessage = errorMsg
                    )
                }
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSendingVerification = true) }
            val result = authRepository.sendEmailVerification()
            result.onSuccess {
                val email = _uiState.value.email.ifBlank { "your email" }
                _uiState.update {
                    it.copy(
                        isSendingVerification = false,
                        snackbarMessage = "Verification link sent to $email! Check inbox and spam."
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSendingVerification = false,
                        snackbarMessage = error.localizedMessage ?: "Could not send verification email."
                    )
                }
            }
        }
    }

    fun checkEmailVerification(onVerified: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingVerification = true) }
            val result = authRepository.reloadUser()
            result.onSuccess { isVerified ->
                _uiState.update {
                    it.copy(
                        isCheckingVerification = false,
                        isEmailVerified = isVerified,
                        snackbarMessage = if (isVerified) "Email verified successfully! Welcome!" else "Email not verified yet. Please click the link in your email."
                    )
                }
                if (isVerified) {
                    onVerified()
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isCheckingVerification = false,
                        snackbarMessage = error.localizedMessage ?: "Verification check failed. Please try again."
                    )
                }
            }
        }
    }

    fun signOut(onSignedOut: () -> Unit = {}) {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update {
                AuthUiState(
                    authState = AuthState.Unauthenticated,
                    isEmailVerified = false,
                    snackbarMessage = "Signed out successfully."
                )
            }
            onSignedOut()
        }
    }
}

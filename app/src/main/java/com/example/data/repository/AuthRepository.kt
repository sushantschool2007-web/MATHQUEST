package com.example.data.repository

import com.example.data.local.DataStoreManager
import com.example.util.PasswordSecurity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class AuthUser(
    val uid: String,
    val email: String,
    val displayName: String
)

class AuthRepository(
    private val mathRepository: MathRepository,
    private val dataStoreManager: DataStoreManager,
    private val firebaseAuth: FirebaseAuth? = null
) {
    private val hasRemoteFirebase: Boolean
        get() = firebaseAuth != null && try {
            val key = firebaseAuth.app.options.apiKey
            key.isNotBlank() && !key.contains("FakeKey", ignoreCase = true) && !key.contains("AIzaSyFake", ignoreCase = true)
        } catch (_: Exception) {
            false
        }

    suspend fun getCurrentUser(): AuthUser? {
        if (hasRemoteFirebase) {
            val fbUser = firebaseAuth?.currentUser
            if (fbUser != null) {
                return AuthUser(
                    uid = fbUser.uid,
                    email = fbUser.email ?: "",
                    displayName = fbUser.displayName?.takeIf { it.isNotBlank() }
                        ?: fbUser.email?.substringBefore("@")
                        ?: "Aspirant"
                )
            }
        }
        val prefs = dataStoreManager.userPreferencesFlow.firstOrNull()
        return if (prefs?.isLoggedIn == true) {
            AuthUser(
                uid = prefs.userId.ifBlank { "local_user" },
                email = prefs.userEmail.ifBlank { "aspirant@cetquest.edu" },
                displayName = prefs.studentName.ifBlank { "Aspirant" }
            )
        } else {
            null
        }
    }

    fun isUserLoggedIn(): Boolean {
        return hasRemoteFirebase && firebaseAuth?.currentUser != null
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<AuthUser> {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) {
            return Result.failure(Exception("Email cannot be empty."))
        }
        if (password.length < 6) {
            return Result.failure(Exception("Password must be at least 6 characters."))
        }

        // Try Remote Firebase if configured with valid key
        if (hasRemoteFirebase && firebaseAuth != null) {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(cleanEmail, password).awaitTask()
                val user = authResult.user
                if (user != null) {
                    val resolvedName = user.displayName?.takeIf { it.isNotBlank() } ?: cleanEmail.substringBefore("@")
                    mathRepository.saveUserProfile(
                        uid = user.uid,
                        email = user.email ?: cleanEmail,
                        displayName = resolvedName
                    )
                    return Result.success(AuthUser(user.uid, user.email ?: cleanEmail, resolvedName))
                }
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: ""
                val isApiKeyIssue = msg.contains("API key", ignoreCase = true) ||
                        msg.contains("Recaptcha", ignoreCase = true) ||
                        msg.contains("internal error", ignoreCase = true)
                if (!isApiKeyIssue) {
                    return Result.failure(Exception(getFriendlyErrorMessage(e)))
                }
            }
        }

        // Fast & Reliable Local Room Database Authentication
        val existingProfile = mathRepository.findProfileByEmail(cleanEmail)
        return if (existingProfile != null) {
            if (existingProfile.passwordHash.isNotBlank()) {
                val isMatch = PasswordSecurity.verifyPassword(password, existingProfile.salt, existingProfile.passwordHash)
                if (isMatch) {
                    mathRepository.saveUserProfile(
                        uid = existingProfile.uid,
                        email = existingProfile.email,
                        displayName = existingProfile.displayName,
                        passwordHash = existingProfile.passwordHash,
                        salt = existingProfile.salt
                    )
                    Result.success(AuthUser(existingProfile.uid, existingProfile.email, existingProfile.displayName))
                } else {
                    Result.failure(Exception("Incorrect password. Please check and try again."))
                }
            } else {
                // First-time password assignment for existing profile
                val salt = PasswordSecurity.generateSalt()
                val hash = PasswordSecurity.hashPassword(password, salt)
                mathRepository.saveUserProfile(
                    uid = existingProfile.uid,
                    email = existingProfile.email,
                    displayName = existingProfile.displayName,
                    passwordHash = hash,
                    salt = salt
                )
                Result.success(AuthUser(existingProfile.uid, existingProfile.email, existingProfile.displayName))
            }
        } else {
            Result.failure(Exception("No account found with this email. Please tap 'Create Account' to sign up."))
        }
    }

    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Result<AuthUser> {
        val cleanName = name.trim().ifBlank { "Aspirant" }
        val cleanEmail = email.trim()

        if (cleanEmail.isBlank()) {
            return Result.failure(Exception("Email cannot be empty."))
        }
        if (password.length < 6) {
            return Result.failure(Exception("Password must be at least 6 characters."))
        }

        // Check if an account already exists locally with this email
        val existingLocal = mathRepository.findProfileByEmail(cleanEmail)
        if (existingLocal != null && existingLocal.passwordHash.isNotBlank()) {
            return Result.failure(Exception("An account with this email already exists. Please log in."))
        }

        // Try Firebase if configured
        if (hasRemoteFirebase && firebaseAuth != null) {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(cleanEmail, password).awaitTask()
                val user = authResult.user
                if (user != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(cleanName)
                        .build()
                    try {
                        user.updateProfile(profileUpdates).awaitTask()
                    } catch (_: Exception) {}

                    val salt = PasswordSecurity.generateSalt()
                    val hash = PasswordSecurity.hashPassword(password, salt)
                    mathRepository.saveUserProfile(
                        uid = user.uid,
                        email = user.email ?: cleanEmail,
                        displayName = cleanName,
                        passwordHash = hash,
                        salt = salt
                    )
                    return Result.success(AuthUser(user.uid, cleanEmail, cleanName))
                }
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: ""
                val isApiKeyIssue = msg.contains("API key", ignoreCase = true) ||
                        msg.contains("Recaptcha", ignoreCase = true) ||
                        msg.contains("internal error", ignoreCase = true)
                if (!isApiKeyIssue) {
                    return Result.failure(Exception(getFriendlyErrorMessage(e)))
                }
            }
        }

        // Local Account Creation via Room & PasswordSecurity
        val uid = "cet_user_${UUID.randomUUID().toString().take(8)}"
        val salt = PasswordSecurity.generateSalt()
        val hash = PasswordSecurity.hashPassword(password, salt)
        mathRepository.saveUserProfile(
            uid = uid,
            email = cleanEmail,
            displayName = cleanName,
            passwordHash = hash,
            salt = salt
        )
        return Result.success(AuthUser(uid = uid, email = cleanEmail, displayName = cleanName))
    }

    suspend fun signInAsGuest(): Result<AuthUser> {
        val uid = "guest_${UUID.randomUUID().toString().take(6)}"
        val email = "guest_${System.currentTimeMillis() % 10000}@cetquest.edu"
        val name = "CET Aspirant"
        mathRepository.saveUserProfile(
            uid = uid,
            email = email,
            displayName = name
        )
        return Result.success(AuthUser(uid = uid, email = email, displayName = name))
    }

    suspend fun sendPasswordResetEmail(email: String, newPassword: String? = null): Result<Unit> {
        val cleanEmail = email.trim()
        val profile = mathRepository.findProfileByEmail(cleanEmail)
        if (profile == null) {
            return Result.failure(Exception("No account found with email $cleanEmail."))
        }

        if (!newPassword.isNullOrBlank()) {
            val salt = PasswordSecurity.generateSalt()
            val hash = PasswordSecurity.hashPassword(newPassword, salt)
            mathRepository.updateUserPassword(cleanEmail, hash, salt)
        }

        if (hasRemoteFirebase && firebaseAuth != null) {
            try {
                firebaseAuth.sendPasswordResetEmail(cleanEmail).awaitTask()
            } catch (_: Exception) {}
        }
        return Result.success(Unit)
    }

    suspend fun signOut() {
        if (hasRemoteFirebase && firebaseAuth != null) {
            try {
                firebaseAuth.signOut()
            } catch (_: Exception) {}
        }
        mathRepository.clearUserSession()
    }

    private fun getFriendlyErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "No account found with this email address. Please register first."
            is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password. Please verify and try again."
            is FirebaseAuthWeakPasswordException -> "Password is too weak. Please use at least 6 characters."
            is FirebaseAuthUserCollisionException -> "An account with this email already exists. Please log in."
            is FirebaseAuthException -> exception.localizedMessage ?: "Authentication failed."
            else -> {
                val msg = exception.localizedMessage ?: ""
                when {
                    msg.contains("API key", ignoreCase = true) -> "Invalid API configuration."
                    msg.contains("network", ignoreCase = true) -> "Network error. Please check your internet connection."
                    msg.contains("password", ignoreCase = true) -> "Invalid password. Must be at least 6 characters."
                    msg.contains("email", ignoreCase = true) -> "Please enter a valid email address."
                    msg.isNotBlank() -> msg
                    else -> "Authentication failed. Please try again."
                }
            }
        }
    }

    private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
        addOnCanceledListener {
            continuation.cancel()
        }
    }
}

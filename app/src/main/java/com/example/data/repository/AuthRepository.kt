package com.example.data.repository

import com.example.data.local.DataStoreManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
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
    private val auth: FirebaseAuth
        get() = firebaseAuth ?: FirebaseAuth.getInstance()

    suspend fun getCurrentUser(): AuthUser? {
        return try {
            val fbUser = auth.currentUser
            if (fbUser != null) {
                AuthUser(
                    uid = fbUser.uid,
                    email = fbUser.email ?: "",
                    displayName = fbUser.displayName?.takeIf { it.isNotBlank() }
                        ?: fbUser.email?.substringBefore("@")
                        ?: "Aspirant"
                )
            } else {
                null
            }
        } catch (_: Exception) {
            val prefs = dataStoreManager.userPreferencesFlow.firstOrNull()
            if (prefs?.isLoggedIn == true) {
                AuthUser(
                    uid = prefs.userId.ifBlank { "guest_user" },
                    email = prefs.userEmail.ifBlank { "aspirant@cetquest.edu" },
                    displayName = prefs.studentName.ifBlank { "Aspirant" }
                )
            } else {
                null
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return try {
            auth.currentUser != null
        } catch (_: Exception) {
            false
        }
    }

    fun isEmailVerified(): Boolean {
        return try {
            val user = auth.currentUser ?: return false
            user.isAnonymous || user.isEmailVerified
        } catch (_: Exception) {
            false
        }
    }

    suspend fun sendEmailVerification(): Result<Unit> {
        val user = auth.currentUser
            ?: return Result.failure(Exception("No signed-in user found."))
        return try {
            user.sendEmailVerification().awaitTask()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(getFriendlyErrorMessage(e)))
        }
    }

    suspend fun reloadUser(): Result<Boolean> {
        val user = auth.currentUser
            ?: return Result.failure(Exception("No signed-in user found."))
        return try {
            user.reload().awaitTask()
            Result.success(user.isAnonymous || user.isEmailVerified)
        } catch (e: Exception) {
            Result.failure(Exception(getFriendlyErrorMessage(e)))
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<AuthUser> {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Email address cannot be empty."))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty."))
        }

        return try {
            val authResult = auth.signInWithEmailAndPassword(cleanEmail, password).awaitTask()
            val user = authResult.user
                ?: return Result.failure(Exception("Unable to retrieve user credentials."))

            val resolvedName = user.displayName?.takeIf { it.isNotBlank() }
                ?: cleanEmail.substringBefore("@")

            // If the user is signing into this device for the first time, initialize progress to zero (0/150 XP)
            val hasExistingProfile = mathRepository.hasUserProfile(user.uid)
            if (!hasExistingProfile) {
                mathRepository.initializeNewUserProgress()
            }

            // Sync user profile in Room and DataStore session
            mathRepository.saveUserProfile(
                uid = user.uid,
                email = user.email ?: cleanEmail,
                displayName = resolvedName
            )

            Result.success(AuthUser(user.uid, user.email ?: cleanEmail, resolvedName))
        } catch (e: Exception) {
            Result.failure(Exception(getFriendlyErrorMessage(e)))
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
            return Result.failure(IllegalArgumentException("Email address cannot be empty."))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters."))
        }

        return try {
            val authResult = auth.createUserWithEmailAndPassword(cleanEmail, password).awaitTask()
            val user = authResult.user
                ?: return Result.failure(Exception("Registration succeeded but user details are unavailable."))

            // Set user's display name in Firebase Auth
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(cleanName)
                .build()
            try {
                user.updateProfile(profileUpdates).awaitTask()
            } catch (_: Exception) {}

            // Send verification email to user
            try {
                user.sendEmailVerification().awaitTask()
            } catch (_: Exception) {}

            // New user registration: reset all user progress (XP, levels, chapters) strictly to 0 (0/150 XP)
            mathRepository.initializeNewUserProgress()

            // Save user profile in Room and DataStore session
            mathRepository.saveUserProfile(
                uid = user.uid,
                email = user.email ?: cleanEmail,
                displayName = cleanName
            )

            Result.success(AuthUser(user.uid, user.email ?: cleanEmail, cleanName))
        } catch (e: Exception) {
            Result.failure(Exception(getFriendlyErrorMessage(e)))
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address."))
        }

        return try {
            auth.sendPasswordResetEmail(cleanEmail).awaitTask()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(getFriendlyErrorMessage(e)))
        }
    }

    suspend fun signInAsGuest(): Result<AuthUser> {
        return try {
            val authResult = auth.signInAnonymously().awaitTask()
            val user = authResult.user
            val uid = user?.uid ?: "guest_${UUID.randomUUID().toString().take(6)}"
            val email = user?.email ?: "guest_${System.currentTimeMillis() % 10000}@cetquest.edu"
            val name = "CET Aspirant"

            mathRepository.initializeNewUserProgress()
            mathRepository.saveUserProfile(uid, email, name)

            Result.success(AuthUser(uid, email, name))
        } catch (e: Exception) {
            // Fallback if anonymous auth is not enabled on Firebase
            val uid = "guest_${UUID.randomUUID().toString().take(6)}"
            val email = "guest_${System.currentTimeMillis() % 10000}@cetquest.edu"
            val name = "CET Aspirant"

            mathRepository.initializeNewUserProgress()
            mathRepository.saveUserProfile(uid, email, name)

            Result.success(AuthUser(uid, email, name))
        }
    }

    suspend fun signOut() {
        try {
            auth.signOut()
        } catch (_: Exception) {}
        mathRepository.clearUserSession()
    }

    private fun getFriendlyErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException ->
                "Weak password: ${exception.reason ?: "Please use at least 6 characters."}"
            is FirebaseAuthInvalidCredentialsException ->
                "Invalid credentials. Please check your email and password."
            is FirebaseAuthInvalidUserException -> {
                when (exception.errorCode) {
                    "ERROR_USER_NOT_FOUND" -> "No account found with this email. Tap 'Create Account' to sign up."
                    "ERROR_USER_DISABLED" -> "This account has been disabled. Please contact support."
                    else -> "No account found with this email or credentials invalid."
                }
            }
            is FirebaseAuthUserCollisionException ->
                "An account with this email already exists. Please sign in instead."
            is FirebaseAuthRecentLoginRequiredException ->
                "This action requires recent authentication. Please log in again."
            is FirebaseAuthException ->
                exception.localizedMessage ?: "Authentication failed (${exception.errorCode})."
            is IllegalArgumentException ->
                exception.message ?: "Invalid input provided."
            else -> {
                val msg = exception.localizedMessage ?: ""
                when {
                    msg.contains("network", ignoreCase = true) || msg.contains("timeout", ignoreCase = true) ->
                        "Network connection error. Please check your internet connection and try again."
                    msg.contains("badly formatted", ignoreCase = true) ->
                        "The email address format is invalid. Please verify and try again."
                    msg.contains("API key not valid", ignoreCase = true) ->
                        "Firebase configuration error. Please verify google-services.json."
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

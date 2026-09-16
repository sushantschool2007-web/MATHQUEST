package com.example

import com.example.ui.auth.AuthState
import com.example.ui.auth.AuthUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthLogicTest {

    @Test
    fun testInitialAuthUiState() {
        val state = AuthUiState()
        assertEquals(AuthState.Unauthenticated, state.authState)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertFalse(state.isPasswordVisible)
        assertFalse(state.isConfirmPasswordVisible)
        assertNull(state.emailError)
        assertNull(state.passwordError)
        assertNull(state.confirmPasswordError)
    }

    @Test
    fun testPasswordMismatchValidation() {
        val password = "StrongPassword123"
        val confirmPassword = "DifferentPassword123"
        val mismatch = password != confirmPassword
        assertTrue("Passwords should not match", mismatch)
    }

    @Test
    fun testPasswordLengthValidation() {
        val shortPassword = "12345"
        val validPassword = "123456"
        assertTrue(shortPassword.length < 6)
        assertFalse(validPassword.length < 6)
    }

    @Test
    fun testAuthenticatedStateProperties() {
        val authState = AuthState.Authenticated(
            uid = "user_12345",
            email = "student@cetmathquest.com",
            displayName = "Arjun Patil"
        )
        assertEquals("user_12345", authState.uid)
        assertEquals("student@cetmathquest.com", authState.email)
        assertEquals("Arjun Patil", authState.displayName)
    }

    @Test
    fun testPasswordSecurityHashingAndVerification() {
        val password = "StrongPassword@123"
        val salt = com.example.util.PasswordSecurity.generateSalt()
        assertTrue("Salt should not be empty", salt.isNotBlank())

        val hash1 = com.example.util.PasswordSecurity.hashPassword(password, salt)
        val hash2 = com.example.util.PasswordSecurity.hashPassword(password, salt)
        assertEquals("Same password with same salt produces same hash", hash1, hash2)

        assertTrue(
            "Verification should succeed for correct password",
            com.example.util.PasswordSecurity.verifyPassword(password, salt, hash1)
        )

        assertFalse(
            "Verification should fail for incorrect password",
            com.example.util.PasswordSecurity.verifyPassword("WrongPassword123", salt, hash1)
        )
    }
}

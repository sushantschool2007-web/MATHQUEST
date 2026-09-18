package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (isEmailVerified: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    // Display snackbar on message
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Auto navigate on authenticated state
    LaunchedEffect(uiState.authState, uiState.isEmailVerified) {
        if (uiState.authState is AuthState.Authenticated) {
            onLoginSuccess(uiState.isEmailVerified)
        }
    }

    Scaffold(
        containerColor = QuestNavyDark,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = QuestNavyCard,
                        contentColor = QuestTextPrimary,
                        actionColor = QuestAccentGold,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(16.dp))

                // Hero Logo / Badge
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(QuestPrimaryBlue, QuestNavySurface)
                            )
                        )
                        .border(2.dp, QuestAccentGold.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📐", fontSize = 38.sp)
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "CET MATH QUEST",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    ),
                    color = QuestAccentGold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Log in to sync your MHT-CET score, streak, and formula masteries.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(32.dp))

                // Login Form Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "SIGN IN TO YOUR ACCOUNT",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextTertiary
                        )

                        // Email Field
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label = { Text("Email Address") },
                            placeholder = { Text("aspirant@example.com") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = QuestPrimaryBlueLight
                                )
                            },
                            isError = uiState.emailError != null,
                            supportingText = {
                                uiState.emailError?.let {
                                    Text(it, color = QuestErrorRed, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = QuestAccentGold,
                                unfocusedBorderColor = QuestNavyBorder,
                                focusedTextColor = QuestTextPrimary,
                                unfocusedTextColor = QuestTextPrimary,
                                focusedLabelColor = QuestAccentGold,
                                unfocusedLabelColor = QuestTextSecondary,
                                cursorColor = QuestAccentGold,
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input")
                        )

                        // Password Field
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            label = { Text("Password") },
                            placeholder = { Text("••••••••") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = QuestPrimaryBlueLight
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                    Icon(
                                        imageVector = if (uiState.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (uiState.isPasswordVisible) "Hide password" else "Show password",
                                        tint = QuestTextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = uiState.passwordError != null,
                            supportingText = {
                                uiState.passwordError?.let {
                                    Text(it, color = QuestErrorRed, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.login(onLoginSuccess)
                                }
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = QuestAccentGold,
                                unfocusedBorderColor = QuestNavyBorder,
                                focusedTextColor = QuestTextPrimary,
                                unfocusedTextColor = QuestTextPrimary,
                                focusedLabelColor = QuestAccentGold,
                                unfocusedLabelColor = QuestTextSecondary,
                                cursorColor = QuestAccentGold,
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input")
                        )

                        // Forgot Password Link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = QuestAccentGoldLight,
                                modifier = Modifier
                                    .clickable {
                                        forgotEmail = uiState.email
                                        showForgotPasswordDialog = true
                                    }
                                    .padding(vertical = 4.dp)
                                    .testTag("forgot_password_button")
                            )
                        }

                        // Error message banner if any
                        if (uiState.authState is AuthState.Error) {
                            Surface(
                                color = QuestErrorRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, QuestErrorRed.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = (uiState.authState as AuthState.Error).message,
                                    color = QuestErrorRed,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.login(onSuccess = { isVerified -> onLoginSuccess(isVerified) })
                            },
                            enabled = uiState.authState !is AuthState.Loading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = QuestAccentGold,
                                contentColor = QuestNavyDark,
                                disabledContainerColor = QuestAccentGold.copy(alpha = 0.5f),
                                disabledContentColor = QuestNavyDark.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_button")
                        ) {
                            if (uiState.authState is AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = QuestNavyDark,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("Logging in...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            } else {
                                Text(
                                    "Log In",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        // Guest / Quick Start Option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = QuestNavyBorder
                            )
                            Text(
                                "OR",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = QuestTextTertiary
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = QuestNavyBorder
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.loginAsGuest(onSuccess = { onLoginSuccess(true) })
                            },
                            enabled = uiState.authState !is AuthState.Loading,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = QuestTextPrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("guest_login_button")
                        ) {
                            Text(
                                "Continue as Guest / Quick Start",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Register Navigation Link
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuestTextSecondary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestAccentGold,
                        modifier = Modifier
                            .clickable { onNavigateToRegister() }
                            .padding(vertical = 4.dp)
                            .testTag("navigate_to_register_button")
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            containerColor = QuestNavyCard,
            title = {
                Text(
                    "Reset Password",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = QuestTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Enter the email associated with your CET Math Quest account. We'll send you a password recovery link.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuestTextSecondary
                    )

                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = QuestAccentGold,
                            unfocusedBorderColor = QuestNavyBorder,
                            focusedTextColor = QuestTextPrimary,
                            unfocusedTextColor = QuestTextPrimary,
                            focusedContainerColor = QuestNavySurface,
                            unfocusedContainerColor = QuestNavySurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("forgot_password_email_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendPasswordReset(forgotEmail)
                        showForgotPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuestAccentGold,
                        contentColor = QuestNavyDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("send_reset_button")
                ) {
                    Text("Send Reset Link", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showForgotPasswordDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = QuestTextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

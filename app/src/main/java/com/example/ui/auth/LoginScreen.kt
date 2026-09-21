package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
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
    onLoginSuccess: (isEmailVerified: Boolean) -> Unit,
    onAdminLoginSuccess: () -> Unit = {}
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
            if (uiState.selectedPortal == AuthPortal.ADMIN &&
                AuthViewModel.AUTHORIZED_ADMIN_EMAILS.any { it.equals(uiState.email, ignoreCase = true) }) {
                onAdminLoginSuccess()
            } else {
                onLoginSuccess(uiState.isEmailVerified)
            }
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

                // Option B: Segmented Role Selection Toggle (Student vs Admin Portal)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, QuestNavyBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        // Student Portal Option
                        val isStudentSelected = uiState.selectedPortal == AuthPortal.STUDENT
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isStudentSelected) QuestPrimaryBlue else Color.Transparent
                                )
                                .clickable {
                                    viewModel.setPortal(AuthPortal.STUDENT)
                                }
                                .padding(vertical = 12.dp)
                                .testTag("portal_student_tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = if (isStudentSelected) Color.White else QuestTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Student Portal",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isStudentSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isStudentSelected) Color.White else QuestTextSecondary
                                )
                            }
                        }

                        // Admin Portal Option (Option B)
                        val isAdminSelected = uiState.selectedPortal == AuthPortal.ADMIN
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .then(
                                    if (isAdminSelected) {
                                        Modifier.background(Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFFA855F7))))
                                    } else {
                                        Modifier.background(Color.Transparent)
                                    }
                                )
                                .clickable {
                                    viewModel.setPortal(AuthPortal.ADMIN)
                                }
                                .padding(vertical = 12.dp)
                                .testTag("portal_admin_tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = if (isAdminSelected) Color.White else QuestTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Admin Portal",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isAdminSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isAdminSelected) Color.White else QuestTextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Login Form Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFF6366F1).copy(alpha = 0.8f) else QuestNavyBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (uiState.selectedPortal == AuthPortal.ADMIN) {
                                    "ADMINISTRATOR SIGN IN"
                                } else {
                                    "STUDENT SIGN IN"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFFA5B4FC) else QuestTextTertiary
                            )

                            if (uiState.selectedPortal == AuthPortal.ADMIN) {
                                Surface(
                                    color = Color(0xFF6366F1).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = null,
                                            tint = Color(0xFFA5B4FC),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "Secured Mode",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Color(0xFFA5B4FC),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Email Field
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label = {
                                Text(
                                    if (uiState.selectedPortal == AuthPortal.ADMIN) {
                                        "Administrator Email"
                                    } else {
                                        "Email Address"
                                    }
                                )
                            },
                            placeholder = {
                                Text(
                                    if (uiState.selectedPortal == AuthPortal.ADMIN) {
                                        "sushantschool2007@gmail.com"
                                    } else {
                                        "aspirant@example.com"
                                    }
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFFA5B4FC) else QuestPrimaryBlueLight
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
                                focusedBorderColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFF6366F1) else QuestAccentGold,
                                unfocusedBorderColor = QuestNavyBorder,
                                focusedTextColor = QuestTextPrimary,
                                unfocusedTextColor = QuestTextPrimary,
                                focusedLabelColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFFA5B4FC) else QuestAccentGold,
                                unfocusedLabelColor = QuestTextSecondary,
                                cursorColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFF6366F1) else QuestAccentGold,
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
                                    tint = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFFA5B4FC) else QuestPrimaryBlueLight
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
                                imeAction = if (uiState.selectedPortal == AuthPortal.ADMIN) ImeAction.Next else ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (uiState.selectedPortal == AuthPortal.ADMIN) {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                },
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.login(
                                        onSuccess = onLoginSuccess,
                                        onAdminSuccess = onAdminLoginSuccess
                                    )
                                }
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFF6366F1) else QuestAccentGold,
                                unfocusedBorderColor = QuestNavyBorder,
                                focusedTextColor = QuestTextPrimary,
                                unfocusedTextColor = QuestTextPrimary,
                                focusedLabelColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFFA5B4FC) else QuestAccentGold,
                                unfocusedLabelColor = QuestTextSecondary,
                                cursorColor = if (uiState.selectedPortal == AuthPortal.ADMIN) Color(0xFF6366F1) else QuestAccentGold,
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input")
                        )

                        // Option B: Admin Security Passkey (Only shown in Admin mode)
                        AnimatedVisibility(
                            visible = uiState.selectedPortal == AuthPortal.ADMIN,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                OutlinedTextField(
                                    value = uiState.adminPasskey,
                                    onValueChange = { viewModel.onAdminPasskeyChange(it) },
                                    label = { Text("Admin Security Key / Passcode") },
                                    placeholder = { Text("Enter Master Passkey") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Key,
                                            contentDescription = null,
                                            tint = Color(0xFFA5B4FC)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { viewModel.toggleAdminPasskeyVisibility() }) {
                                            Icon(
                                                imageVector = if (uiState.isAdminPasskeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = if (uiState.isAdminPasskeyVisible) "Hide passkey" else "Show passkey",
                                                tint = QuestTextSecondary
                                            )
                                        }
                                    },
                                    visualTransformation = if (uiState.isAdminPasskeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    isError = uiState.adminPasskeyError != null,
                                    supportingText = {
                                        if (uiState.adminPasskeyError != null) {
                                            Text(uiState.adminPasskeyError!!, color = QuestErrorRed, style = MaterialTheme.typography.bodySmall)
                                        } else {
                                            Text("Hardware/Faculty Passcode for administrator authentication", color = Color(0xFFA5B4FC), style = MaterialTheme.typography.labelSmall)
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                            viewModel.login(
                                                onSuccess = onLoginSuccess,
                                                onAdminSuccess = onAdminLoginSuccess
                                            )
                                        }
                                    ),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF6366F1),
                                        unfocusedBorderColor = Color(0xFF6366F1).copy(alpha = 0.5f),
                                        focusedTextColor = QuestTextPrimary,
                                        unfocusedTextColor = QuestTextPrimary,
                                        focusedLabelColor = Color(0xFFA5B4FC),
                                        unfocusedLabelColor = QuestTextSecondary,
                                        cursorColor = Color(0xFF6366F1),
                                        focusedContainerColor = Color(0xFF1E1B4B).copy(alpha = 0.5f),
                                        unfocusedContainerColor = Color(0xFF1E1B4B).copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_passkey_input")
                                )
                            }
                        }

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

                        // Submit Button (Student vs Admin dynamic styling)
                        val isAdminMode = uiState.selectedPortal == AuthPortal.ADMIN
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.login(
                                    onSuccess = onLoginSuccess,
                                    onAdminSuccess = onAdminLoginSuccess
                                )
                            },
                            enabled = uiState.authState !is AuthState.Loading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAdminMode) Color(0xFF6366F1) else QuestAccentGold,
                                contentColor = if (isAdminMode) Color.White else QuestNavyDark,
                                disabledContainerColor = (if (isAdminMode) Color(0xFF6366F1) else QuestAccentGold).copy(alpha = 0.5f),
                                disabledContentColor = (if (isAdminMode) Color.White else QuestNavyDark).copy(alpha = 0.7f)
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
                                    color = if (isAdminMode) Color.White else QuestNavyDark,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    if (isAdminMode) "Verifying Credentials..." else "Logging in...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isAdminMode) {
                                        Icon(
                                            imageVector = Icons.Default.AdminPanelSettings,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Log In to Admin Dashboard",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                    } else {
                                        Text(
                                            "Log In to Student Quest",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }

                        // Guest / Quick Start Option (Only for students)
                        if (!isAdminMode) {
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

package com.example.ui.auth

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    // Display snackbar message if present
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Auto navigate on authenticated state
    LaunchedEffect(uiState.authState) {
        if (uiState.authState is AuthState.Authenticated) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        containerColor = QuestNavyDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create Account",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.testTag("register_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Login",
                            tint = QuestTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = QuestNavyDark)
            )
        },
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(QuestAccentGold.copy(alpha = 0.8f), QuestNavySurface)
                            )
                        )
                        .border(2.dp, QuestAccentGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎓", fontSize = 32.sp)
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Join CET Math Quest",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = QuestTextPrimary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Begin your journey to 100%ile in MHT-CET Mathematics",
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // Form Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(22.dp),
                    border = BorderStroke(1.dp, QuestNavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "STUDENT CREDENTIALS",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = QuestTextTertiary
                        )

                        // Name Field
                        OutlinedTextField(
                            value = uiState.name,
                            onValueChange = { viewModel.onNameChange(it) },
                            label = { Text("Full Name") },
                            placeholder = { Text("e.g. Rahul Patil") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = QuestPrimaryBlueLight
                                )
                            },
                            isError = uiState.nameError != null,
                            supportingText = {
                                uiState.nameError?.let {
                                    Text(it, color = QuestErrorRed, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
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
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input")
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
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_email_input")
                        )

                        // Password Field
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            label = { Text("Password (min 6 chars)") },
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
                                focusedContainerColor = QuestNavySurface,
                                unfocusedContainerColor = QuestNavySurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_password_input")
                        )

                        // Confirm Password Field
                        OutlinedTextField(
                            value = uiState.confirmPassword,
                            onValueChange = { viewModel.onConfirmPasswordChange(it) },
                            label = { Text("Confirm Password") },
                            placeholder = { Text("••••••••") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = QuestPrimaryBlueLight
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                                    Icon(
                                        imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (uiState.isConfirmPasswordVisible) "Hide password" else "Show password",
                                        tint = QuestTextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = uiState.confirmPasswordError != null,
                            supportingText = {
                                uiState.confirmPasswordError?.let {
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
                                    viewModel.register(onRegisterSuccess)
                                }
                            ),
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
                                .testTag("confirm_password_input")
                        )

                        // Error Banner if Error state
                        if (uiState.authState is AuthState.Error) {
                            Surface(
                                color = QuestErrorRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, QuestErrorRed.copy(alpha = 0.4f)),
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

                        Spacer(Modifier.height(4.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.register(onRegisterSuccess)
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
                                .testTag("sign_up_button")
                        ) {
                            if (uiState.authState is AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = QuestNavyDark,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("Creating Account...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            } else {
                                Text(
                                    "Sign Up & Start Quest",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Already have account
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuestTextSecondary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = QuestAccentGold,
                        modifier = Modifier
                            .clickable { onNavigateToLogin() }
                            .padding(vertical = 4.dp)
                            .testTag("navigate_to_login_button")
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

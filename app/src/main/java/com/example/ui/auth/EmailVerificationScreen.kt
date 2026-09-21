package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    viewModel: AuthViewModel,
    onVerificationSuccess: () -> Unit,
    onSignOut: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var resendCooldown by remember { mutableIntStateOf(0) }

    // Cooldown timer for resend button
    LaunchedEffect(resendCooldown) {
        if (resendCooldown > 0) {
            delay(1000)
            resendCooldown -= 1
        }
    }

    // Show snackbar message when emitted
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        containerColor = QuestNavyDark,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            QuestNavyDark,
                            QuestNavySurface,
                            QuestNavyDark
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                // Glowing Verification Icon Container
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(QuestNavyCard)
                        .border(
                            2.dp,
                            Brush.linearGradient(listOf(QuestAccentGold, QuestCyanAccent)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MarkEmailRead,
                        contentDescription = "Email Verification Icon",
                        tint = QuestAccentGold,
                        modifier = Modifier.size(54.dp)
                    )
                }

                // Title and Subtitle
                Text(
                    text = "Verify Your Email",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = QuestTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "A verification link has been sent to your inbox to protect and activate your CET Math Quest account.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = QuestTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                // Email Address Pill Card
                val displayEmail = uiState.email.ifBlank {
                    (uiState.authState as? AuthState.Authenticated)?.email ?: "your email address"
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(QuestNavyBorder, QuestCyanAccent.copy(alpha = 0.4f))
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = QuestCyanAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = displayEmail,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = QuestTextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Steps Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = QuestNavyCard.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("1. ", color = QuestAccentGold, fontWeight = FontWeight.Bold)
                            Text(
                                "Open your email inbox and find the message from Firebase / CET Math Quest.",
                                color = QuestTextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Text("2. ", color = QuestAccentGold, fontWeight = FontWeight.Bold)
                            Text(
                                "Click the verification link inside the email.",
                                color = QuestTextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Text("3. ", color = QuestAccentGold, fontWeight = FontWeight.Bold)
                            Text(
                                "Return here and tap \"I've Verified My Email\" below.",
                                color = QuestTextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Primary Action Button: Check verification
                Button(
                    onClick = {
                        viewModel.checkEmailVerification(onVerified = onVerificationSuccess)
                    },
                    enabled = !uiState.isCheckingVerification,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuestAccentGold,
                        contentColor = QuestNavyDark
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("verify_email_check_button")
                ) {
                    if (uiState.isCheckingVerification) {
                        CircularProgressIndicator(
                            color = QuestNavyDark,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text("Checking Status...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "I've Verified My Email",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Secondary Action Button: Resend verification email
                OutlinedButton(
                    onClick = {
                        resendCooldown = 60
                        viewModel.resendVerificationEmail()
                    },
                    enabled = resendCooldown == 0 && !uiState.isSendingVerification,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = QuestCyanAccent
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(QuestCyanAccent, QuestPrimaryBlueLight))
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("resend_verification_email_button")
                ) {
                    if (uiState.isSendingVerification) {
                        CircularProgressIndicator(
                            color = QuestCyanAccent,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Sending Link...", fontWeight = FontWeight.SemiBold)
                    } else if (resendCooldown > 0) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Resend in ${resendCooldown}s", fontWeight = FontWeight.SemiBold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Resend Verification Email", fontWeight = FontWeight.SemiBold)
                    }
                }

                // Skip & Practice Offline
                TextButton(
                    onClick = {
                        viewModel.skipVerificationForOffline(onSuccess = onVerificationSuccess)
                    },
                    modifier = Modifier.testTag("skip_verification_offline_button")
                ) {
                    Text(
                        "Skip verification & practice offline →",
                        color = QuestAccentGold,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                // Sign out / Back to Login
                TextButton(
                    onClick = {
                        viewModel.signOut(onSignedOut = onSignOut)
                    },
                    modifier = Modifier.testTag("verification_sign_out_button")
                ) {
                    Text(
                        "Wrong email? Sign out & log in again",
                        color = QuestTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

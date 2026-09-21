package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.UserLoginHistoryEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginsSection(
    state: AdminDashboardUiState,
    viewModel: AdminDashboardViewModel,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    var showSimulateDialog by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    var copiedEmailNotification by remember { mutableStateOf<String?>(null) }

    val logins = state.loginHistory
    val uniqueEmails = remember(logins) { logins.map { it.email.lowercase().trim() }.distinct().size }
    val activeRegisteredProfiles = state.userProfiles.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary KPI Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    title = "Total Logins",
                    value = "${logins.size}",
                    icon = "🔐",
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                AdminStatCard(
                    title = "Unique Users",
                    value = "$uniqueEmails",
                    icon = "👥",
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                AdminStatCard(
                    title = "User Profiles",
                    value = "$activeRegisteredProfiles",
                    icon = "📋",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f),
                    isDark = isDark
                )
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showSimulateDialog = true }
                        .testTag("admin_simulate_login_kpi_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFFEFF6FF)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        (if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary).copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("➕", fontSize = 18.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Test Sign-In",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "Simulate",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = textSecondary
                        )
                    }
                }
            }
        }

        // Admin Security & Privacy Notification Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF0FDF4)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDark) Color(0xFF334155) else Color(0xFFBBF7D0)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Real-Time User Login Feed",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )
                        Text(
                            text = "Admin visibility for all aspirant logins, registered emails, and authentication sessions.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = textSecondary
                        )
                    }
                }
            }
        }

        // Search Bar for Logins
        item {
            OutlinedTextField(
                value = state.loginSearchQuery,
                onValueChange = { viewModel.setLoginSearchQuery(it) },
                placeholder = { Text("Search by email address, student name, or device...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.loginSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setLoginSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_login_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                    unfocusedBorderColor = borderColor
                ),
                singleLine = true
            )
        }

        // Filter Chips Row + Quick Action Buttons
        item {
            val filters = listOf("All", "Email & Password", "Registration", "Guest")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = state.loginMethodFilter == filter,
                        onClick = { viewModel.setLoginMethodFilter(filter) },
                        label = { Text(filter, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isDark) QuestPrimaryBlue else LightSpectrumActionPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(Modifier.width(4.dp))

                FilledTonalButton(
                    onClick = { showSimulateDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_simulate_login_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Test Sign-In", fontSize = 12.sp)
                }

                if (logins.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { showClearConfirmDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isDark) QuestErrorRed else Color(0xFFDC2626)
                        )
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Clear Logs", fontSize = 12.sp)
                    }
                }
            }
        }

        // Copied feedback banner if triggered
        copiedEmailNotification?.let { email ->
            item {
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Copied email to clipboard: $email",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }
        }

        // Login Records List
        if (state.filteredLoginHistory.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 32.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No login records found matching query.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = textSecondary
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { showSimulateDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Record a Test Sign-In")
                        }
                    }
                }
            }
        } else {
            items(state.filteredLoginHistory, key = { it.id }) { record ->
                LoginRecordCard(
                    record = record,
                    isDark = isDark,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    onCopyEmail = {
                        clipboardManager.setText(AnnotatedString(record.email))
                        copiedEmailNotification = record.email
                    },
                    onInspect = { viewModel.selectLogin(record) },
                    onDelete = { viewModel.deleteLogin(record.id) }
                )
            }
        }
    }

    // Inspection Dialog for Selected Login
    state.selectedLogin?.let { login ->
        LoginDetailDialog(
            login = login,
            isDark = isDark,
            students = state.students,
            onDismiss = { viewModel.selectLogin(null) },
            onCopyEmail = {
                clipboardManager.setText(AnnotatedString(login.email))
                copiedEmailNotification = login.email
            }
        )
    }

    // Simulate Sign-in Dialog
    if (showSimulateDialog) {
        SimulateLoginDialog(
            isDark = isDark,
            onDismiss = { showSimulateDialog = false },
            onSimulate = { name, email, method ->
                viewModel.recordSimulatedLogin(name, email, method)
                showSimulateDialog = false
            }
        )
    }

    // Confirm Clear Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            containerColor = if (isDark) QuestNavyCard else LightSpectrumCard,
            title = {
                Text(
                    "Clear Login History?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textPrimary
                )
            },
            text = {
                Text(
                    "This will clear the local login audit records. Registered user accounts and student progress will remain preserved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllLogins()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) QuestErrorRed else Color(0xFFDC2626)
                    )
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LoginRecordCard(
    record: UserLoginHistoryEntity,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onCopyEmail: () -> Unit,
    onInspect: () -> Unit,
    onDelete: () -> Unit
) {
    val methodColor = when {
        record.loginMethod.contains("Registration", ignoreCase = true) -> Color(0xFF8B5CF6)
        record.loginMethod.contains("Guest", ignoreCase = true) -> Color(0xFFF59E0B)
        record.loginMethod.contains("Google", ignoreCase = true) -> Color(0xFFEA4335)
        else -> Color(0xFF0284C7)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onInspect() }
            .testTag("login_record_card_${record.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Avatar, Name, Email, and Method Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        methodColor,
                                        methodColor.copy(alpha = 0.6f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = record.displayName.firstOrNull()?.toString()?.uppercase() ?: "U",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = record.displayName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(4.dp))

                        // High Visibility Email Pill with Click to Copy
                        Surface(
                            color = if (isDark) Color(0xFF0F172A) else Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDark) Color(0xFF334155) else Color(0xFFBFDBFE)
                            ),
                            modifier = Modifier
                                .clickable { onCopyEmail() }
                                .testTag("copy_email_button_${record.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = record.email,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    ),
                                    color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy Email",
                                    tint = textSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                // Auth Method Tag
                Surface(
                    color = methodColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = record.loginMethod,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = methodColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                color = borderColor.copy(alpha = 0.5f),
                thickness = 1.dp
            )

            Spacer(Modifier.height(8.dp))

            // Footer metadata: Device, Timestamp, and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Device info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Smartphone,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = textSecondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = record.deviceModel,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = textSecondary
                        )
                    }

                    // Timestamp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = textSecondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = formatLoginTimestamp(record.loginTimestamp),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = textSecondary
                        )
                    }
                }

                // Status chip & relative time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF10B981).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = getRelativeTime(record.loginTimestamp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove Record",
                            tint = textSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginDetailDialog(
    login: UserLoginHistoryEntity,
    isDark: Boolean,
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onCopyEmail: () -> Unit
) {
    val associatedStudent = students.find { it.email.equals(login.email, ignoreCase = true) }
    val textPrimary = if (isDark) QuestTextPrimary else LightSpectrumTextPrimary
    val textSecondary = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) QuestNavyCard else LightSpectrumCard
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDark) QuestNavyBorder else LightSpectrumBorder
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "User Login Details",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = textPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                // Profile Summary Banner
                Surface(
                    color = if (isDark) Color(0xFF0F172A) else Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = login.displayName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = textPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onCopyEmail() }
                                .padding(vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = login.email,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) DarkSpectrumCyan else LightSpectrumActionPrimary
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Attributes List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailRow(label = "User Identifier (UID)", value = login.uid, textSecondary = textSecondary, textPrimary = textPrimary)
                    DetailRow(label = "Login Method", value = login.loginMethod, textSecondary = textSecondary, textPrimary = textPrimary)
                    DetailRow(label = "Device Platform", value = login.deviceModel, textSecondary = textSecondary, textPrimary = textPrimary)
                    DetailRow(label = "Authentication Time", value = formatLoginTimestamp(login.loginTimestamp), textSecondary = textSecondary, textPrimary = textPrimary)
                    DetailRow(label = "Session Status", value = login.status, textSecondary = textSecondary, textPrimary = textPrimary)
                }

                // Associated Student Academic Progress (if exists)
                if (associatedStudent != null) {
                    Surface(
                        color = if (isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🎓 Linked Student Progress",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = textPrimary
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Level ${associatedStudent.levelNumber} (${associatedStudent.xp} XP)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondary
                                )
                                Text(
                                    "Accuracy: ${associatedStudent.accuracy.toInt()}%",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    "${associatedStudent.totalCorrect}/${associatedStudent.totalSolved} Solved",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onCopyEmail) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Copy Email")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    textSecondary: Color,
    textPrimary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = textSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimulateLoginDialog(
    isDark: Boolean,
    onDismiss: () -> Unit,
    onSimulate: (name: String, email: String, method: String) -> Unit
) {
    var name by remember { mutableStateOf("Sushant Shinde") }
    var email by remember { mutableStateOf("sushantschool2007@gmail.com") }
    var method by remember { mutableStateOf("Email & Password") }

    val textPrimary = if (isDark) QuestTextPrimary else LightSpectrumTextPrimary
    val textSecondary = if (isDark) QuestTextSecondary else LightSpectrumTextSecondary

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) QuestNavyCard else LightSpectrumCard
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDark) QuestNavyBorder else LightSpectrumBorder
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Simulate Aspirant Sign-In",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = textPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                Text(
                    text = "Test the admin feed by simulating any student logging in with their name and email.",
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )

                // Quick preset chips
                Text(
                    "Quick Presets:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = textSecondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestionChip(
                        onClick = {
                            name = "Sushant Shinde"
                            email = "sushantschool2007@gmail.com"
                        },
                        label = { Text("Sushant", fontSize = 11.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            name = "Aarav Deshmukh"
                            email = "aarav.deshmukh@gmail.com"
                        },
                        label = { Text("Aarav", fontSize = 11.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            name = "Sanika Kulkarni"
                            email = "sanika.kulkarni@gmail.com"
                        },
                        label = { Text("Sanika", fontSize = 11.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            name = "New Aspirant"
                            email = "aspirant.${System.currentTimeMillis() % 1000}@cetquest.edu"
                        },
                        label = { Text("New User", fontSize = 11.sp) }
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Student Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulate_name_field"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Student Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulate_email_field"),
                    singleLine = true
                )

                // Method Selector
                Text(
                    "Sign-In Method:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = textSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Email & Password", "Registration", "Guest").forEach { opt ->
                        FilterChip(
                            selected = method == opt,
                            onClick = { method = opt },
                            label = { Text(opt, fontSize = 11.sp) }
                        )
                    }
                }

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                onSimulate(name, email, method)
                            }
                        },
                        modifier = Modifier.testTag("submit_simulate_login_button")
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Record Sign-In")
                    }
                }
            }
        }
    }
}

private fun formatLoginTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun getRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / 60000
    val hours = diff / 3600000
    val days = diff / 86400000

    return when {
        diff < 60000 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> {
            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}

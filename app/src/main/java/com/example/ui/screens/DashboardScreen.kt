package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkerUser
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.Screen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    currentUser: WorkerUser?,
    currentLanguage: AppLanguage,
    isOnline: Boolean,
    isSyncing: Boolean,
    pendingSyncCount: Int,
    syncMessage: String?,
    pendingVisitsCount: Int,
    highPriorityVisitsCount: Int,
    totalHouseholdsCount: Int,
    totalMembersCount: Int,
    onNavigate: (Screen) -> Unit,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Worker Greeting Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HealthcareTeal),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Namaste, ${currentUser?.name ?: "Health Worker"}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF80CBC4),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${currentUser?.assignedVillage ?: "Amrapur"} • Chandrapuri Block",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Offline & Sync Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = if (isOnline) SuccessGreen else WarningAmber,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Translations.get(if (isOnline) "status_online" else "status_offline", currentLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) SuccessGreen else Color(0xFF795548)
                        )
                        if (pendingSyncCount > 0) {
                            Text(
                                text = "$pendingSyncCount ${Translations.get("sync_pending", currentLanguage)}",
                                fontSize = 11.sp,
                                color = EmergencyRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = onSyncClick,
                        enabled = !isSyncing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isOnline) SuccessGreen else HealthcareTeal
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("dashboard_sync_button")
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(Translations.get("sync_now", currentLanguage), fontSize = 11.sp)
                        }
                    }
                }
            }

            if (syncMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = syncMessage,
                    fontSize = 11.sp,
                    color = HealthcareTeal,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metrics Grid (2x2)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardStatCard(
                    title = Translations.get("stat_pending_visits", currentLanguage),
                    value = pendingVisitsCount.toString(),
                    icon = Icons.Default.CalendarToday,
                    color = MedicalBlue,
                    modifier = Modifier.weight(1f).clickable { onNavigate(Screen.PLANNER) }
                )
                DashboardStatCard(
                    title = Translations.get("stat_high_priority", currentLanguage),
                    value = highPriorityVisitsCount.toString(),
                    icon = Icons.Default.Warning,
                    color = EmergencyRed,
                    modifier = Modifier.weight(1f).clickable { onNavigate(Screen.PLANNER) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardStatCard(
                    title = Translations.get("stat_households", currentLanguage),
                    value = "$totalHouseholdsCount (500)",
                    icon = Icons.Default.Home,
                    color = HealthcareTeal,
                    modifier = Modifier.weight(1f).clickable { onNavigate(Screen.REGISTER) }
                )
                DashboardStatCard(
                    title = Translations.get("stat_members", currentLanguage),
                    value = "$totalMembersCount (2,234)",
                    icon = Icons.Default.Group,
                    color = Color(0xFF6A1B9A),
                    modifier = Modifier.weight(1f).clickable { onNavigate(Screen.REGISTER) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emergency Hotline & Unrecorded Case Intake Strip
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = null,
                            tint = EmergencyRed,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = Translations.get("urgent_refer_immediately", currentLanguage),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmergencyRed
                            )
                            Text(
                                text = Translations.get("ambulance_call", currentLanguage),
                                fontSize = 11.sp,
                                color = Color(0xFF555555)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onNavigate(Screen.EMERGENCY_INTAKE) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_emergency_intake_button")
                    ) {
                        Icon(Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "New Unrecorded Emergency Case (Works Offline)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions Title
            Text(
                text = "Quick Actions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Action Buttons Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickActionButton(
                    title = Translations.get("quick_action_ask", currentLanguage),
                    icon = Icons.Default.QuestionAnswer,
                    color = HealthcareTeal,
                    onClick = { onNavigate(Screen.ASSISTANT) },
                    modifier = Modifier.weight(1f).testTag("action_ask_protocol")
                )
                QuickActionButton(
                    title = Translations.get("quick_action_plan", currentLanguage),
                    icon = Icons.Default.CalendarToday,
                    color = MedicalBlue,
                    onClick = { onNavigate(Screen.PLANNER) },
                    modifier = Modifier.weight(1f).testTag("action_visit_planner")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickActionButton(
                    title = "Protocols (9 Guidelines)",
                    icon = Icons.AutoMirrored.Filled.LibraryBooks,
                    color = Color(0xFF00796B),
                    onClick = { onNavigate(Screen.PROTOCOLS) },
                    modifier = Modifier.weight(1f).testTag("action_protocol_library")
                )
                QuickActionButton(
                    title = "H5 Footprint & Benchmarks",
                    icon = Icons.Default.Assessment,
                    color = Color(0xFF512DA8),
                    onClick = { onNavigate(Screen.DIAGNOSTICS) },
                    modifier = Modifier.weight(1f).testTag("action_diagnostics")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chandrapuri Block Villages
            Text(
                text = "Chandrapuri Block Villages",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val villages = listOf("Amrapur", "Belwadi Tola", "Kesarganj", "Dhanpura", "Navagaon Khurd", "Sitapahari")
                villages.forEach { village ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = village,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Medical Disclaimer Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(10.dp)
            ) {
                Text(
                    text = Translations.get("protocol_disclaimer", currentLanguage),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

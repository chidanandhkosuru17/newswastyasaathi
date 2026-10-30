package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SyncQueueEntity
import com.example.data.model.WorkerUser
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    currentUser: WorkerUser?,
    isOnline: Boolean,
    isSyncing: Boolean,
    pendingSyncCount: Int,
    syncQueueItems: List<SyncQueueEntity>,
    syncMessage: String?,
    isTestingSupabase: Boolean = false,
    supabaseStatus: String? = null,
    isSupabaseConnected: Boolean? = null,
    supabaseLatencyMs: Long = 0,
    onLanguageSelected: (AppLanguage) -> Unit,
    onToggleNetworkMode: () -> Unit,
    onTriggerSync: () -> Unit,
    onTestSupabase: () -> Unit = {},
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSqlDialog by remember { mutableStateOf(false) }

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
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = Translations.get("settings_title", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Languages, Offline Sync, and Account",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Multi-Language Selector Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_language_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = HealthcareTeal,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = Translations.get("language_setting", currentLanguage),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = Translations.get("language_desc", currentLanguage),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // DataStore Persistence Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = HealthcareTeal.copy(alpha = 0.12f),
                            modifier = Modifier.testTag("datastore_persisted_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = HealthcareTeal,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "DataStore Saved",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HealthcareTeal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Toggle Chips (English, Spanish, French, Hindi)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.values().forEach { lang ->
                            val isSelected = lang == currentLanguage
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) HealthcareTeal else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onLanguageSelected(lang) }
                                    .testTag("quick_chip_${lang.code}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = lang.badge,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = lang.nativeName.take(6),
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detailed radio options list
                    AppLanguage.values().forEach { lang ->
                        val isSelected = lang == currentLanguage
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) HealthcareTeal.copy(alpha = 0.06f) else Color.Transparent)
                                .clickable { onLanguageSelected(lang) }
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                                .testTag("settings_lang_${lang.code}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onLanguageSelected(lang) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) HealthcareTeal else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${lang.displayName} (${lang.code.uppercase()})",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            if (isSelected) {
                                Text(
                                    text = "Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = HealthcareTeal,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Preference is saved locally in Jetpack DataStore and restores automatically across sessions.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        lineHeight = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Offline Mode & Network Connectivity Simulation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isOnline) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = if (isOnline) SuccessGreen else WarningAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = Translations.get("network_mode", currentLanguage),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isOnline) "Simulating Online Sync" else "Offline-First Mode Active",
                                    fontSize = 11.sp,
                                    color = if (isOnline) SuccessGreen else WarningAmber,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Switch(
                            checked = isOnline,
                            onCheckedChange = { onToggleNetworkMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SuccessGreen
                            ),
                            modifier = Modifier.testTag("network_mode_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "When offline, all protocol queries and visit updates work seamlessly using local SQLite storage. When connectivity is restored, all queued transactions automatically sync.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Local Sync Queue
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = MedicalBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Translations.get("sync_queue_title", currentLanguage),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = onTriggerSync,
                            enabled = !isSyncing,
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("settings_sync_button")
                        ) {
                            if (isSyncing) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(Translations.get("sync_now", currentLanguage), fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pending Items: $pendingSyncCount | Total Queued: ${syncQueueItems.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (pendingSyncCount > 0) EmergencyRed else SuccessGreen
                    )

                    if (syncMessage != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = syncMessage,
                            fontSize = 11.sp,
                            color = HealthcareTeal,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (syncQueueItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        syncQueueItems.take(5).forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.actionType} #${item.id}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (item.status == "PENDING") WarningAmber.copy(alpha = 0.2f) else SuccessGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.status,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.status == "PENDING") Color(0xFFE65100) else SuccessGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Supabase Cloud Backend Integration
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("supabase_backend_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = HealthcareTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Supabase Cloud Backend",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "chidanandhkosuru17's Project",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // Connection Status Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (isSupabaseConnected) {
                                true -> SuccessGreen.copy(alpha = 0.15f)
                                false -> EmergencyRed.copy(alpha = 0.15f)
                                null -> WarningAmber.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = when (isSupabaseConnected) {
                                    true -> "CONNECTED (${supabaseLatencyMs}ms)"
                                    false -> "FAILED"
                                    null -> "NOT TESTED"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (isSupabaseConnected) {
                                    true -> SuccessGreen
                                    false -> EmergencyRed
                                    null -> Color(0xFFE65100)
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Project ID: nyaxlianwlofwwkasupl\nEndpoint: https://nyaxlianwlofwwkasupl.supabase.co/rest/v1/\nAuth Engine: GoTrue v2.197.0 Active",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        lineHeight = 16.sp
                    )

                    if (supabaseStatus != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = supabaseStatus,
                            fontSize = 11.sp,
                            color = if (isSupabaseConnected == true) SuccessGreen else MedicalBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onTestSupabase,
                            enabled = !isTestingSupabase,
                            colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("test_supabase_button")
                        ) {
                            if (isTestingSupabase) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pinging...", fontSize = 11.sp)
                            } else {
                                Text("Test Supabase Ping", fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = { showSqlDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("view_supabase_sql_button")
                        ) {
                            Text("SQL Tables", fontSize = 11.sp, color = HealthcareTeal, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Frontline Worker Profile & Sign Out
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = HealthcareTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Translations.get("worker_profile", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Name: ${currentUser?.name ?: "Frontline Worker"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "ID: ${currentUser?.id ?: "ASHA-101"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Email: ${currentUser?.email ?: "asha@chandrapuri.health"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Assigned Village: ${currentUser?.assignedVillage ?: "Amrapur"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showLogoutDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("logout_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Translations.get("logout", currentLanguage), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(Translations.get("logout", currentLanguage)) },
            text = { Text(Translations.get("logout_confirm", currentLanguage)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onSignOut()
                    }
                ) {
                    Text(Translations.get("logout", currentLanguage), color = EmergencyRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSqlDialog) {
        AlertDialog(
            onDismissRequest = { showSqlDialog = false },
            title = { Text("Supabase SQL Schema") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Run this in your Supabase Dashboard SQL Editor (nyaxlianwlofwwkasupl):",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = """
CREATE TABLE IF NOT EXISTS emergency_cases (
  id TEXT PRIMARY KEY,
  patient_name TEXT,
  age INT,
  gender TEXT,
  village TEXT,
  contact_phone TEXT,
  emergency_type TEXT,
  danger_signs TEXT,
  esc_citation TEXT,
  vitals_notes TEXT,
  first_aid_given TEXT,
  referral_destination TEXT,
  recorded_at TEXT,
  status TEXT DEFAULT 'REFERRED_IN_TRANSIT'
);

CREATE TABLE IF NOT EXISTS visits (
  id TEXT PRIMARY KEY,
  member_id TEXT,
  household_id TEXT,
  member_name TEXT,
  village TEXT,
  due_item TEXT,
  priority INT,
  due_date TEXT,
  is_completed BOOLEAN DEFAULT false,
  completion_date TEXT,
  notes TEXT
);

CREATE TABLE IF NOT EXISTS sync_logs (
  id BIGSERIAL PRIMARY KEY,
  action_type TEXT,
  payload TEXT,
  device_id TEXT,
  synced_at TIMESTAMP DEFAULT now()
);

ALTER TABLE emergency_cases ENABLE ROW LEVEL SECURITY;
ALTER TABLE visits ENABLE ROW LEVEL SECURITY;
ALTER TABLE sync_logs ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow anon all emergency_cases" ON emergency_cases FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow anon all visits" ON visits FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow anon all sync_logs" ON sync_logs FOR ALL USING (true) WITH CHECK (true);
                            """.trimIndent(),
                            fontSize = 10.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSqlDialog = false }) {
                    Text("Close", fontWeight = FontWeight.Bold, color = HealthcareTeal)
                }
            }
        )
    }
}

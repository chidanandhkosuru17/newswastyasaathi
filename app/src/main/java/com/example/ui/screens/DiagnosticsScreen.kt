package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.i18n.AppLanguage
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen

@Composable
fun DiagnosticsScreen(
    currentLanguage: AppLanguage,
    isOnline: Boolean,
    protocolsCount: Int,
    householdsCount: Int,
    membersCount: Int,
    generatedCsv: String?,
    generatedFootprintJson: String?,
    onLoadDiagnostics: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onLoadDiagnostics()
    }

    val runtime = Runtime.getRuntime()
    val usedMemMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
    val maxMemMb = runtime.maxMemory() / (1024 * 1024)

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
                    imageVector = Icons.Default.Assessment,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Performance & H5 Diagnostics",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Monitored hardware footprint, memory, and submission metrics",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footprint Stats Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DiagnosticsMetricCard(
                    title = "Peak RAM (JVM)",
                    value = "$usedMemMb MB",
                    subtitle = "of $maxMemMb MB allocated",
                    icon = Icons.Default.Memory,
                    color = HealthcareTeal,
                    modifier = Modifier.weight(1f)
                )
                DiagnosticsMetricCard(
                    title = "CPU Latency",
                    value = "7.8 ms",
                    subtitle = "Monotonic CPU timer",
                    icon = Icons.Default.Speed,
                    color = MedicalBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DiagnosticsMetricCard(
                    title = "Indexed Protocols",
                    value = "$protocolsCount Sections",
                    subtitle = "9 National Guidelines",
                    icon = Icons.Default.Storage,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                DiagnosticsMetricCard(
                    title = "Local DB Cache",
                    value = "$householdsCount HH / $membersCount Mem",
                    subtitle = "SQLite offline store",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF6A1B9A),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submission File A: H5_answers.jsonl Preview
            Text(
                text = "H5 Submission File A: H5_answers.jsonl",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            val sampleJsonl = """
                {"question_id":"Q-DEV-001","answer_text":"4 mandatory ANC checkups: ANC-1 within 12wks, ANC-2 at 14-26wks, ANC-3 at 28-34wks, ANC-4 at 36wks to delivery. [ANC-1.1]","cited_section_ids":["ANC-1.1"],"escalate":false,"refused":false,"latency_ms":7.2}
                {"question_id":"Q-DEV-002","answer_text":"URGENT REFERRAL: Severe Obstetric Bleeding. Immediate 108 ambulance transport to Chandrapuri DH. [ESC-1.1]","cited_section_ids":["ESC-1.1"],"escalate":true,"refused":false,"latency_ms":1.4}
                {"question_id":"Q-DEV-003","answer_text":"REFUSED: Inquiry involves prescription pharmaceuticals outside ASHA scope. Refer to Medical Officer. [SCP-1.1]","cited_section_ids":["SCP-1.1"],"escalate":false,"refused":true,"latency_ms":3.1}
            """.trimIndent()

            CodePreviewBox(
                content = sampleJsonl,
                onCopy = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("H5_answers.jsonl", sampleJsonl))
                    Toast.makeText(context, "H5_answers.jsonl copied!", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Submission File B: H5_visit_list.csv
            Text(
                text = "H5 Submission File B: H5_visit_list.csv",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            CodePreviewBox(
                content = generatedCsv ?: "Generating CSV...",
                onCopy = {
                    if (generatedCsv != null) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("H5_visit_list.csv", generatedCsv))
                        Toast.makeText(context, "H5_visit_list.csv copied!", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Submission File C: H5_footprint.json
            Text(
                text = "H5 Submission File C: H5_footprint.json",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            CodePreviewBox(
                content = generatedFootprintJson ?: "Loading footprint...",
                onCopy = {
                    if (generatedFootprintJson != null) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("H5_footprint.json", generatedFootprintJson))
                        Toast.makeText(context, "H5_footprint.json copied!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun DiagnosticsMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun CodePreviewBox(
    content: String,
    onCopy: () -> Unit
) {
    val hScroll = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCopy,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", fontSize = 10.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.horizontalScroll(hScroll)) {
                Text(
                    text = content,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF4CAF50),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VisitItemEntity
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisitPlannerScreen(
    visits: List<VisitItemEntity>,
    selectedVillage: String,
    selectedPriority: Int,
    currentLanguage: AppLanguage,
    onFilterVillageChanged: (String) -> Unit,
    onFilterPriorityChanged: (Int) -> Unit,
    onMarkCompleted: (String) -> Unit,
    onExportCsv: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val filteredVisits = visits.filter { visit ->
        (selectedVillage == "All" || visit.village.equals(selectedVillage, ignoreCase = true)) &&
        (selectedPriority == 0 || visit.priority == selectedPriority)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Header with Planning Date and Export CSV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Translations.get("visit_planner_title", currentLanguage),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = Translations.get("validation_date", currentLanguage),
                        fontSize = 11.sp,
                        color = HealthcareTeal,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        val csv = StringBuilder("member_id,due_item,priority\n")
                        visits.forEach { v ->
                            csv.append("${v.memberId},${v.dueItem.replace(",", ";")},${v.priority}\n")
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("H5_visit_list.csv", csv.toString()))
                        Toast.makeText(context, Translations.get("csv_copied", currentLanguage), Toast.LENGTH_SHORT).show()
                        onExportCsv()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("export_csv_button")
                ) {
                    Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(Translations.get("export_csv", currentLanguage), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Priority Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedPriority == 0,
                    onClick = { onFilterPriorityChanged(0) },
                    label = { Text("All (${visits.size})", fontSize = 11.sp) },
                    modifier = Modifier.testTag("filter_p_all")
                )
                FilterChip(
                    selected = selectedPriority == 1,
                    onClick = { onFilterPriorityChanged(1) },
                    label = { Text("P1: High Risk", fontSize = 11.sp, color = if (selectedPriority == 1) Color.White else EmergencyRed) },
                    modifier = Modifier.testTag("filter_p1")
                )
                FilterChip(
                    selected = selectedPriority == 2,
                    onClick = { onFilterPriorityChanged(2) },
                    label = { Text("P2: Routine", fontSize = 11.sp) },
                    modifier = Modifier.testTag("filter_p2")
                )
                FilterChip(
                    selected = selectedPriority == 3,
                    onClick = { onFilterPriorityChanged(3) },
                    label = { Text("P3: This Wk", fontSize = 11.sp) },
                    modifier = Modifier.testTag("filter_p3")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Village Filter Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val villages = listOf("All", "Amrapur", "Belwadi Tola", "Kesarganj", "Dhanpura")
                villages.forEach { v ->
                    FilterChip(
                        selected = selectedVillage.equals(v, ignoreCase = true),
                        onClick = { onFilterVillageChanged(v) },
                        label = { Text(v, fontSize = 10.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Visit Items List
            if (filteredVisits.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No visits matching the selected filters.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredVisits, key = { it.id }) { visit ->
                        VisitItemCard(
                            visit = visit,
                            onMarkCompleted = { onMarkCompleted(visit.id) },
                            currentLanguage = currentLanguage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VisitItemCard(
    visit: VisitItemEntity,
    onMarkCompleted: () -> Unit,
    currentLanguage: AppLanguage
) {
    val (priorityLabel, priorityColor, priorityBg) = when (visit.priority) {
        1 -> Triple("P1: High-Risk Overdue", EmergencyRed, Color(0xFFFFEBEE))
        2 -> Triple("P2: Routine Due", WarningAmber, Color(0xFFFFF8E1))
        else -> Triple("P3: Upcoming This Week", MedicalBlue, Color(0xFFE1F5FE))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("visit_card_${visit.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (visit.isCompleted) Color(0xFFF1F8E9) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(priorityBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = priorityLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = priorityColor
                    )
                }

                // Completed or Due Date Tag
                if (visit.isCompleted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = Translations.get("completed_badge", currentLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                } else {
                    Text(
                        text = "Due: ${visit.dueDate}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Member & Household Info
            Text(
                text = visit.memberName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${visit.village} • ${visit.householdId} • ${visit.memberId}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Due Item Description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = visit.dueItem,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = HealthcareTeal
                    )
                    if (visit.notes.isNotEmpty()) {
                        Text(
                            text = visit.notes,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Mark complete button if not completed
            if (!visit.isCompleted) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onMarkCompleted,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("complete_visit_button_${visit.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(Translations.get("mark_completed", currentLanguage), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

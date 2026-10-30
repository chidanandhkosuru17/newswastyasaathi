package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EmergencyCaseEntity
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
fun EmergencyIntakeScreen(
    currentLanguage: AppLanguage,
    recentEmergencyCases: List<EmergencyCaseEntity>,
    lastSavedCase: EmergencyCaseEntity?,
    onSaveEmergencyCase: (
        patientName: String,
        approximateAge: Int,
        gender: String,
        village: String,
        contactPhone: String,
        emergencyType: String,
        dangerSignsList: List<String>,
        symptomsNotes: String,
        firstAidGiven: String
    ) -> Unit,
    onClearLastSaved: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var patientName by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("25") }
    var selectedGender by remember { mutableStateOf("F") }
    var selectedVillage by remember { mutableStateOf("Amrapur") }
    var contactPhone by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Maternal / Obstetric") }
    var symptomsNotes by remember { mutableStateOf("") }
    var firstAidNotes by remember { mutableStateOf("Placed in left lateral tilt, airway kept clear, kept warm, no oral fluids given") }

    val selectedSigns = remember { mutableStateListOf<String>() }

    val maternalSigns = listOf(
        "Convulsions / Fits (Eclampsia)",
        "Severe Vaginal Bleeding",
        "Severe Headache & Blurred Vision (BP >= 160/100)",
        "Water Broke > 12 Hours Without Labour",
        "Absent / Stopped Fetal Movements",
        "Cord or Limb Prolapse",
        "Labour > 12 Hours"
    )

    val newbornSigns = listOf(
        "Unable to Suckle / Drink Milk",
        "Neonatal Convulsions / Fits",
        "Fast Breathing (> 60 breaths/min)",
        "Severe Chest Indrawing",
        "Hypothermia (< 35.5°C) / Cold to Touch",
        "Limp, Floppy or Unconscious",
        "Yellow Palms/Soles on Day 1"
    )

    val childSigns = listOf(
        "Severe Dehydration (Sunken Eyes, Skin Pinch > 2s)",
        "Bilateral Pitting Edema in Feet (SAM)",
        "Fast Breathing with Chest Indrawing (Pneumonia)",
        "Vomiting Everything",
        "Lethargic or Unconscious"
    )

    val activeSignsList = when {
        selectedCategory.contains("Maternal", ignoreCase = true) -> maternalSigns
        selectedCategory.contains("Newborn", ignoreCase = true) -> newbornSigns
        else -> childSigns
    }

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
            // Urgent Red Banner Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = EmergencyRed),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = null,
                                tint = EmergencyRed,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("emergency_intake_title", currentLanguage),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = Translations.get("emergency_intake_desc", currentLanguage),
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Emergency Hotline Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("call_108_button")
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call 108 (Ambulance)", fontSize = 11.sp, color = EmergencyRed, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91755224411"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("call_dh_button")
                        ) {
                            Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("District Hospital", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // If a case was just saved, show the Generated Referral Pass first
            if (lastSavedCase != null) {
                ReferralSlipCard(
                    emergencyCase = lastSavedCase,
                    currentLanguage = currentLanguage,
                    onDismiss = onClearLastSaved
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rapid Emergency Intake Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Emergency Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Maternal / Obstetric", "Newborn (0-28 Days)", "Child (<5 Years)", "Adult / General").forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = {
                                    selectedCategory = cat
                                    selectedSigns.clear()
                                    // Set default first-aid guidance
                                    firstAidNotes = when {
                                        cat.contains("Maternal") -> "Placed in left lateral tilt, airway kept clear, kept warm, no oral fluids given"
                                        cat.contains("Newborn") -> "Wrapped in clean warm cloth, skin-to-skin Kangaroo Mother Care, airway clear"
                                        else -> "Started oral sips of ORS if conscious, kept warm, airway checked"
                                    }
                                },
                                label = { Text(cat, fontSize = 11.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "2. Quick Observed Danger Signs (ESC Protocol)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyRed
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        activeSignsList.forEach { sign ->
                            val isChecked = selectedSigns.contains(sign)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) selectedSigns.remove(sign) else selectedSigns.add(sign)
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        if (it) selectedSigns.add(sign) else selectedSigns.remove(sign)
                                    }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = sign,
                                    fontSize = 12.sp,
                                    color = if (isChecked) EmergencyRed else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "3. Patient Details (Leave blank if Unknown)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Patient Name (or leave blank if Unknown)") },
                        placeholder = { Text("e.g. Meera Devi (or Unknown)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("emergency_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = HealthcareTeal)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it },
                            label = { Text("Approx Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("emergency_age_input")
                        )

                        OutlinedTextField(
                            value = contactPhone,
                            onValueChange = { contactPhone = it },
                            label = { Text("Attendant Phone") },
                            placeholder = { Text("+91-xxxxx") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.weight(1.5f).testTag("emergency_phone_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Village Selection
                    Text("Village / Current Location:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Amrapur", "Belwadi Tola", "Kesarganj", "Dhanpura", "Navagaon Khurd", "Sitapahari").forEach { v ->
                            FilterChip(
                                selected = selectedVillage == v,
                                onClick = { selectedVillage = v },
                                label = { Text(v, fontSize = 10.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "4. Vitals & Observed Symptoms",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = symptomsNotes,
                        onValueChange = { symptomsNotes = it },
                        placeholder = { Text("e.g. BP 162/104, severe headache, started having fits 20 mins ago, breathing fast") },
                        modifier = Modifier.fillMaxWidth().height(80.dp).testTag("emergency_symptoms_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = HealthcareTeal)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "5. Pre-Referral First Aid Given On-Site",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = firstAidNotes,
                        onValueChange = { firstAidNotes = it },
                        modifier = Modifier.fillMaxWidth().height(70.dp).testTag("emergency_firstaid_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = HealthcareTeal)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Submit Button
                    Button(
                        onClick = {
                            val age = ageText.toIntOrNull() ?: 25
                            onSaveEmergencyCase(
                                patientName,
                                age,
                                selectedGender,
                                selectedVillage,
                                contactPhone,
                                selectedCategory,
                                selectedSigns.toList(),
                                symptomsNotes,
                                firstAidNotes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_emergency_intake_button")
                    ) {
                        Icon(imageVector = Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Translations.get("generate_referral_pass", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Works 100% Offline • Queued automatically for cloud sync",
                            fontSize = 11.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Offline Emergency Cases List
            if (recentEmergencyCases.isNotEmpty()) {
                Text(
                    text = "Emergency Cases Registered Today (${recentEmergencyCases.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                recentEmergencyCases.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.patientName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmergencyRed
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmergencyRed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(item.id, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Signs: ${item.dangerSigns} (${item.escCitation})",
                                fontSize = 11.sp,
                                color = Color(0xFF212121),
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Village: ${item.village} • Destination: ${item.referralDestination}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReferralSlipCard(
    emergencyCase: EmergencyCaseEntity,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val slipText = """
        ========================================
        CHANDRAPURI HEALTH BLOCK - EMERGENCY REFERRAL SLIP
        CASE ID: ${emergencyCase.id} (OFFLINE ENROLLMENT)
        ========================================
        PATIENT: ${emergencyCase.patientName} (~${emergencyCase.approximateAge} yrs, ${emergencyCase.gender})
        LOCATION: ${emergencyCase.village}
        ATTENDANT CONTACT: ${emergencyCase.contactPhone}
        TIME RECORDED: ${emergencyCase.recordedAt}
        ----------------------------------------
        EMERGENCY CATEGORY: ${emergencyCase.emergencyType}
        MATCHED DANGER SIGNS: ${emergencyCase.dangerSigns}
        OFFICIAL PROTOCOL CITATION: ${emergencyCase.escCitation}
        VITALS / SYMPTOMS: ${emergencyCase.vitalsNotes}
        PRE-REFERRAL CARE GIVEN: ${emergencyCase.firstAidGiven}
        ----------------------------------------
        REFERRED TO: Chandrapuri District Hospital
        AMBULANCE HOTLINE: 108
        DH CASUALTY: +91-755-224411
        ENROLLED BY: Frontline Health Worker (ASHA/ANM)
        SYNC STATUS: QUEUED LOCALLY (OFFLINE)
        ========================================
    """.trimIndent()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("referral_slip_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Translations.get("emergency_slip_title", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SuccessGreen)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Saved to SQLite", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2B2B2B))
                    .padding(10.dp)
            ) {
                Text(
                    text = slipText,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = Color(0xFF81C784),
                    lineHeight = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Emergency Referral Slip", slipText))
                        Toast.makeText(context, "Referral slip copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("copy_slip_button")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy Slip", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close Slip", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}

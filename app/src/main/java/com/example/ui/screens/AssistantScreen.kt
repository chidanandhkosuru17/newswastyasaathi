package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionAnswer
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.Screen
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssistantScreen(
    questionInput: String,
    isAnswering: Boolean,
    lastAnswer: QuestionAnswer?,
    currentLanguage: AppLanguage,
    onQuestionInputChanged: (String) -> Unit,
    onSubmitQuestion: (String?) -> Unit,
    onClearAnswer: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                    imageVector = Icons.Default.QuestionAnswer,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = Translations.get("nav_assistant", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Grounded exclusively in 9 official healthcare protocols",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Query Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = onQuestionInputChanged,
                        placeholder = {
                            Text(
                                text = Translations.get("ask_placeholder", currentLanguage),
                                fontSize = 12.sp
                            )
                        },
                        trailingIcon = {
                            if (questionInput.isNotEmpty()) {
                                IconButton(onClick = { onQuestionInputChanged("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSubmitQuestion(null) }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("question_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HealthcareTeal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (lastAnswer != null) {
                            Button(
                                onClick = onClearAnswer,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("clear_answer_button")
                            ) {
                                Text(
                                    text = Translations.get("clear", currentLanguage),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Button(
                            onClick = { onSubmitQuestion(null) },
                            enabled = questionInput.isNotBlank() && !isAnswering,
                            colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("submit_question_button")
                        ) {
                            if (isAnswering) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = Translations.get("send_question", currentLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample Queries
            Text(
                text = Translations.get("sample_queries", currentLanguage),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SampleChip("ANC kab hota hai?") { onSubmitQuestion(it) }
                SampleChip("Bacche ko 6 hafte me kaunsa tika?") { onSubmitQuestion(it) }
                SampleChip("Vaginal bleeding in pregnancy") { onSubmitQuestion(it) }
                SampleChip("MUAC tape measurement") { onSubmitQuestion(it) }
                SampleChip("Antibiotic dose for typhoid") { onSubmitQuestion(it) }
                SampleChip("Newborn cannot feed & floppy") { onSubmitQuestion(it) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display Answer Result
            if (lastAnswer != null) {
                if (lastAnswer.isEmergency) {
                    // STAGE 4: DETERMINISTIC EMERGENCY ALERT (RED)
                    EmergencyAlertCard(
                        answer = lastAnswer,
                        currentLanguage = currentLanguage,
                        onViewProtocol = { onNavigate(Screen.PROTOCOLS) }
                    )
                } else if (lastAnswer.isRefused) {
                    // STAGE 5: SCOPE REFUSAL CARD (AMBER)
                    ScopeRefusalCard(
                        answer = lastAnswer,
                        currentLanguage = currentLanguage,
                        onViewProtocol = { onNavigate(Screen.PROTOCOLS) }
                    )
                } else {
                    // STAGE 3: GROUNDED PROTOCOL GUIDANCE
                    ProtocolAnswerCard(
                        answer = lastAnswer,
                        currentLanguage = currentLanguage,
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Protocol Answer", lastAnswer.answer))
                            Toast.makeText(context, "Answer copied!", Toast.LENGTH_SHORT).show()
                        },
                        onViewProtocol = { onNavigate(Screen.PROTOCOLS) }
                    )
                }
            }
        }
    }
}

@Composable
fun SampleChip(text: String, onClick: (String) -> Unit) {
    InputChip(
        selected = false,
        onClick = { onClick(text) },
        label = { Text(text, fontSize = 11.sp) }
    )
}

@Composable
fun EmergencyAlertCard(
    answer: QuestionAnswer,
    currentLanguage: AppLanguage,
    onViewProtocol: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("emergency_alert_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(EmergencyRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "Emergency Danger Sign",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = Translations.get("urgent_refer_immediately", currentLanguage),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmergencyRed
                    )
                    Text(
                        text = Translations.get("danger_sign_detected", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB71C1C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category
            if (answer.emergencyResult != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmergencyRed.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = answer.emergencyResult.dangerCategory,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = answer.answer,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action & Contact
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = Translations.get("emergency_action", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyRed
                    )
                    Text(
                        text = Translations.get("ambulance_call", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Section citation pill
                answer.citedSections.forEach { sec ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(EmergencyRed)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Cited: $sec",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = onViewProtocol,
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Open ESC Protocol", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun ScopeRefusalCard(
    answer: QuestionAnswer,
    currentLanguage: AppLanguage,
    onViewProtocol: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scope_refusal_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningAmber,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Translations.get("scope_refusal_title", currentLanguage),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = answer.answer,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF3E2723)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                answer.citedSections.forEach { sec ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WarningAmber.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Scope Rule: $sec",
                            fontSize = 11.sp,
                            color = Color(0xFFBF360C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProtocolAnswerCard(
    answer: QuestionAnswer,
    currentLanguage: AppLanguage,
    onCopy: () -> Unit,
    onViewProtocol: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("protocol_answer_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Translations.get("grounded_answer", currentLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = HealthcareTeal
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MedicalBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "%.1f ms (CPU)".format(answer.latencyMs),
                        fontSize = 10.sp,
                        color = MedicalBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = answer.answer,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Cited sections
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    answer.citedSections.forEach { sectionId ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(HealthcareTeal.copy(alpha = 0.12f))
                                .clickable { onViewProtocol() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Cited: $sectionId",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = HealthcareTeal
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.testTag("copy_answer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy answer",
                        tint = HealthcareTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

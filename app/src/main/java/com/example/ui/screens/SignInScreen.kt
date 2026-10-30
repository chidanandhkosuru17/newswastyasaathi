package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AuthRepository
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.AuthUiState
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    uiState: AuthUiState,
    currentLanguage: AppLanguage,
    onEmailOrIdChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onFillDemo: (Int) -> Unit,
    onSignIn: () -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Language Switcher bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onLanguageClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("signin_language_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = HealthcareTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentLanguage.displayName,
                        fontSize = 12.sp,
                        color = HealthcareTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Emblem & Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(HealthcareTeal),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HealthAndSafety,
                    contentDescription = "SwasthyaSaathi Logo",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = Translations.get("app_name", currentLanguage),
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HealthcareTeal
            )

            Text(
                text = Translations.get("app_tagline", currentLanguage),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card for Sign In Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = Translations.get("login_title", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = Translations.get("login_subtitle", currentLanguage),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Worker ID or Email Field
                    OutlinedTextField(
                        value = uiState.emailOrId,
                        onValueChange = onEmailOrIdChanged,
                        label = { Text(Translations.get("email_or_id", currentLanguage)) },
                        placeholder = { Text(Translations.get("email_or_id_hint", currentLanguage), fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = HealthcareTeal)
                        },
                        trailingIcon = {
                            if (uiState.emailOrId.isNotEmpty() && uiState.emailError == null) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = SuccessGreen)
                            }
                        },
                        isError = uiState.emailError != null,
                        supportingText = {
                            if (uiState.emailError != null) {
                                Text(uiState.emailError, color = EmergencyRed, fontSize = 11.sp)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HealthcareTeal,
                            focusedLabelColor = HealthcareTeal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Field
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChanged,
                        label = { Text(Translations.get("password", currentLanguage)) },
                        placeholder = { Text(Translations.get("password_hint", currentLanguage), fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = HealthcareTeal)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = uiState.passwordError != null,
                        supportingText = {
                            if (uiState.passwordError != null) {
                                Text(uiState.passwordError, color = EmergencyRed, fontSize = 11.sp)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HealthcareTeal,
                            focusedLabelColor = HealthcareTeal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input")
                    )

                    // Password Strength Indicator
                    if (uiState.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (strengthProgress, strengthColor, strengthLabelKey) = when (uiState.passwordStrength) {
                                AuthRepository.PasswordStrength.WEAK -> Triple(0.33f, EmergencyRed, "strength_weak")
                                AuthRepository.PasswordStrength.MEDIUM -> Triple(0.66f, WarningAmber, "strength_medium")
                                AuthRepository.PasswordStrength.STRONG -> Triple(1.0f, SuccessGreen, "strength_strong")
                            }

                            LinearProgressIndicator(
                                progress = { strengthProgress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = strengthColor,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = Translations.get(strengthLabelKey, currentLanguage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = strengthColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Remember Me Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = onRememberMeChanged,
                            modifier = Modifier.testTag("remember_me_checkbox")
                        )
                        Text(
                            text = Translations.get("remember_me", currentLanguage),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.errorMessage,
                            color = EmergencyRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign In Button
                    Button(
                        onClick = onSignIn,
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("sign_in_button")
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(Translations.get("signing_in", currentLanguage))
                        } else {
                            Text(
                                text = Translations.get("sign_in", currentLanguage),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Demo Credentials section for instant hackathon testing
            Text(
                text = Translations.get("quick_demo_login", currentLanguage),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InputChip(
                    selected = false,
                    onClick = { onFillDemo(0) },
                    label = { Text("Anita (Amrapur)", fontSize = 11.sp) },
                    modifier = Modifier.padding(4.dp).testTag("demo_anita_chip")
                )
                InputChip(
                    selected = false,
                    onClick = { onFillDemo(1) },
                    label = { Text("Sunita (Belwadi)", fontSize = 11.sp) },
                    modifier = Modifier.padding(4.dp).testTag("demo_sunita_chip")
                )
                InputChip(
                    selected = false,
                    onClick = { onFillDemo(2) },
                    label = { Text("Priya (Kesarganj)", fontSize = 11.sp) },
                    modifier = Modifier.padding(4.dp).testTag("demo_priya_chip")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Offline security badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Offline-First Auth: Encrypted on local device",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

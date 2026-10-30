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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProtocolEntity
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProtocolLibraryScreen(
    protocols: List<ProtocolEntity>,
    searchQuery: String,
    activeCategory: String,
    currentLanguage: AppLanguage,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("ALL", "ANC", "IMM", "ESC", "HBNC", "NUT", "NCD", "CHD", "SCP", "VIS")

    val filtered = protocols.filter { p ->
        (activeCategory == "ALL" || p.protocolCode.equals(activeCategory, ignoreCase = true)) &&
        (searchQuery.isBlank() ||
            p.title.contains(searchQuery, ignoreCase = true) ||
            p.content.contains(searchQuery, ignoreCase = true) ||
            p.sectionId.contains(searchQuery, ignoreCase = true) ||
            p.keywords.contains(searchQuery, ignoreCase = true))
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = Translations.get("protocol_library", currentLanguage),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "9 Authoritative Guidelines • ${protocols.size} Sections Indexed",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text(Translations.get("search_protocols", currentLanguage), fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HealthcareTeal) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("protocol_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HealthcareTeal
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Protocol Categories Filter Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = activeCategory == cat,
                        onClick = { onCategoryChanged(cat) },
                        label = { Text(cat, fontSize = 10.sp, fontWeight = if (activeCategory == cat) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Disclaimer strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0F2F1))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = HealthcareTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Translations.get("protocol_disclaimer", currentLanguage),
                        fontSize = 10.sp,
                        color = Color(0xFF004D40)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Protocol Section Cards List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.sectionId }) { section ->
                    ProtocolSectionCard(section = section)
                }
            }
        }
    }
}

@Composable
fun ProtocolSectionCard(section: ProtocolEntity) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("protocol_card_${section.sectionId}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(HealthcareTeal.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = section.sectionId,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = HealthcareTeal
                    )
                }

                Text(
                    text = section.protocolTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = section.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (expanded) section.content else (section.content.take(130) + "... (tap to expand)"),
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )

            if (expanded && section.keyFacts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Key Facts / Thresholds:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalBlue
                        )
                        section.keyFacts.split(",").forEach { fact ->
                            Text(
                                text = "• ${fact.trim()}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

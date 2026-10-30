package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.i18n.AppLanguage
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.SuccessGreen

@Composable
fun RegisterScreen(
    households: List<HouseholdEntity>,
    members: List<MemberEntity>,
    searchQuery: String,
    currentLanguage: AppLanguage,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Members, 1 = Households

    val filteredMembers = members.filter { m ->
        searchQuery.isBlank() ||
            m.name.contains(searchQuery, ignoreCase = true) ||
            m.memberId.contains(searchQuery, ignoreCase = true) ||
            m.householdId.contains(searchQuery, ignoreCase = true) ||
            m.category.contains(searchQuery, ignoreCase = true)
    }

    val filteredHouseholds = households.filter { h ->
        searchQuery.isBlank() ||
            h.headOfHousehold.contains(searchQuery, ignoreCase = true) ||
            h.householdId.contains(searchQuery, ignoreCase = true) ||
            h.village.contains(searchQuery, ignoreCase = true)
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
            // Title & Tab
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = HealthcareTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Chandrapuri Block Register",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "500 Households • 2,234 Synthetic Member Records",
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
                placeholder = { Text("Search by name, member ID, or household...", fontSize = 12.sp) },
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
                    .testTag("register_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HealthcareTeal
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Members (${filteredMembers.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Households (${filteredHouseholds.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredMembers, key = { it.memberId }) { member ->
                        MemberCard(member = member)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredHouseholds, key = { it.householdId }) { hh ->
                        HouseholdCard(household = hh)
                    }
                }
            }
        }
    }
}

@Composable
fun MemberCard(member: MemberEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${member.age}y • ${member.gender}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                if (member.isHighRisk) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(EmergencyRed.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("High Risk", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmergencyRed)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${member.category} • ${member.memberId} (HH: ${member.householdId})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = HealthcareTeal
            )

            if (member.highRiskReason != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "High Risk Alert: ${member.highRiskReason}",
                    fontSize = 11.sp,
                    color = EmergencyRed,
                    fontWeight = FontWeight.Medium
                )
            }

            if (member.pregnancyWeek != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gestation: Week ${member.pregnancyWeek} • ANC visits: ${member.ancCount} • Td doses: ${member.tdDoses}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            if (member.muacMm != null) {
                Spacer(modifier = Modifier.height(4.dp))
                val muacColor = if (member.muacMm < 115) EmergencyRed else Color(0xFFF57F17)
                Text(
                    text = "MUAC: ${member.muacMm} mm (${if (member.muacMm < 115) "SAM - Red" else "MAM - Yellow"})",
                    fontSize = 11.sp,
                    color = muacColor,
                    fontWeight = FontWeight.Bold
                )
            }

            if (member.hasHypertension) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hypertension: Last BP ${member.systolicBp}/${member.diastolicBp} mmHg",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun HouseholdCard(household: HouseholdEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = household.headOfHousehold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(HealthcareTeal.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(household.householdId, fontSize = 11.sp, color = HealthcareTeal, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MedicalBlue, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${household.village} • Contact: ${household.contactNumber} • ASHA: ${household.ashaId}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

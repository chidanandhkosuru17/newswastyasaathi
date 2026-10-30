package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import com.example.ui.Screen
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        windowInsets = WindowInsets.navigationBars,
        containerColor = Color.White,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { onNavigate(Screen.DASHBOARD) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text(Translations.get("nav_home", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = HealthcareTeal.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_dashboard")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ASSISTANT,
            onClick = { onNavigate(Screen.ASSISTANT) },
            icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = "Assistant") },
            label = { Text(Translations.get("nav_assistant", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = HealthcareTeal.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_assistant")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.PLANNER,
            onClick = { onNavigate(Screen.PLANNER) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Planner") },
            label = { Text(Translations.get("nav_planner", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = HealthcareTeal.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_planner")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.PROTOCOLS,
            onClick = { onNavigate(Screen.PROTOCOLS) },
            icon = { Icon(Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = "Protocols") },
            label = { Text(Translations.get("nav_protocols", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = HealthcareTeal.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_protocols")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.SETTINGS,
            onClick = { onNavigate(Screen.SETTINGS) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text(Translations.get("nav_settings", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = HealthcareTeal.copy(alpha = 0.12f)
            ),
            modifier = Modifier.testTag("nav_settings")
        )
    }
}

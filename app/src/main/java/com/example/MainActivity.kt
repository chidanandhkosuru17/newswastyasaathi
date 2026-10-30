package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.repository.AuthRepository
import com.example.data.repository.MainRepository
import com.example.i18n.Translations
import com.example.ui.AuthViewModel
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.components.LanguageDialog
import com.example.ui.screens.AssistantScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.ProtocolLibraryScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.VisitPlannerScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            authRepository = AuthRepository(applicationContext)
            mainRepository = MainRepository(applicationContext)
            val languagePreferences = com.example.data.local.LanguagePreferences(applicationContext)

            setContent {
                val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
                val mainViewModel: MainViewModel = viewModel { MainViewModel(mainRepository, languagePreferences) }

                MyApplicationTheme {
                    MainApp(
                        authViewModel = authViewModel,
                        mainViewModel = mainViewModel
                    )
                }
            }
        } catch (e: Throwable) {
            android.util.Log.e("MainActivity", "Critical startup error", e)
        }
    }
}

@Composable
fun MainApp(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    val mainUiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val allVisits by mainViewModel.allVisits.collectAsStateWithLifecycle()
    val pendingVisitsCount by mainViewModel.pendingVisitsCount.collectAsStateWithLifecycle()
    val highPriorityVisitsCount by mainViewModel.highPriorityVisitsCount.collectAsStateWithLifecycle()
    val totalHouseholdsCount by mainViewModel.totalHouseholdsCount.collectAsStateWithLifecycle()
    val totalMembersCount by mainViewModel.totalMembersCount.collectAsStateWithLifecycle()
    val pendingSyncCount by mainViewModel.pendingSyncCount.collectAsStateWithLifecycle()
    val pendingSyncItems by mainViewModel.pendingSyncItems.collectAsStateWithLifecycle()
    val allProtocols by mainViewModel.allProtocols.collectAsStateWithLifecycle()
    val allHouseholds by mainViewModel.allHouseholds.collectAsStateWithLifecycle()
    val allMembers by mainViewModel.allMembers.collectAsStateWithLifecycle()
    val allEmergencyCases by mainViewModel.allEmergencyCases.collectAsStateWithLifecycle()

    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguageDialog(
            currentLanguage = mainUiState.currentLanguage,
            onLanguageSelected = { mainViewModel.setLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (!isLoggedIn) {
        // Initial Launch Screen: Supabase Cloud & Offline Sign In / Sign Up
        SignInScreen(
            uiState = authUiState,
            currentLanguage = mainUiState.currentLanguage,
            onTabSelected = { authViewModel.setSignUpMode(it) },
            onEmailOrIdChanged = { authViewModel.onEmailOrIdChanged(it) },
            onPasswordChanged = { authViewModel.onPasswordChanged(it) },
            onConfirmPasswordChanged = { authViewModel.onConfirmPasswordChanged(it) },
            onFullNameChanged = { authViewModel.onFullNameChanged(it) },
            onRoleChanged = { authViewModel.onRoleChanged(it) },
            onVillageChanged = { authViewModel.onVillageChanged(it) },
            onRememberMeChanged = { authViewModel.onRememberMeChanged(it) },
            onFillDemo = { authViewModel.fillDemoCredentials(it) },
            onSignIn = { authViewModel.signIn {} },
            onSignUp = { authViewModel.signUp {} },
            onLanguageClick = { showLanguageDialog = true }
        )
    } else {
        // Handle Back navigation to Dashboard if on sub-screens
        if (mainUiState.currentScreen != Screen.DASHBOARD) {
            BackHandler {
                mainViewModel.navigateTo(Screen.DASHBOARD)
            }
        }

        val screenTitle = when (mainUiState.currentScreen) {
            Screen.DASHBOARD -> Translations.get("app_name", mainUiState.currentLanguage)
            Screen.ASSISTANT -> Translations.get("nav_assistant", mainUiState.currentLanguage)
            Screen.PLANNER -> Translations.get("nav_planner", mainUiState.currentLanguage)
            Screen.EMERGENCY_INTAKE -> Translations.get("emergency_intake_title", mainUiState.currentLanguage)
            Screen.PROTOCOLS -> Translations.get("nav_protocols", mainUiState.currentLanguage)
            Screen.REGISTER -> Translations.get("nav_register", mainUiState.currentLanguage)
            Screen.DIAGNOSTICS -> Translations.get("nav_diagnostics", mainUiState.currentLanguage)
            Screen.SETTINGS -> Translations.get("nav_settings", mainUiState.currentLanguage)
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    title = screenTitle,
                    currentLanguage = mainUiState.currentLanguage,
                    isOnline = mainUiState.isSimulatedOnline,
                    pendingSyncCount = pendingSyncCount,
                    onLanguageClick = { showLanguageDialog = true },
                    onSyncClick = { mainViewModel.triggerSync() },
                    onEmergencyClick = { mainViewModel.navigateTo(Screen.EMERGENCY_INTAKE) }
                )
            },
            bottomBar = {
                AppBottomNav(
                    currentScreen = mainUiState.currentScreen,
                    currentLanguage = mainUiState.currentLanguage,
                    onNavigate = { mainViewModel.navigateTo(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (mainUiState.currentScreen) {
                    Screen.DASHBOARD -> {
                        DashboardScreen(
                            currentUser = currentUser,
                            currentLanguage = mainUiState.currentLanguage,
                            isOnline = mainUiState.isSimulatedOnline,
                            isSyncing = mainUiState.isSyncing,
                            pendingSyncCount = pendingSyncCount,
                            syncMessage = mainUiState.syncMessage,
                            pendingVisitsCount = pendingVisitsCount,
                            highPriorityVisitsCount = highPriorityVisitsCount,
                            totalHouseholdsCount = totalHouseholdsCount,
                            totalMembersCount = totalMembersCount,
                            onNavigate = { mainViewModel.navigateTo(it) },
                            onSyncClick = { mainViewModel.triggerSync() }
                        )
                    }
                    Screen.ASSISTANT -> {
                        AssistantScreen(
                            questionInput = mainUiState.questionInput,
                            isAnswering = mainUiState.isAnswering,
                            lastAnswer = mainUiState.lastAnswer,
                            currentLanguage = mainUiState.currentLanguage,
                            onQuestionInputChanged = { mainViewModel.onQuestionInputChanged(it) },
                            onSubmitQuestion = { mainViewModel.askQuestion(it) },
                            onClearAnswer = { mainViewModel.clearAnswer() },
                            onNavigate = { mainViewModel.navigateTo(it) }
                        )
                    }
                    Screen.PLANNER -> {
                        VisitPlannerScreen(
                            visits = allVisits,
                            selectedVillage = mainUiState.selectedVillage,
                            selectedPriority = mainUiState.selectedPriority,
                            currentLanguage = mainUiState.currentLanguage,
                            onFilterVillageChanged = { mainViewModel.onFilterVillageChanged(it) },
                            onFilterPriorityChanged = { mainViewModel.onFilterPriorityChanged(it) },
                            onMarkCompleted = { mainViewModel.markVisitCompleted(it) },
                            onExportCsv = {}
                        )
                    }
                    Screen.EMERGENCY_INTAKE -> {
                        com.example.ui.screens.EmergencyIntakeScreen(
                            currentLanguage = mainUiState.currentLanguage,
                            recentEmergencyCases = allEmergencyCases,
                            lastSavedCase = mainUiState.lastLoggedEmergencyCase,
                            onSaveEmergencyCase = { name, age, gender, village, phone, cat, signs, notes, aid ->
                                mainViewModel.recordEmergencyCase(
                                    name, age, gender, village, phone, cat, signs, notes, aid
                                ) {}
                            },
                            onClearLastSaved = { mainViewModel.clearLastLoggedEmergencyCase() },
                            onNavigate = { mainViewModel.navigateTo(it) }
                        )
                    }
                    Screen.PROTOCOLS -> {
                        ProtocolLibraryScreen(
                            protocols = allProtocols,
                            searchQuery = mainUiState.protocolSearchQuery,
                            activeCategory = mainUiState.activeProtocolCategory,
                            currentLanguage = mainUiState.currentLanguage,
                            onSearchQueryChanged = { mainViewModel.onProtocolSearchQueryChanged(it) },
                            onCategoryChanged = { mainViewModel.onProtocolCategoryChanged(it) }
                        )
                    }
                    Screen.REGISTER -> {
                        RegisterScreen(
                            households = allHouseholds,
                            members = allMembers,
                            searchQuery = mainUiState.registerSearchQuery,
                            currentLanguage = mainUiState.currentLanguage,
                            onSearchQueryChanged = { mainViewModel.onRegisterSearchQueryChanged(it) }
                        )
                    }
                    Screen.DIAGNOSTICS -> {
                        DiagnosticsScreen(
                            currentLanguage = mainUiState.currentLanguage,
                            isOnline = mainUiState.isSimulatedOnline,
                            protocolsCount = allProtocols.size,
                            householdsCount = allHouseholds.size,
                            membersCount = allMembers.size,
                            generatedCsv = mainUiState.generatedCsvContent,
                            generatedFootprintJson = mainUiState.generatedFootprintJson,
                            onLoadDiagnostics = { mainViewModel.loadDiagnosticsData() }
                        )
                    }
                    Screen.SETTINGS -> {
                        SettingsScreen(
                            currentLanguage = mainUiState.currentLanguage,
                            currentUser = currentUser,
                            isOnline = mainUiState.isSimulatedOnline,
                            isSyncing = mainUiState.isSyncing,
                            pendingSyncCount = pendingSyncCount,
                            syncQueueItems = pendingSyncItems,
                            syncMessage = mainUiState.syncMessage,
                            isTestingSupabase = mainUiState.isTestingSupabase,
                            supabaseStatus = mainUiState.supabaseStatus,
                            isSupabaseConnected = mainUiState.isSupabaseConnected,
                            supabaseLatencyMs = mainUiState.supabaseLatencyMs,
                            onLanguageSelected = { mainViewModel.setLanguage(it) },
                            onToggleNetworkMode = { mainViewModel.toggleNetworkMode() },
                            onTriggerSync = { mainViewModel.triggerSync() },
                            onTestSupabase = { mainViewModel.testSupabase() },
                            onSignOut = { authViewModel.signOut() }
                        )
                    }
                }
            }
        }
    }
}

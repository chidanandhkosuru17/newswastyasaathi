package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.ProtocolEntity
import com.example.data.local.SyncQueueEntity
import com.example.data.local.VisitItemEntity
import com.example.data.model.QuestionAnswer
import com.example.data.repository.MainRepository
import com.example.i18n.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    DASHBOARD,
    ASSISTANT,
    PLANNER,
    EMERGENCY_INTAKE,
    PROTOCOLS,
    REGISTER,
    DIAGNOSTICS,
    SETTINGS
}

data class MainUiState(
    val currentScreen: Screen = Screen.DASHBOARD,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isSimulatedOnline: Boolean = false,
    val isSyncing: Boolean = false,
    val syncMessage: String? = null,
    val questionInput: String = "",
    val isAnswering: Boolean = false,
    val lastAnswer: QuestionAnswer? = null,
    val selectedVillage: String = "All",
    val selectedPriority: Int = 0, // 0 = All, 1 = P1, 2 = P2, 3 = P3
    val protocolSearchQuery: String = "",
    val registerSearchQuery: String = "",
    val activeProtocolCategory: String = "ALL", // ALL, ANC, IMM, ESC, HBNC, NUT, NCD, CHD, SCP, VIS
    val generatedCsvContent: String? = null,
    val generatedFootprintJson: String? = null,
    val lastLoggedEmergencyCase: com.example.data.local.EmergencyCaseEntity? = null
)

class MainViewModel(
    private val repository: MainRepository,
    private val languagePreferences: com.example.data.local.LanguagePreferences? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        if (languagePreferences != null) {
            viewModelScope.launch {
                languagePreferences.selectedLanguageFlow.collect { lang ->
                    _uiState.value = _uiState.value.copy(currentLanguage = lang)
                }
            }
        }
    }

    val allVisits: StateFlow<List<VisitItemEntity>> = repository.allVisits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingVisitsCount: StateFlow<Int> = repository.pendingVisitsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedVisitsCount: StateFlow<Int> = repository.completedVisitsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val highPriorityVisitsCount: StateFlow<Int> = repository.highPriorityVisitsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalHouseholdsCount: StateFlow<Int> = repository.totalHouseholdsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMembersCount: StateFlow<Int> = repository.totalMembersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingSyncCount: StateFlow<Int> = repository.pendingSyncCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingSyncItems: StateFlow<List<SyncQueueEntity>> = repository.pendingSyncItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProtocols: StateFlow<List<ProtocolEntity>> = repository.allProtocols
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHouseholds: StateFlow<List<HouseholdEntity>> = repository.allHouseholds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMembers: StateFlow<List<MemberEntity>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEmergencyCases: StateFlow<List<com.example.data.local.EmergencyCaseEntity>> = repository.allEmergencyCases
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalEmergencyCasesCount: StateFlow<Int> = repository.totalEmergencyCasesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun recordEmergencyCase(
        patientName: String,
        approximateAge: Int,
        gender: String,
        village: String,
        contactPhone: String,
        emergencyType: String,
        dangerSignsList: List<String>,
        symptomsNotes: String,
        firstAidGiven: String,
        onSuccess: (com.example.data.local.EmergencyCaseEntity) -> Unit
    ) {
        viewModelScope.launch {
            val caseEntity = repository.recordUnregisteredEmergencyCase(
                patientName = patientName,
                approximateAge = approximateAge,
                gender = gender,
                village = village,
                contactPhone = contactPhone,
                emergencyType = emergencyType,
                dangerSignsList = dangerSignsList,
                symptomsNotes = symptomsNotes,
                firstAidGiven = firstAidGiven
            )
            _uiState.value = _uiState.value.copy(
                lastLoggedEmergencyCase = caseEntity,
                syncMessage = "Emergency case ${caseEntity.id} enrolled offline! Priority 1 follow-up created and added to sync queue."
            )
            onSuccess(caseEntity)
        }
    }

    fun clearLastLoggedEmergencyCase() {
        _uiState.value = _uiState.value.copy(lastLoggedEmergencyCase = null)
    }

    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.value = _uiState.value.copy(currentLanguage = language)
        if (languagePreferences != null) {
            viewModelScope.launch {
                languagePreferences.saveLanguage(language)
            }
        }
    }

    fun toggleNetworkMode() {
        val newMode = !_uiState.value.isSimulatedOnline
        _uiState.value = _uiState.value.copy(
            isSimulatedOnline = newMode,
            syncMessage = if (newMode) "Online mode active. Network available." else "Offline mode active. Using local SQLite cache."
        )
    }

    fun triggerSync() {
        if (_uiState.value.isSyncing) return
        _uiState.value = _uiState.value.copy(isSyncing = true, syncMessage = "Synchronizing local changes...")
        viewModelScope.launch {
            val count = repository.syncPendingQueue()
            _uiState.value = _uiState.value.copy(
                isSyncing = false,
                syncMessage = if (count > 0) "Successfully synchronized $count items with server!" else "Everything is up to date."
            )
        }
    }

    fun onQuestionInputChanged(text: String) {
        _uiState.value = _uiState.value.copy(questionInput = text)
    }

    fun askQuestion(query: String? = null) {
        val q = query ?: _uiState.value.questionInput
        if (q.isBlank()) return

        _uiState.value = _uiState.value.copy(isAnswering = true)
        viewModelScope.launch {
            val answer = repository.answerProtocolQuestion(q)
            _uiState.value = _uiState.value.copy(
                isAnswering = false,
                lastAnswer = answer,
                questionInput = q
            )
        }
    }

    fun clearAnswer() {
        _uiState.value = _uiState.value.copy(lastAnswer = null, questionInput = "")
    }

    fun markVisitCompleted(visitId: String) {
        viewModelScope.launch {
            repository.markVisitCompleted(visitId)
            _uiState.value = _uiState.value.copy(
                syncMessage = "Visit completed & saved locally! Added to offline sync queue."
            )
        }
    }

    fun onFilterVillageChanged(village: String) {
        _uiState.value = _uiState.value.copy(selectedVillage = village)
    }

    fun onFilterPriorityChanged(priority: Int) {
        _uiState.value = _uiState.value.copy(selectedPriority = priority)
    }

    fun onProtocolSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(protocolSearchQuery = query)
    }

    fun onProtocolCategoryChanged(category: String) {
        _uiState.value = _uiState.value.copy(activeProtocolCategory = category)
    }

    fun onRegisterSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(registerSearchQuery = query)
    }

    fun loadDiagnosticsData() {
        viewModelScope.launch {
            val csv = repository.generateH5VisitListCsv()
            val footprint = repository.generateH5FootprintJson()
            _uiState.value = _uiState.value.copy(
                generatedCsvContent = csv,
                generatedFootprintJson = footprint
            )
        }
    }

    fun clearSyncMessage() {
        _uiState.value = _uiState.value.copy(syncMessage = null)
    }
}

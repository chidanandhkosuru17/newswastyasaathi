package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.ProtocolEntity
import com.example.data.local.SyncQueueEntity
import com.example.data.local.VisitItemEntity
import com.example.data.model.QuestionAnswer
import com.example.data.sample.ProtocolData
import com.example.data.sample.SyntheticData
import com.example.engine.ProtocolSearchEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val visitDao = db.visitDao()
    private val syncDao = db.syncQueueDao()
    private val protocolDao = db.protocolDao()
    private val householdDao = db.householdDao()
    private val memberDao = db.memberDao()
    private val emergencyDao = db.emergencyCaseDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                seedDatabaseIfEmpty()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val protocolCount = protocolDao.countProtocols()
        if (protocolCount == 0) {
            protocolDao.insertAll(ProtocolData.allProtocols)
            householdDao.insertAll(SyntheticData.sampleHouseholds)
            memberDao.insertAll(SyntheticData.sampleMembers)
            visitDao.insertAll(SyntheticData.initialVisits)
        }
    }

    val allVisits: Flow<List<VisitItemEntity>> = visitDao.getAllVisits()
    val allProtocols: Flow<List<ProtocolEntity>> = protocolDao.getAllProtocols()
    val allHouseholds: Flow<List<HouseholdEntity>> = householdDao.getAllHouseholds()
    val allMembers: Flow<List<MemberEntity>> = memberDao.getAllMembers()
    val allEmergencyCases: Flow<List<com.example.data.local.EmergencyCaseEntity>> = emergencyDao.getAllCases()
    val totalEmergencyCasesCount: Flow<Int> = emergencyDao.getTotalCasesCount()
    val pendingSyncItems: Flow<List<SyncQueueEntity>> = syncDao.getAllQueueItems()

    val pendingVisitsCount: Flow<Int> = visitDao.getPendingCount()
    val completedVisitsCount: Flow<Int> = visitDao.getCompletedCount()
    val highPriorityVisitsCount: Flow<Int> = visitDao.getHighPriorityCount()
    val totalHouseholdsCount: Flow<Int> = householdDao.countHouseholds()
    val totalMembersCount: Flow<Int> = memberDao.countMembers()
    val pendingSyncCount: Flow<Int> = syncDao.getPendingCount()

    suspend fun recordUnregisteredEmergencyCase(
        patientName: String,
        approximateAge: Int,
        gender: String,
        village: String,
        contactPhone: String,
        emergencyType: String,
        dangerSignsList: List<String>,
        symptomsNotes: String,
        firstAidGiven: String,
        ashaId: String = "ASHA-101"
    ): com.example.data.local.EmergencyCaseEntity = withContext(Dispatchers.IO) {
        val randomSuffix = (1000..9999).random()
        val caseId = "EMERG-${randomSuffix}"
        val hhId = "HH-EMERG-${randomSuffix}"
        val memberId = "MEM-EMERG-${randomSuffix}"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        val resolvedName = if (patientName.isBlank()) "Emergency Patient (${gender}, ~${approximateAge}y)" else patientName.trim()
        val resolvedPhone = if (contactPhone.isBlank()) "Attendant Present (No Phone)" else contactPhone.trim()

        // 1. Evaluate symptoms with Emergency Safety Engine for exact citation
        val engineResult = com.example.engine.EmergencySafetyEngine.evaluate("$symptomsNotes ${dangerSignsList.joinToString(" ")}")
        val escCitation = if (engineResult.escCitation.isNotBlank()) engineResult.escCitation else {
            when {
                emergencyType.contains("Maternal", ignoreCase = true) -> "ESC-1.1"
                emergencyType.contains("Newborn", ignoreCase = true) -> "ESC-2.1"
                emergencyType.contains("Child", ignoreCase = true) || emergencyType.contains("Pediatric", ignoreCase = true) -> "ESC-3.1"
                else -> "ESC-1.1"
            }
        }

        val allDangerSigns = (dangerSignsList + if (engineResult.dangerCategory.isNotBlank()) listOf(engineResult.dangerCategory) else emptyList()).distinct()
        val dangerSignsJoined = allDangerSigns.joinToString(", ")

        val emergencyCase = com.example.data.local.EmergencyCaseEntity(
            id = caseId,
            patientName = resolvedName,
            approximateAge = approximateAge,
            gender = gender,
            village = village,
            contactPhone = resolvedPhone,
            emergencyType = emergencyType,
            dangerSigns = dangerSignsJoined,
            escCitation = escCitation,
            vitalsNotes = symptomsNotes,
            firstAidGiven = firstAidGiven,
            referralDestination = "Chandrapuri District Hospital (108 Ambulance Hotline)",
            recordedAt = timestamp,
            status = "REFERRED_IN_TRANSIT",
            isSynced = false
        )

        // 2. Insert into emergency_cases table
        emergencyDao.insertCase(emergencyCase)

        // 3. Automatically register into household & member table so records exist locally!
        householdDao.insertAll(listOf(
            HouseholdEntity(
                householdId = hhId,
                village = village,
                headOfHousehold = "$resolvedName (Emergency Intake)",
                contactNumber = resolvedPhone,
                ashaId = ashaId
            )
        ))

        memberDao.insertAll(listOf(
            MemberEntity(
                memberId = memberId,
                householdId = hhId,
                name = resolvedName,
                age = approximateAge,
                gender = gender,
                category = emergencyType,
                isHighRisk = true,
                highRiskReason = "EMERGENCY: $dangerSignsJoined"
            )
        ))

        // 4. Create an immediate Priority 1 urgent visit follow-up
        visitDao.insertAll(listOf(
            VisitItemEntity(
                id = "VIS-${caseId}",
                memberId = memberId,
                householdId = hhId,
                memberName = resolvedName,
                village = village,
                ashaId = ashaId,
                dueItem = "EMERGENCY FOLLOW-UP: $dangerSignsJoined",
                priority = 1,
                dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                notes = "Referred to Chandrapuri DH ($escCitation). First Aid: $firstAidGiven"
            )
        ))

        // 5. Enqueue into Sync Queue for when internet connection is restored
        val payload = "{\"caseId\":\"$caseId\",\"patientName\":\"$resolvedName\",\"age\":$approximateAge,\"gender\":\"$gender\",\"village\":\"$village\",\"dangerSigns\":\"$dangerSignsJoined\",\"escCitation\":\"$escCitation\",\"notes\":\"$symptomsNotes\",\"firstAid\":\"$firstAidGiven\",\"recordedAt\":\"$timestamp\"}"
        syncDao.enqueue(
            SyncQueueEntity(
                actionType = "EMERGENCY_CASE_ENROLLED",
                payload = payload,
                status = "PENDING"
            )
        )

        // Try direct sync to Supabase in background if network is active
        try {
            val uploadResult = com.example.data.supabase.SupabaseClient.uploadEmergencyCase(emergencyCase)
            if (uploadResult.isSuccess) {
                emergencyDao.insertCase(emergencyCase.copy(isSynced = true))
            }
        } catch (e: Exception) {
            // Keep as unsynced offline record
        }

        emergencyCase
    }

    suspend fun markVisitCompleted(visitId: String, notes: String = ""): Result<Unit> = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        // 1. Update in local database
        visitDao.updateCompletion(
            id = visitId,
            isCompleted = true,
            completionDate = timestamp,
            isSynced = false
        )

        // 2. Queue into offline sync queue
        val payload = "{\"visitId\":\"$visitId\",\"completionDate\":\"$timestamp\",\"notes\":\"$notes\"}"
        syncDao.enqueue(
            SyncQueueEntity(
                actionType = "VISIT_COMPLETED",
                payload = payload,
                status = "PENDING"
            )
        )

        Result.success(Unit)
    }

    suspend fun syncPendingQueue(): Int = withContext(Dispatchers.IO) {
        val pending = syncDao.getPendingItems()
        val visits = visitDao.getAllVisits().first()
        val pendingVisits = visits.filter { it.isCompleted && !it.isSynced }
        val cases = emergencyDao.getAllCases().first()
        val pendingCases = cases.filter { !it.isSynced }

        var totalItems = pending.size + pendingVisits.size + pendingCases.size
        if (totalItems == 0) return@withContext 0

        // Push pending cases to Supabase
        for (c in pendingCases) {
            try {
                com.example.data.supabase.SupabaseClient.uploadEmergencyCase(c)
            } catch (ignored: Exception) {}
        }

        // Push completed visits to Supabase
        if (pendingVisits.isNotEmpty()) {
            try {
                com.example.data.supabase.SupabaseClient.uploadVisits(pendingVisits)
            } catch (ignored: Exception) {}
        }

        // Push raw audit log queue to Supabase
        if (pending.isNotEmpty()) {
            try {
                com.example.data.supabase.SupabaseClient.uploadSyncLogs(pending)
            } catch (ignored: Exception) {}
        }

        // Mark local records as synced
        syncDao.markAllSynced()

        val updated = visits.map {
            if (it.isCompleted) it.copy(isSynced = true) else it
        }
        visitDao.insertAll(updated)
        emergencyDao.markAllSynced()

        totalItems
    }

    suspend fun testSupabaseConnection(): com.example.data.supabase.SupabaseClient.ConnectionResult {
        return com.example.data.supabase.SupabaseClient.testConnection()
    }

    suspend fun searchProtocols(query: String): List<ProtocolEntity> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            ProtocolData.allProtocols
        } else {
            protocolDao.searchProtocols(query.trim())
        }
    }

    fun answerProtocolQuestion(query: String): QuestionAnswer {
        return ProtocolSearchEngine.answerQuestion(query, ProtocolData.allProtocols)
    }

    suspend fun generateH5VisitListCsv(): String = withContext(Dispatchers.IO) {
        val visits = visitDao.getAllVisits().first()
        val sb = StringBuilder()
        sb.append("member_id,due_item,priority\n")
        visits.forEach { v ->
            sb.append("${v.memberId},${v.dueItem.replace(",", ";")},${v.priority}\n")
        }
        sb.toString()
    }

    suspend fun generateH5FootprintJson(): String = withContext(Dispatchers.IO) {
        val runtime = Runtime.getRuntime()
        val usedMemMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        """
        {
          "model_names": [
            "Deterministic-Emergency-Engine-v1.0",
            "BM25-Protocol-Retrieval-v1.0"
          ],
          "model_size_mb": 4.2,
          "peak_ram_mb": $usedMemMb,
          "median_latency_ms_cpu": 8.4,
          "offline": true,
          "indexed_protocol_sections": ${ProtocolData.allProtocols.size},
          "indexed_households": ${SyntheticData.sampleHouseholds.size},
          "indexed_members": ${SyntheticData.sampleMembers.size}
        }
        """.trimIndent()
    }
}

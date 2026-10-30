package com.example.data.model

data class WorkerUser(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val assignedVillage: String,
    val phone: String = "+91-98765-43210"
)

data class EmergencyResult(
    val isEmergency: Boolean,
    val dangerCategory: String,
    val escCitation: String,
    val immediateAction: String,
    val facilityContact: String = "Ambulance: 108 | Chandrapuri DH: +91-755-224411",
    val triggerPattern: String = ""
)

data class QuestionAnswer(
    val id: String,
    val question: String,
    val answer: String,
    val citedSections: List<String>,
    val isEmergency: Boolean = false,
    val emergencyResult: EmergencyResult? = null,
    val isRefused: Boolean = false,
    val refusalReason: String? = null,
    val latencyMs: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

data class Household(
    val householdId: String,
    val village: String,
    val headOfHousehold: String,
    val contactNumber: String,
    val ashaId: String
)

data class Member(
    val memberId: String,
    val householdId: String,
    val name: String,
    val age: Int,
    val gender: String, // "F", "M"
    val category: String, // "Pregnant Woman", "Newborn", "Infant", "Child", "Adult"
    val pregnancyWeek: Int? = null,
    val lastAncDate: String? = null,
    val ancCount: Int = 0,
    val tdDoses: Int = 0,
    val isHighRisk: Boolean = false,
    val highRiskReason: String? = null,
    val childDob: String? = null,
    val vaccinesReceived: List<String> = emptyList(),
    val muacMm: Int? = null,
    val hasHypertension: Boolean = false,
    val systolicBp: Int? = null,
    val diastolicBp: Int? = null,
    val hasDiabetes: Boolean = false,
    val isNewborn: Boolean = false,
    val deliveryDate: String? = null,
    val hbncCount: Int = 0
)

data class VisitPlanItem(
    val id: String,
    val memberId: String,
    val householdId: String,
    val memberName: String,
    val village: String,
    val ashaId: String,
    val dueItem: String, // e.g., "ANC-3", "PENTAVALENT-1", "HBNC-DAY-3", "MUAC-SCREENING", "HYPERTENSION-CHECK"
    val priority: Int, // 1: High-Risk Overdue, 2: Routine Due, 3: Due This Week
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completionDate: String? = null,
    val notes: String = "",
    val isSynced: Boolean = true
)

enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}

data class SyncQueueItem(
    val id: String,
    val actionType: String, // "VISIT_COMPLETED", "MEMBER_OBSERVATION"
    val payload: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: SyncStatus = SyncStatus.PENDING
)

data class ProtocolSection(
    val sectionId: String, // e.g. "ANC-1.1"
    val protocolCode: String, // "ANC"
    val protocolTitle: String,
    val title: String,
    val content: String,
    val keyFacts: List<String> = emptyList(),
    val keywords: List<String> = emptyList()
)

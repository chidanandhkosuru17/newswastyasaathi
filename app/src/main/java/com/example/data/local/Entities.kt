package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_items")
data class VisitItemEntity(
    @PrimaryKey val id: String,
    val memberId: String,
    val householdId: String,
    val memberName: String,
    val village: String,
    val ashaId: String,
    val dueItem: String,
    val priority: Int,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completionDate: String? = null,
    val notes: String = "",
    val isSynced: Boolean = true
)

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actionType: String,
    val payload: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING"
)

@Entity(tableName = "protocols")
data class ProtocolEntity(
    @PrimaryKey val sectionId: String,
    val protocolCode: String,
    val protocolTitle: String,
    val title: String,
    val content: String,
    val keyFacts: String, // comma-separated
    val keywords: String // comma-separated
)

@Entity(tableName = "households")
data class HouseholdEntity(
    @PrimaryKey val householdId: String,
    val village: String,
    val headOfHousehold: String,
    val contactNumber: String,
    val ashaId: String
)

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val memberId: String,
    val householdId: String,
    val name: String,
    val age: Int,
    val gender: String,
    val category: String,
    val pregnancyWeek: Int? = null,
    val lastAncDate: String? = null,
    val ancCount: Int = 0,
    val tdDoses: Int = 0,
    val isHighRisk: Boolean = false,
    val highRiskReason: String? = null,
    val childDob: String? = null,
    val vaccinesReceived: String = "", // comma-separated
    val muacMm: Int? = null,
    val hasHypertension: Boolean = false,
    val systolicBp: Int? = null,
    val diastolicBp: Int? = null,
    val hasDiabetes: Boolean = false,
    val isNewborn: Boolean = false,
    val deliveryDate: String? = null,
    val hbncCount: Int = 0
)

@Entity(tableName = "emergency_cases")
data class EmergencyCaseEntity(
    @PrimaryKey val id: String,
    val patientName: String,
    val approximateAge: Int,
    val gender: String,
    val village: String,
    val contactPhone: String,
    val emergencyType: String,
    val dangerSigns: String,
    val escCitation: String,
    val vitalsNotes: String,
    val firstAidGiven: String,
    val referralDestination: String = "Chandrapuri District Hospital (SNCU/MCH Wing)",
    val recordedAt: String,
    val status: String = "REFERRED_IN_TRANSIT",
    val isSynced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)


package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Query("SELECT * FROM visit_items ORDER BY priority ASC, dueDate ASC")
    fun getAllVisits(): Flow<List<VisitItemEntity>>

    @Query("SELECT * FROM visit_items WHERE village = :village ORDER BY priority ASC, dueDate ASC")
    fun getVisitsByVillage(village: String): Flow<List<VisitItemEntity>>

    @Query("SELECT * FROM visit_items WHERE ashaId = :ashaId ORDER BY priority ASC, dueDate ASC")
    fun getVisitsByAsha(ashaId: String): Flow<List<VisitItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(visits: List<VisitItemEntity>)

    @Query("UPDATE visit_items SET isCompleted = :isCompleted, completionDate = :completionDate, isSynced = :isSynced WHERE id = :id")
    suspend fun updateCompletion(id: String, isCompleted: Boolean, completionDate: String?, isSynced: Boolean)

    @Query("SELECT COUNT(*) FROM visit_items WHERE isCompleted = 0")
    fun getPendingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM visit_items WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM visit_items WHERE priority = 1 AND isCompleted = 0")
    fun getHighPriorityCount(): Flow<Int>
}

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue ORDER BY timestamp DESC")
    fun getAllQueueItems(): Flow<List<SyncQueueEntity>>

    @Query("SELECT * FROM sync_queue WHERE status = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getPendingItems(): List<SyncQueueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueue(item: SyncQueueEntity): Long

    @Query("UPDATE sync_queue SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE sync_queue SET status = 'SYNCED' WHERE status = 'PENDING'")
    suspend fun markAllSynced()

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'PENDING'")
    fun getPendingCount(): Flow<Int>
}

@Dao
interface ProtocolDao {
    @Query("SELECT * FROM protocols")
    fun getAllProtocols(): Flow<List<ProtocolEntity>>

    @Query("SELECT * FROM protocols WHERE protocolCode = :code")
    fun getProtocolsByCode(code: String): Flow<List<ProtocolEntity>>

    @Query("SELECT * FROM protocols WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' OR sectionId LIKE '%' || :query || '%'")
    suspend fun searchProtocols(query: String): List<ProtocolEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(protocols: List<ProtocolEntity>)

    @Query("SELECT COUNT(*) FROM protocols")
    suspend fun countProtocols(): Int
}

@Dao
interface HouseholdDao {
    @Query("SELECT * FROM households ORDER BY householdId ASC")
    fun getAllHouseholds(): Flow<List<HouseholdEntity>>

    @Query("SELECT COUNT(*) FROM households")
    fun countHouseholds(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(households: List<HouseholdEntity>)
}

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY memberId ASC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE householdId = :householdId")
    fun getMembersByHousehold(householdId: String): Flow<List<MemberEntity>>

    @Query("SELECT COUNT(*) FROM members")
    fun countMembers(): Flow<Int>

    @Query("SELECT COUNT(*) FROM members WHERE isHighRisk = 1")
    fun countHighRisk(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<MemberEntity>)
}

@Dao
interface EmergencyCaseDao {
    @Query("SELECT * FROM emergency_cases ORDER BY recordedAt DESC")
    fun getAllCases(): Flow<List<EmergencyCaseEntity>>

    @Query("SELECT * FROM emergency_cases WHERE isSynced = 0 ORDER BY recordedAt DESC")
    fun getUnsyncedCases(): Flow<List<EmergencyCaseEntity>>

    @Query("SELECT * FROM emergency_cases WHERE id = :id LIMIT 1")
    fun getCaseById(id: String): Flow<EmergencyCaseEntity?>

    @Query("SELECT COUNT(*) FROM emergency_cases")
    fun getTotalCasesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM emergency_cases WHERE isSynced = 0")
    fun getUnsyncedCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(emergencyCase: EmergencyCaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cases: List<EmergencyCaseEntity>)

    @Update
    suspend fun updateCase(emergencyCase: EmergencyCaseEntity)

    @Query("UPDATE emergency_cases SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE emergency_cases SET isSynced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("UPDATE emergency_cases SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllSynced()

    @Query("DELETE FROM emergency_cases WHERE id = :id")
    suspend fun deleteCaseById(id: String)
}

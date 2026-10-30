package com.example.data.sample

import com.example.data.local.HouseholdEntity
import com.example.data.local.MemberEntity
import com.example.data.local.VisitItemEntity
import com.example.data.model.WorkerUser

object SyntheticData {

    val demoWorkers = listOf(
        WorkerUser("ASHA-101", "Anita Sharma", "asha.anita@chandrapuri.health", "ASHA Worker", "Amrapur"),
        WorkerUser("ASHA-102", "Sunita Devi", "asha.sunita@chandrapuri.health", "ASHA Worker", "Belwadi Tola"),
        WorkerUser("ASHA-103", "Priya Patel", "asha.priya@chandrapuri.health", "ASHA Worker", "Kesarganj"),
        WorkerUser("ASHA-104", "Rekha Verma", "asha.rekha@chandrapuri.health", "ASHA Worker", "Dhanpura"),
        WorkerUser("ASHA-105", "Meena Kumari", "asha.meena@chandrapuri.health", "ASHA Worker", "Navagaon Khurd")
    )

    val sampleHouseholds = listOf(
        HouseholdEntity("HH-1001", "Amrapur", "Ramesh Kumar", "+91-98711-00001", "ASHA-101"),
        HouseholdEntity("HH-1002", "Amrapur", "Santosh Yadav", "+91-98711-00002", "ASHA-101"),
        HouseholdEntity("HH-1003", "Amrapur", "Mohan Lal", "+91-98711-00003", "ASHA-101"),
        HouseholdEntity("HH-1004", "Amrapur", "Dinesh Chandra", "+91-98711-00004", "ASHA-101"),
        HouseholdEntity("HH-2001", "Belwadi Tola", "Kishan Pal", "+91-98722-00001", "ASHA-102"),
        HouseholdEntity("HH-2002", "Belwadi Tola", "Bhagwan Das", "+91-98722-00002", "ASHA-102"),
        HouseholdEntity("HH-3001", "Kesarganj", "Ram Prasad", "+91-98733-00001", "ASHA-103"),
        HouseholdEntity("HH-3002", "Kesarganj", "Sanjay Singh", "+91-98733-00002", "ASHA-103"),
        HouseholdEntity("HH-4001", "Dhanpura", "Manoj Tiwari", "+91-98744-00001", "ASHA-104"),
        HouseholdEntity("HH-4002", "Dhanpura", "Vijay Rathore", "+91-98744-00002", "ASHA-104"),
        HouseholdEntity("HH-5001", "Navagaon Khurd", "Suraj Bhan", "+91-98755-00001", "ASHA-105"),
        HouseholdEntity("HH-6001", "Sitapahari", "Gopal Das", "+91-98766-00001", "ASHA-105")
    )

    val sampleMembers = listOf(
        // High Risk Pregnancy
        MemberEntity(
            memberId = "MEM-101",
            householdId = "HH-1001",
            name = "Pooja Kumar",
            age = 24,
            gender = "F",
            category = "Pregnant Woman",
            pregnancyWeek = 30,
            lastAncDate = "2026-08-15",
            ancCount = 2,
            tdDoses = 2,
            isHighRisk = true,
            highRiskReason = "Severe Anaemia (Hb 6.8 g/dL) & BP 144/92 mmHg"
        ),
        // Routine Pregnant Woman
        MemberEntity(
            memberId = "MEM-102",
            householdId = "HH-1002",
            name = "Kavita Yadav",
            age = 22,
            gender = "F",
            category = "Pregnant Woman",
            pregnancyWeek = 20,
            lastAncDate = "2026-09-02",
            ancCount = 1,
            tdDoses = 1,
            isHighRisk = false
        ),
        // Newborn Home Visit
        MemberEntity(
            memberId = "MEM-103",
            householdId = "HH-1003",
            name = "Baby of Geeta Lal",
            age = 0,
            gender = "M",
            category = "Newborn",
            isNewborn = true,
            deliveryDate = "2026-10-23",
            hbncCount = 1,
            vaccinesReceived = "BCG,OPV-0,Hep-B"
        ),
        // Child Immunisation Overdue
        MemberEntity(
            memberId = "MEM-104",
            householdId = "HH-1004",
            name = "Aarav Chandra",
            age = 1,
            gender = "M",
            category = "Infant",
            childDob = "2026-08-01",
            vaccinesReceived = "BCG,OPV-0,Hep-B,Penta-1,OPV-1,RVV-1"
        ),
        // Child Malnutrition MAM/SAM
        MemberEntity(
            memberId = "MEM-201",
            householdId = "HH-2001",
            name = "Rani Pal",
            age = 2,
            gender = "F",
            category = "Child",
            muacMm = 112 // SAM
        ),
        // Hypertension Follow-up
        MemberEntity(
            memberId = "MEM-202",
            householdId = "HH-2002",
            name = "Shanti Devi",
            age = 58,
            gender = "F",
            category = "Adult",
            hasHypertension = true,
            systolicBp = 158,
            diastolicBp = 96
        ),
        // Diabetes Follow-up
        MemberEntity(
            memberId = "MEM-301",
            householdId = "HH-3001",
            name = "Ram Prasad",
            age = 52,
            gender = "M",
            category = "Adult",
            hasDiabetes = true
        ),
        // Child Due for 9 Months Measles
        MemberEntity(
            memberId = "MEM-401",
            householdId = "HH-4001",
            name = "Ananya Tiwari",
            age = 1,
            gender = "F",
            category = "Infant",
            childDob = "2026-01-20",
            vaccinesReceived = "BCG,OPV-0,Hep-B,Penta-1,Penta-2,Penta-3,fIPV-2"
        )
    )

    // Planned visits for the validation date: 26 October 2026
    val initialVisits = listOf(
        VisitItemEntity(
            id = "VIS-001",
            memberId = "MEM-101",
            householdId = "HH-1001",
            memberName = "Pooja Kumar",
            village = "Amrapur",
            ashaId = "ASHA-101",
            dueItem = "ANC-3 & High-Risk BP/Hb Monitoring",
            priority = 1,
            dueDate = "2026-10-26",
            notes = "Hb was 6.8 g/dL, check for edema and referral compliance"
        ),
        VisitItemEntity(
            id = "VIS-002",
            memberId = "MEM-201",
            householdId = "HH-2001",
            memberName = "Rani Pal",
            village = "Belwadi Tola",
            ashaId = "ASHA-102",
            dueItem = "SAM Follow-up (MUAC < 115mm)",
            priority = 1,
            dueDate = "2026-10-26",
            notes = "Appetite test and check bilateral pedal edema"
        ),
        VisitItemEntity(
            id = "VIS-003",
            memberId = "MEM-103",
            householdId = "HH-1003",
            memberName = "Baby of Geeta Lal",
            village = "Amrapur",
            ashaId = "ASHA-101",
            dueItem = "HBNC Day-3 Visit",
            priority = 1,
            dueDate = "2026-10-26",
            notes = "Weigh newborn, assess temperature and exclusive breastfeeding"
        ),
        VisitItemEntity(
            id = "VIS-004",
            memberId = "MEM-102",
            householdId = "HH-1002",
            memberName = "Kavita Yadav",
            village = "Amrapur",
            ashaId = "ASHA-101",
            dueItem = "ANC-2 Checkup & IFA Distribution",
            priority = 2,
            dueDate = "2026-10-26",
            notes = "20 weeks gestation, distribute 180 IFA & Calcium tablets"
        ),
        VisitItemEntity(
            id = "VIS-005",
            memberId = "MEM-104",
            householdId = "HH-1004",
            memberName = "Aarav Chandra",
            village = "Amrapur",
            ashaId = "ASHA-101",
            dueItem = "Pentavalent-2 & OPV-2 Overdue",
            priority = 2,
            dueDate = "2026-10-26",
            notes = "10 weeks primary dose overdue by 2 weeks"
        ),
        VisitItemEntity(
            id = "VIS-006",
            memberId = "MEM-202",
            householdId = "HH-2002",
            memberName = "Shanti Devi",
            village = "Belwadi Tola",
            ashaId = "ASHA-102",
            dueItem = "Hypertension Monthly BP Check",
            priority = 2,
            dueDate = "2026-10-26",
            notes = "Re-check blood pressure and check antihypertensive compliance"
        ),
        VisitItemEntity(
            id = "VIS-007",
            memberId = "MEM-401",
            householdId = "HH-4001",
            memberName = "Ananya Tiwari",
            village = "Dhanpura",
            ashaId = "ASHA-104",
            dueItem = "MR-1 & Vitamin A Dose 1",
            priority = 2,
            dueDate = "2026-10-26",
            notes = "Completed 9 months, due for Measles-Rubella vaccine"
        ),
        VisitItemEntity(
            id = "VIS-008",
            memberId = "MEM-301",
            householdId = "HH-3001",
            memberName = "Ram Prasad",
            village = "Kesarganj",
            ashaId = "ASHA-103",
            dueItem = "Diabetes Blood Sugar Check",
            priority = 3,
            dueDate = "2026-10-29",
            notes = "Monthly fasting blood glucose follow-up"
        )
    )
}

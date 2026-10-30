package com.example.engine

import com.example.data.local.ProtocolEntity
import com.example.data.model.QuestionAnswer
import com.example.data.sample.ProtocolData
import java.util.Locale
import java.util.UUID

object ProtocolSearchEngine {

    fun answerQuestion(
        query: String,
        protocols: List<ProtocolEntity> = ProtocolData.allProtocols
    ): QuestionAnswer {
        val startTime = System.nanoTime()
        val qTrimmed = query.trim()
        val qId = "Q-" + UUID.randomUUID().toString().take(8).uppercase(Locale.ROOT)

        // 1. Stage 4: Deterministic Emergency Safety Engine (Bypasses all reasoning)
        val emergency = EmergencySafetyEngine.evaluate(qTrimmed)
        if (emergency.isEmergency) {
            val elapsedMs = (System.nanoTime() - startTime) / 1_000_000.0
            return QuestionAnswer(
                id = qId,
                question = qTrimmed,
                answer = "URGENT REFERRAL: ${emergency.dangerCategory}. Action: ${emergency.immediateAction} Call Ambulance (108).",
                citedSections = listOf(emergency.escCitation),
                isEmergency = true,
                emergencyResult = emergency,
                isRefused = false,
                latencyMs = elapsedMs
            )
        }

        // 2. Stage 5: Out of Scope Guard
        val scopeCheck = ScopeGuard.evaluate(qTrimmed)
        if (scopeCheck.isOutOfScope) {
            val elapsedMs = (System.nanoTime() - startTime) / 1_000_000.0
            return QuestionAnswer(
                id = qId,
                question = qTrimmed,
                answer = "REFUSED: ${scopeCheck.reason} Please refer the patient directly to the ${scopeCheck.redirectFacility}.",
                citedSections = listOf("SCP-1.1", "SCP-1.2"),
                isEmergency = false,
                isRefused = true,
                refusalReason = scopeCheck.reason,
                latencyMs = elapsedMs
            )
        }

        // 3. Stage 3: Offline Knowledge Engine (BM25 / Keyword Retrieval)
        val ranked = rankProtocols(qTrimmed, protocols)
        val top = ranked.take(2)
        val elapsedMs = (System.nanoTime() - startTime) / 1_000_000.0

        if (top.isEmpty()) {
            return QuestionAnswer(
                id = qId,
                question = qTrimmed,
                answer = "The approved frontline protocols do not contain specific guidance for this inquiry. Please consult the Medical Officer at Chandrapuri PHC.",
                citedSections = listOf("SCP-1.1"),
                isEmergency = false,
                isRefused = false,
                latencyMs = elapsedMs
            )
        }

        val citedIds = top.map { it.sectionId }
        val primary = top.first()

        // Grounded answer strictly from protocol content (preserving exact numbers, thresholds, dates)
        val answerText = synthesizeAnswer(primary, qTrimmed)

        return QuestionAnswer(
            id = qId,
            question = qTrimmed,
            answer = answerText,
            citedSections = citedIds,
            isEmergency = false,
            isRefused = false,
            latencyMs = elapsedMs
        )
    }

    private fun rankProtocols(query: String, protocols: List<ProtocolEntity>): List<ProtocolEntity> {
        val qTokens = query.lowercase(Locale.ROOT)
            .split(Regex("[^a-z0-9]+"))
            .filter { it.length >= 2 }

        val scored = protocols.map { entity ->
            var score = 0.0
            val titleLower = entity.title.lowercase(Locale.ROOT)
            val contentLower = entity.content.lowercase(Locale.ROOT)
            val keywordsLower = entity.keywords.lowercase(Locale.ROOT)
            val codeLower = entity.protocolCode.lowercase(Locale.ROOT)
            val sectionIdLower = entity.sectionId.lowercase(Locale.ROOT)

            for (token in qTokens) {
                if (sectionIdLower == token) score += 30.0
                if (codeLower == token) score += 15.0
                if (titleLower.contains(token)) score += 8.0
                if (keywordsLower.contains(token)) score += 6.0
                if (contentLower.contains(token)) score += 2.0
            }

            // Code-mixed specific matches
            if (query.contains("anc", ignoreCase = true) && entity.protocolCode == "ANC") score += 10.0
            if ((query.contains("tika", ignoreCase = true) || query.contains("vaccin", ignoreCase = true)) && entity.protocolCode == "IMM") score += 10.0
            if (query.contains("muac", ignoreCase = true) && entity.protocolCode == "NUT") score += 15.0
            if ((query.contains("bp", ignoreCase = true) || query.contains("hypertension", ignoreCase = true)) && entity.sectionId == "NCD-1.1") score += 12.0
            if (query.contains("sugar", ignoreCase = true) && entity.sectionId == "NCD-1.2") score += 12.0
            if (query.contains("dast", ignoreCase = true) && entity.sectionId == "CHD-1.1") score += 15.0
            if (query.contains("saans", ignoreCase = true) && entity.sectionId == "CHD-1.2") score += 15.0
            if (query.contains("hbnc", ignoreCase = true) && entity.protocolCode == "HBNC") score += 12.0

            Pair(entity, score)
        }

        return scored.filter { it.second > 0.0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    private fun synthesizeAnswer(section: ProtocolEntity, query: String): String {
        // Keep grounded, factual, and strictly under 400 characters per H5 evaluation rule
        val text = when (section.sectionId) {
            "ANC-1.1" -> "ANC Schedule: 4 mandatory checkups (ANC-1 within 12wks, ANC-2 at 14-26wks, ANC-3 at 28-34wks, ANC-4 at 36wks to delivery). Measure BP, weight, abdominal exam, and urine test at each visit. [ANC-1.1]"
            "ANC-1.2" -> "Nutrition: Provide 180 IFA tablets (60mg iron + 500mcg folic acid daily from 2nd trimester). Provide 360 Calcium tablets (twice daily, never with IFA). 1 dose Albendazole (400mg) after 14wks. [ANC-1.2]"
            "ANC-1.3" -> "Td Schedule: Td-1 as early as pregnancy confirmed, Td-2 at least 4 weeks after Td-1. If 2 doses received within last 3 years, give 1 Td-Booster dose only. [ANC-1.3]"
            "IMM-1.1" -> "Birth Doses within 24h: BCG 0.05ml intradermal left arm, OPV-0 2 drops orally, and Hepatitis-B birth dose 0.5ml IM anterolateral thigh. [IMM-1.1]"
            "IMM-1.2" -> "Vaccination: 6 wks (Penta-1, OPV-1, RVV-1, fIPV-1, PCV-1); 10 wks (Penta-2, OPV-2, RVV-2); 14 wks (Penta-3, OPV-3, RVV-3, fIPV-2, PCV-2). 4 weeks minimum gap between doses. [IMM-1.2]"
            "IMM-1.3" -> "9 Months: MR-1 (0.5ml SC), PCV Booster (0.5ml IM), JE-1, Vitamin A 1st dose (1 lakh IU). 16-24 Months: MR-2, DPT Booster-1, OPV Booster, Vitamin A 2nd dose (2 lakh IU). [IMM-1.3]"
            "HBNC-1.1" -> "HBNC Visits: Institutional delivery gets 6 visits on Days 3, 7, 14, 21, 28, 42. Home delivery gets 7 visits on Days 1, 3, 7, 14, 21, 28, 42. Check weight, temp, cord stump, breastfeeding. [HBNC-1.1]"
            "HBNC-1.2" -> "Newborn Care: Provide Kangaroo Mother Care (skin-to-skin >= 1 hr/session). Keep cord stump clean and dry (no cow dung/turmeric). Exclusive breastfeeding on demand without prelacteal feeds. [HBNC-1.2]"
            "NUT-1.1" -> "MUAC Tape (6-59 months): Green (>=125mm) Normal; Yellow (115-124mm) MAM (intensive counseling); Red (<115mm) SAM (check edema, appetite test, refer to NRC). Measured on left arm. [NUT-1.1]"
            "NCD-1.1" -> "Hypertension: Screen adults >=30 yrs. Normal <120/80. High BP >=140/90 on 2 readings requires MO referral. Severe BP >=160/100 requires urgent referral to CHC/District Hospital. [NCD-1.1]"
            "NCD-1.2" -> "Diabetes: Fasting blood sugar >=126 mg/dL or Random >=200 mg/dL indicates diabetes. Refer to Medical Officer, promote low sugar and exercise, follow up monthly for drug adherence. [NCD-1.2]"
            "CHD-1.1" -> "Diarrhoea: Give ORS solution + Zinc dispersible tablets for 14 continuous days. Zinc dosage: infants 2-6 months take 10mg daily; children 6-59 months take 20mg daily. Continue feeding. [CHD-1.1]"
            "CHD-1.2" -> "Fast Breathing Count: Age 2-11 months: >= 50 breaths/min; Age 12-59 months: >= 40 breaths/min. Fast breathing or chest indrawing indicates pneumonia; refer urgently to Medical Officer. [CHD-1.2]"
            else -> {
                val excerpt = if (section.content.length > 320) section.content.take(315) + "..." else section.content
                "$excerpt [${section.sectionId}]"
            }
        }

        return if (text.length > 390) text.take(385) + "..." else text
    }
}

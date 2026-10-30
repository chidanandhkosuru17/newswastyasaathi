package com.example.engine

import com.example.data.model.EmergencyResult
import java.util.Locale

object EmergencySafetyEngine {

    /**
     * Deterministic emergency danger-sign detection.
     * Executes BEFORE any retrieval, model reasoning, or language processing.
     * Guaranteed 100% deterministic rules.
     */
    fun evaluate(query: String): EmergencyResult {
        val q = query.lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        // 1. Fits / Convulsions / Seizures (Pregnancy, Labour, Postpartum, Newborn)
        if (containsAny(q, "convulsion", "convulsions", "fit", "fits", "seizure", "seizures",
                "daura", "daure", "jhatka", "jhatke", "convulsiones", "crise de convulsions")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Maternal / Neonatal Convulsions (Eclampsia / Neonatal Fits)",
                escCitation = "ESC-1.1",
                immediateAction = "URGENT: Arrange immediate 108 emergency transport to Chandrapuri District Hospital. Place patient in left lateral tilt, keep airway clear, do not put anything in the mouth, monitor vitals.",
                triggerPattern = "convulsions/fits"
            )
        }

        // 2. Severe Vaginal Bleeding (Pregnancy, Labour, Postpartum)
        if (containsAny(q, "vaginal bleed", "bleeding", "khoon beh", "blood loss", "sangrado",
                "saignement", "haemorrhage", "hemorrhage", "heavy bleed", "pads in one hour", "soaking pads") &&
            !containsAny(q, "normal spot", "light brown discharge without pain")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Severe Obstetric / Postpartum Bleeding (Antepartum/Postpartum Haemorrhage)",
                escCitation = "ESC-1.1",
                immediateAction = "URGENT: Immediate referral to Chandrapuri District Hospital. Call 108 ambulance immediately. Keep patient lying flat with feet elevated, keep warm, accompany with MCP card.",
                triggerPattern = "bleeding"
            )
        }

        // 3. Severe Pre-eclampsia / High BP with blurred vision & headache
        if ((containsAny(q, "severe headache", "headache", "sar dard") &&
                containsAny(q, "blur", "blurred vision", "dhundhla", "vision trouble", "vision borrosa", "vision floue")) ||
            containsAny(q, "160 100", "160/100", "170 110", "180 120", "bp 160", "bp 170", "bp 180", "bp 190")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Impending Eclampsia / Severe Pre-eclampsia",
                escCitation = "ESC-1.1",
                immediateAction = "URGENT: High risk of maternal seizure. Arrange immediate emergency transport to Chandrapuri District Hospital. Do not leave patient unattended, minimize bright lights and noise.",
                triggerPattern = "severe pre-eclampsia"
            )
        }

        // 4. Reduced or Absent Fetal Movements
        if (containsAny(q, "baby not moving", "fetal movement", "bachha hil", "bachha chal", "no movement", "movimiento fetal", "mouvements foetaux") &&
            containsAny(q, "stop", "stopped", "nahi", "absent", "no", "kam", "reduc")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Fetal Distress / Decreased Fetal Movement",
                escCitation = "ESC-1.1",
                immediateAction = "URGENT: Suspected intrauterine fetal distress. Refer immediately to Chandrapuri District Hospital for urgent ultrasound and fetal heart rate auscultation.",
                triggerPattern = "decreased/absent fetal movements"
            )
        }

        // 5. Water Break / Rupture of Membranes > 12 Hours or Prolapse
        if (containsAny(q, "water break", "water broke", "pani gir", "pani chhoot", "amniotic", "bag broke", "rupture of membrane") ||
            containsAny(q, "cord prolapse", "hand prolapse", "limb prolapse", "naal bahar")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Prolonged Rupture of Membranes / Cord Prolapse",
                escCitation = "ESC-1.2",
                immediateAction = "URGENT: Risk of severe maternal/neonatal sepsis or cord compression. Transport immediately to Chandrapuri District Hospital with hips elevated.",
                triggerPattern = "water break / prolapse"
            )
        }

        // 6. Newborn Inability to Feed, Lethargy or Hypothermia
        if (containsAny(q, "newborn", "baby", "infant", "navjat", "bachha", "bebe") &&
            (containsAny(q, "cannot feed", "unable to suck", "not drinking milk", "doodh nahi", "vomiting everything", "vomit all", "vomissant tout") ||
             containsAny(q, "unconscious", "behosh", "limp", "floppy", "cold to touch", "hypothermia", "thanda pad"))) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Newborn Danger Signs (Severe Neonatal Illness / Sepsis)",
                escCitation = "ESC-2.1",
                immediateAction = "URGENT: Severe newborn danger signs. Arrange emergency transport to Chandrapuri District Hospital Special Newborn Care Unit (SNCU). Keep newborn warm against mother's chest (KMC) during transit.",
                triggerPattern = "newborn unable to feed / hypothermia"
            )
        }

        // 7. Fast Breathing with Chest Indrawing in Infant / Young Child
        if (containsAny(q, "chest indrawing", "pasli", "stridor", "grunting", "tirage sous costal") ||
            (containsAny(q, "breath", "saans", "respir") && containsAny(q, "fast", "tez", "rapid", "struggle", "60") && containsAny(q, "baby", "infant", "newborn", "bachha"))) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Severe Pneumonia / Acute Respiratory Distress",
                escCitation = "ESC-2.1",
                immediateAction = "URGENT: Respiratory failure warning sign. Immediate referral to Chandrapuri District Hospital for oxygen and injectable antibiotics. Keep airway clear.",
                triggerPattern = "severe chest indrawing / fast breathing"
            )
        }

        // 8. Severe Dehydration in Children
        if (containsAny(q, "dehydration", "dast", "diarrhoea", "diarrea", "diarrhee") &&
            (containsAny(q, "sunken eyes", "aankhen dhas", "skin pinch", "chamdi wapas", "lethargic", "unconscious", "behosh"))) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Severe Dehydration with Shock Risk",
                escCitation = "ESC-3.1",
                immediateAction = "URGENT: Child is severely dehydrated. Rush to Chandrapuri District Hospital for IV fluids. Give frequent sips of ORS during travel if child is conscious.",
                triggerPattern = "severe dehydration"
            )
        }

        // 9. Bilateral Pitting Edema in Children (SAM with Complications)
        if (containsAny(q, "edema", "oedema", "soojan", "swelling in feet", "swollen feet", "hinchazon") &&
            containsAny(q, "child", "baby", "bachha", "muac", "malnutrition", "kuposhan")) {
            return EmergencyResult(
                isEmergency = true,
                dangerCategory = "Severe Acute Malnutrition (SAM) with Bilateral Edema",
                escCitation = "ESC-3.2",
                immediateAction = "URGENT: Kwashiorkor / Complicated SAM. Refer directly to the Nutrition Rehabilitation Centre (NRC) at Chandrapuri District Hospital. Do not force high-protein feeds without medical supervision.",
                triggerPattern = "bilateral pitting edema in child"
            )
        }

        return EmergencyResult(
            isEmergency = false,
            dangerCategory = "",
            escCitation = "",
            immediateAction = ""
        )
    }

    private fun containsAny(text: String, vararg keywords: String): Boolean {
        for (k in keywords) {
            if (text.contains(k)) return true
        }
        return false
    }
}

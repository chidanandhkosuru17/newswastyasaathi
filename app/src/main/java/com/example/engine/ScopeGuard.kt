package com.example.engine

import java.util.Locale

object ScopeGuard {

    data class ScopeCheckResult(
        val isOutOfScope: Boolean,
        val reason: String = "",
        val redirectFacility: String = "Medical Officer at Chandrapuri Community Health Centre / District Hospital"
    )

    fun evaluate(query: String): ScopeCheckResult {
        val q = query.lowercase(Locale.ROOT)

        val outOfScopeKeywords = listOf(
            "chemotherapy", "cancer surgery", "oncology", "mri scan", "ct scan",
            "antibiotic prescription", "dose of amoxicillin", "dose of azithromycin",
            "steroid dose", "dexamethasone dose", "icu ventilator", "dialysis",
            "cardiac surgery", "insulin pump setup", "psychiatric medication",
            "prescribe medicine for cancer", "how to perform c-section", "surgical removal"
        )

        for (kw in outOfScopeKeywords) {
            if (q.contains(kw)) {
                return ScopeCheckResult(
                    isOutOfScope = true,
                    reason = "This inquiry involves specialized diagnosis, prescription-only pharmaceuticals, or hospital surgical procedures outside frontline ASHA/ANM protocols."
                )
            }
        }

        // Generic "prescribe" or "what antibiotic" check
        if (q.contains("prescribe") || q.contains("prescription for") || q.contains("antibiotic dose")) {
            return ScopeCheckResult(
                isOutOfScope = true,
                reason = "ASHA workers and ANMs are not authorized to prescribe prescription pharmaceuticals or antibiotics outside their standard first-aid kit."
            )
        }

        return ScopeCheckResult(isOutOfScope = false)
    }
}

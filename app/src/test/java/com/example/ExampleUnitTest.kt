package com.example

import com.example.data.repository.AuthRepository
import com.example.engine.EmergencySafetyEngine
import com.example.engine.ProtocolSearchEngine
import com.example.engine.ScopeGuard
import com.example.i18n.AppLanguage
import com.example.i18n.Translations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testEmergencyEngine_MustEscalateDangerSigns() {
        // 1. Fits / convulsions in pregnancy
        val r1 = EmergencySafetyEngine.evaluate("Patient is having convulsions and daure in 8th month pregnancy")
        assertTrue("Must detect convulsions", r1.isEmergency)
        assertEquals("ESC-1.1", r1.escCitation)

        // 2. Severe vaginal bleeding
        val r2 = EmergencySafetyEngine.evaluate("Heavy vaginal bleeding soaking multiple pads")
        assertTrue("Must detect severe bleeding", r2.isEmergency)
        assertEquals("ESC-1.1", r2.escCitation)

        // 3. Hinglish code-mixed bleeding
        val r3 = EmergencySafetyEngine.evaluate("Garbhavastha me khoon beh raha hai bohot zyada")
        assertTrue("Must detect Hinglish bleeding", r3.isEmergency)

        // 4. Baby not moving
        val r4 = EmergencySafetyEngine.evaluate("Baby not moving for past 12 hours bachha hil nahi raha")
        assertTrue("Must detect absent fetal movements", r4.isEmergency)

        // 5. Newborn danger sign
        val r5 = EmergencySafetyEngine.evaluate("Newborn cannot feed and is cold to touch and floppy")
        assertTrue("Must detect newborn danger sign", r5.isEmergency)
        assertEquals("ESC-2.1", r5.escCitation)

        // 6. Non-danger cases must NOT escalate
        val rNonDanger = EmergencySafetyEngine.evaluate("ANC checkup kab hota hai aur schedule kya hai?")
        assertFalse("Normal ANC question must not escalate", rNonDanger.isEmergency)

        val rNonDanger2 = EmergencySafetyEngine.evaluate("How to measure MUAC using shakir tape?")
        assertFalse("MUAC question must not escalate", rNonDanger2.isEmergency)
    }

    @Test
    fun testScopeGuard_RefusesUnsupportedRequests() {
        // Prescription antibiotic dosage request
        val check1 = ScopeGuard.evaluate("What is the antibiotic dose of amoxicillin for child?")
        assertTrue("Must refuse antibiotic prescription", check1.isOutOfScope)

        // Surgical / oncology request
        val check2 = ScopeGuard.evaluate("How to perform cancer surgery?")
        assertTrue("Must refuse cancer surgery", check2.isOutOfScope)

        // In-scope protocol request
        val check3 = ScopeGuard.evaluate("How many IFA tablets are given during pregnancy?")
        assertFalse("Must allow in-scope IFA question", check3.isOutOfScope)
    }

    @Test
    fun testProtocolSearchEngine_GroundingAndCitation() {
        val answer = ProtocolSearchEngine.answerQuestion("ANC registration and checkup schedule")
        assertNotNull(answer)
        assertFalse(answer.isEmergency)
        assertFalse(answer.isRefused)
        assertTrue(answer.citedSections.contains("ANC-1.1"))
        assertTrue(answer.answer.contains("4 mandatory"))
        assertTrue("Answer text must be under 400 chars", answer.answer.length <= 400)
    }

    @Test
    fun testMultiLanguageTranslations() {
        for (lang in AppLanguage.values()) {
            val appName = Translations.get("app_name", lang)
            val signin = Translations.get("sign_in", lang)
            val urgent = Translations.get("urgent_refer_immediately", lang)
            assertTrue("App name must not be empty for ${lang.displayName}", appName.isNotEmpty())
            assertTrue("Sign in text must not be empty for ${lang.displayName}", signin.isNotEmpty())
            assertTrue("Urgent alert text must not be empty for ${lang.displayName}", urgent.isNotEmpty())
            val emergencyTitle = Translations.get("emergency_intake_title", lang)
            assertTrue("Emergency intake title must not be empty for ${lang.displayName}", emergencyTitle.isNotEmpty())
        }
    }

    @Test
    fun testUnrecordedEmergencyCase_OfflineTriageAndSlip() {
        // Scenario: No internet connection. An unregistered pregnant woman rushes in with acute eclampsia & convulsions.
        val unrecordedSymptoms = "Mother 8 months pregnancy, suddenly unconscious with severe convulsions and fits. BP 168/108 mmHg"
        val triageResult = EmergencySafetyEngine.evaluate(unrecordedSymptoms)

        assertTrue("Must detect emergency even for completely unrecorded patient", triageResult.isEmergency)
        assertEquals("ESC-1.1", triageResult.escCitation)
        assertTrue(triageResult.immediateAction.contains("Chandrapuri District Hospital"))

        // Create the emergency case entity
        val emergencyCase = com.example.data.local.EmergencyCaseEntity(
            id = "EMERG-9999",
            patientName = "Unregistered Patient (F, ~24y)",
            approximateAge = 24,
            gender = "F",
            village = "Sitapahari",
            contactPhone = "Relative Present (+91-98700-11111)",
            emergencyType = "Maternal / Obstetric",
            dangerSigns = "Convulsions / Fits (Eclampsia), High BP",
            escCitation = triageResult.escCitation,
            vitalsNotes = unrecordedSymptoms,
            firstAidGiven = "Left lateral tilt, airway cleared, kept warm",
            referralDestination = "Chandrapuri District Hospital (Hotline: 108)",
            recordedAt = "2026-10-30 18:00",
            status = "REFERRED_IN_TRANSIT",
            isSynced = false
        )

        assertNotNull(emergencyCase.id)
        assertFalse("Must be marked unsynced while offline", emergencyCase.isSynced)
        assertEquals("ESC-1.1", emergencyCase.escCitation)
    }

    @Test
    fun testLanguageFromCodeAndDataStoreKey() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("EN"))
        assertEquals(AppLanguage.SPANISH, AppLanguage.fromCode("es"))
        assertEquals(AppLanguage.SPANISH, AppLanguage.fromCode("ES"))
        assertEquals(AppLanguage.FRENCH, AppLanguage.fromCode("fr"))
        assertEquals(AppLanguage.FRENCH, AppLanguage.fromCode("FR"))
        assertEquals(AppLanguage.HINDI, AppLanguage.fromCode("hi"))
        assertEquals(AppLanguage.HINDI, AppLanguage.fromCode("HI"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("unknown"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode(null))
    }

    @Test
    fun testAuthValidation_EmailAndStrength() {
        // ID/Email format validation
        assertTrue("Valid email format", AuthRepository.isValidEmailOrId("asha.devi@swasthya.gov.in"))
        assertTrue("Valid worker ID format", AuthRepository.isValidEmailOrId("ASHA-001"))
        assertFalse("Blank identifier", AuthRepository.isValidEmailOrId(""))
        assertFalse("Too short identifier", AuthRepository.isValidEmailOrId("ab"))

        // Password strength validation
        val weak = AuthRepository.calculatePasswordStrength("123")
        assertEquals(AuthRepository.PasswordStrength.WEAK, weak)

        val medium = AuthRepository.calculatePasswordStrength("asha2026")
        assertTrue(medium == AuthRepository.PasswordStrength.MEDIUM || medium == AuthRepository.PasswordStrength.STRONG)

        val strong = AuthRepository.calculatePasswordStrength("Chandrapuri#2026")
        assertEquals(AuthRepository.PasswordStrength.STRONG, strong)
    }

    @Test
    fun testMultiLanguageEmergencyDangerSigns() {
        // Spanish convulsion / bleeding
        val esDanger = EmergencySafetyEngine.evaluate("Paciente embarazada con convulsiones y sangrado vaginal grave")
        assertTrue("Must detect Spanish emergency danger sign", esDanger.isEmergency)

        // French convulsion / bleeding
        val frDanger = EmergencySafetyEngine.evaluate("Femme enceinte avec convulsions et saignement vaginal abondant")
        assertTrue("Must detect French emergency danger sign", frDanger.isEmergency)

        // Hindi / Hinglish chest indrawing
        val hiDanger = EmergencySafetyEngine.evaluate("Bachhe ki chhati andar dhas rahi hai chest indrawing")
        assertTrue("Must detect pediatric chest indrawing", hiDanger.isEmergency)
    }

    @Test
    fun testSupabaseConfiguration() {
        assertEquals("chidanandhkosuru17's Project", com.example.data.supabase.SupabaseConfig.projectName)
        assertEquals("nyaxlianwlofwwkasupl", com.example.data.supabase.SupabaseConfig.projectId)
        assertTrue(com.example.data.supabase.SupabaseConfig.supabaseUrl.contains("nyaxlianwlofwwkasupl.supabase.co"))
        assertTrue(com.example.data.supabase.SupabaseConfig.supabaseAnonKey.startsWith("sb_publishable_") || com.example.data.supabase.SupabaseConfig.supabaseAnonKey.isNotEmpty())
    }

    @Test
    fun testSupabaseAuthSignUpAndValidation() {
        // Email validation for signup
        assertTrue(AuthRepository.isValidEmail("doctor.verma@chandrapuri.health"))
        assertTrue(AuthRepository.isValidEmail("user@example.com"))
        assertFalse(AuthRepository.isValidEmail("not-an-email"))
        assertFalse(AuthRepository.isValidEmail(""))

        // Password strength meter
        assertEquals(AuthRepository.PasswordStrength.WEAK, AuthRepository.calculatePasswordStrength("123"))
        assertEquals(AuthRepository.PasswordStrength.STRONG, AuthRepository.calculatePasswordStrength("HealthSecure@2026!"))

        // Check translation keys for signup
        for (lang in AppLanguage.values()) {
            val signupTab = Translations.get("tab_sign_up", lang)
            val signinTab = Translations.get("tab_sign_in", lang)
            val fullName = Translations.get("full_name", lang)
            val confirmPass = Translations.get("confirm_password", lang)
            assertTrue("Signup tab must not be empty for ${lang.displayName}", signupTab.isNotEmpty())
            assertTrue("Signin tab must not be empty for ${lang.displayName}", signinTab.isNotEmpty())
            assertTrue("Full name label must not be empty for ${lang.displayName}", fullName.isNotEmpty())
            assertTrue("Confirm password label must not be empty for ${lang.displayName}", confirmPass.isNotEmpty())
        }
    }
}

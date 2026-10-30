package com.example.i18n

object Translations {

    fun get(key: String, lang: AppLanguage): String {
        val map = when (lang) {
            AppLanguage.ENGLISH -> en
            AppLanguage.SPANISH -> es
            AppLanguage.FRENCH -> fr
            AppLanguage.HINDI -> hi
        }
        return map[key] ?: en[key] ?: key
    }

    private val en = mapOf(
        "app_name" to "SwasthyaSaathi",
        "app_tagline" to "Reliable Health Guidance, Even Without a Network",
        "login_title" to "Health Worker Sign In",
        "login_subtitle" to "Access Chandrapuri Block frontline healthcare portal",
        "email_or_id" to "Worker ID or Email",
        "email_or_id_hint" to "e.g. asha.anita@chandrapuri.health or ASHA-101",
        "password" to "Password",
        "password_hint" to "Enter secure password",
        "sign_in" to "Sign In",
        "signing_in" to "Verifying...",
        "remember_me" to "Keep me signed in offline",
        "quick_demo_login" to "Demo Credentials",
        "password_strength" to "Password Strength",
        "strength_weak" to "Weak (Needs at least 6 chars)",
        "strength_medium" to "Medium (Good for offline access)",
        "strength_strong" to "Strong (Great security)",
        "error_invalid_email" to "Please enter a valid email format or Worker ID (e.g., ASHA-101)",
        "error_weak_password" to "Password must be at least 6 characters",
        "error_auth_failed" to "Invalid ID or password. Use demo quick-fill.",

        // Nav
        "nav_home" to "Dashboard",
        "nav_assistant" to "Ask Protocol",
        "nav_planner" to "Visit Planner",
        "nav_protocols" to "Protocols",
        "nav_register" to "Register",
        "nav_diagnostics" to "Diagnostics",
        "nav_settings" to "Settings",

        // Dashboard
        "status_offline" to "Offline Mode (Fully Functional)",
        "status_online" to "Online & Synchronized",
        "sync_pending" to "Pending Sync",
        "sync_now" to "Sync Now",
        "stat_households" to "Households",
        "stat_members" to "Registered Members",
        "stat_pending_visits" to "Pending Visits",
        "stat_high_priority" to "High Priority (P1)",
        "quick_action_ask" to "Ask Health Protocol",
        "quick_action_plan" to "Today's Visit List",
        "quick_action_emergency" to "Emergency Guide",
        "block_chandrapuri" to "Chandrapuri Block",
        "hospital_referral" to "Chandrapuri District Hospital",

        // Emergency
        "urgent_refer_immediately" to "URGENT: Refer Immediately",
        "danger_sign_detected" to "Danger Sign Detected!",
        "ambulance_call" to "Call Ambulance: 108 | Hospital: +91-755-224411",
        "emergency_action" to "Immediate Action Required",
        "view_cited_protocol" to "View Protocol Section",

        // Assistant
        "ask_placeholder" to "Ask protocol question (e.g., 'ANC schedule', 'Bacche ko 6 hafte me kaunsa tika?')",
        "send_question" to "Submit",
        "clear" to "Clear",
        "sample_queries" to "Sample Questions",
        "grounded_answer" to "Protocol Grounded Guidance",
        "cited_sections" to "Cited Protocol Sections",
        "scope_refusal_title" to "Out of Protocol Scope",
        "scope_refusal_msg" to "This inquiry is outside frontline ASHA/ANM protocols. Please refer patient to Medical Officer at Chandrapuri PHC/CHC.",
        "search_hint" to "Answers are strictly grounded in 9 official national healthcare guidelines.",

        // Visit Planner
        "visit_planner_title" to "Daily Household Visit Planner",
        "validation_date" to "Planning Date: 26 Oct 2026",
        "filter_village" to "Filter Village",
        "all_villages" to "All Villages",
        "priority_1" to "Priority 1: High Risk & Overdue",
        "priority_2" to "Priority 2: Routine Due",
        "priority_3" to "Priority 3: Due This Week",
        "mark_completed" to "Mark Completed",
        "completed_badge" to "Completed",
        "export_csv" to "Export H5 CSV",
        "csv_copied" to "Visit list copied to clipboard (H5 format)!",

        // Protocols
        "protocol_library" to "Frontline Healthcare Protocol Library",
        "protocol_disclaimer" to "DISCLAIMER: Educational & operational decision support for trained frontline health workers. Protocols are authoritative guidelines and do not replace clinical doctor judgment.",
        "search_protocols" to "Search protocols or leaf IDs...",

        // Settings
        "settings_title" to "Application Settings",
        "language_setting" to "Display Language",
        "language_desc" to "Choose your preferred language for the interface",
        "network_mode" to "Network Connectivity Simulation",
        "toggle_offline" to "Force Offline Mode",
        "toggle_online" to "Simulate Online Connection",
        "sync_queue_title" to "Local Offline Sync Queue",
        "worker_profile" to "Frontline Worker Profile",
        "logout" to "Sign Out",
        "logout_confirm" to "Are you sure you want to sign out?",

        // Offline Emergency Intake
        "nav_emergency_intake" to "SOS Emergency",
        "emergency_intake_title" to "Offline Emergency Intake & Triage",
        "emergency_intake_desc" to "Immediate triage & referral slip for new, unregistered patients with zero network",
        "patient_info" to "Patient Information (If Known)",
        "patient_name" to "Patient Name",
        "patient_age" to "Approximate Age",
        "patient_gender" to "Gender",
        "patient_village" to "Village / Current Location",
        "contact_phone" to "Attendant Contact Phone",
        "emergency_category" to "Emergency Category",
        "danger_signs_checklist" to "Select Observed Danger Signs",
        "symptoms_notes" to "Symptoms & Vitals (BP, Temp, Pulse)",
        "first_aid_administered" to "First Aid & Pre-Referral Care Given",
        "generate_referral_pass" to "Save & Generate Emergency Referral Slip",
        "offline_saved_alert" to "Case Saved Locally! New patient enrolled and queued for sync when online.",
        "emergency_slip_title" to "OFFLINE EMERGENCY REFERRAL SLIP",
        "referral_hospital" to "Chandrapuri District Hospital (Hotline: 108)",
        "share_slip" to "Copy Referral Slip"
    )

    private val es = mapOf(
        "app_name" to "SwasthyaSaathi",
        "app_tagline" to "Orientación de salud confiable, incluso sin red",
        "login_title" to "Inicio de Sesión de Trabajador de Salud",
        "login_subtitle" to "Acceso al portal de atención primaria del Bloque Chandrapuri",
        "email_or_id" to "ID de Trabajador o Correo",
        "email_or_id_hint" to "ej. asha.anita@chandrapuri.health o ASHA-101",
        "password" to "Contraseña",
        "password_hint" to "Ingrese contraseña segura",
        "sign_in" to "Iniciar Sesión",
        "signing_in" to "Verificando...",
        "remember_me" to "Mantener sesión sin conexión",
        "quick_demo_login" to "Credenciales de Demostración",
        "password_strength" to "Seguridad de Contraseña",
        "strength_weak" to "Débil (Mínimo 6 caracteres)",
        "strength_medium" to "Media (Buena para uso sin conexión)",
        "strength_strong" to "Fuerte (Excelente seguridad)",
        "error_invalid_email" to "Formato de correo no válido o ID de trabajador (ej. ASHA-101)",
        "error_weak_password" to "La contraseña debe tener al menos 6 caracteres",
        "error_auth_failed" to "ID o contraseña no válidos. Use el autocompletado demo.",

        // Nav
        "nav_home" to "Inicio",
        "nav_assistant" to "Consultar",
        "nav_planner" to "Visitas",
        "nav_protocols" to "Protocolos",
        "nav_register" to "Registro",
        "nav_diagnostics" to "Diagnóstico",
        "nav_settings" to "Ajustes",

        // Dashboard
        "status_offline" to "Modo sin conexión (Totalmente funcional)",
        "status_online" to "En línea y sincronizado",
        "sync_pending" to "Sincronización pendiente",
        "sync_now" to "Sincronizar ahora",
        "stat_households" to "Hogares",
        "stat_members" to "Miembros Registrados",
        "stat_pending_visits" to "Visitas Pendientes",
        "stat_high_priority" to "Alta Prioridad (P1)",
        "quick_action_ask" to "Consultar Protocolo",
        "quick_action_plan" to "Visitas de Hoy",
        "quick_action_emergency" to "Guía de Emergencia",
        "block_chandrapuri" to "Bloque Chandrapuri",
        "hospital_referral" to "Hospital de Distrito Chandrapuri",

        // Emergency
        "urgent_refer_immediately" to "URGENTE: Derivar Inmediatamente",
        "danger_sign_detected" to "¡Signo de Peligro Detectado!",
        "ambulance_call" to "Ambulancia: 108 | Hospital: +91-755-224411",
        "emergency_action" to "Acción Inmediata Requerida",
        "view_cited_protocol" to "Ver Sección del Protocolo",

        // Assistant
        "ask_placeholder" to "Pregunte sobre el protocolo (ej., 'calendario prenatal', 'vacunas 6 semanas')",
        "send_question" to "Consultar",
        "clear" to "Limpiar",
        "sample_queries" to "Preguntas de Ejemplo",
        "grounded_answer" to "Respuesta Basada en Protocolo",
        "cited_sections" to "Secciones de Protocolo Citadas",
        "scope_refusal_title" to "Fuera del Alcance del Protocolo",
        "scope_refusal_msg" to "Esta consulta excede los protocolos de atención primaria. Por favor derive al médico en el Centro de Salud Chandrapuri.",
        "search_hint" to "Respuestas estrictamente basadas en 9 guías oficiales de salud.",

        // Visit Planner
        "visit_planner_title" to "Planificador Diario de Visitas Domiciliarias",
        "validation_date" to "Fecha de Planificación: 26 Oct 2026",
        "filter_village" to "Filtrar por Aldea",
        "all_villages" to "Todas las Aldeas",
        "priority_1" to "Prioridad 1: Alto Riesgo y Atrasado",
        "priority_2" to "Prioridad 2: Rutina Pendiente",
        "priority_3" to "Prioridad 3: Próximo Esta Semana",
        "mark_completed" to "Marcar Completada",
        "completed_badge" to "Completada",
        "export_csv" to "Exportar CSV H5",
        "csv_copied" to "¡Lista de visitas copiada al portapapeles!",

        // Protocols
        "protocol_library" to "Biblioteca de Protocolos de Salud",
        "protocol_disclaimer" to "AVISO: Apoyo educativo y operativo para personal de salud. No sustituye el criterio médico cualificado.",
        "search_protocols" to "Buscar protocolos o secciones...",

        // Settings
        "settings_title" to "Configuración de la Aplicación",
        "language_setting" to "Idioma de Visualización",
        "language_desc" to "Seleccione su idioma preferido para la interfaz",
        "network_mode" to "Simulación de Conectividad de Red",
        "toggle_offline" to "Forzar Modo Sin Conexión",
        "toggle_online" to "Simular Conexión En Línea",
        "sync_queue_title" to "Cola Local de Sincronización",
        "worker_profile" to "Perfil del Trabajador de Salud",
        "logout" to "Cerrar Sesión",
        "logout_confirm" to "¿Está seguro de que desea cerrar sesión?"
    )

    private val fr = mapOf(
        "app_name" to "SwasthyaSaathi",
        "app_tagline" to "Conseils de santé fiables, même sans réseau",
        "login_title" to "Connexion Agent de Santé",
        "login_subtitle" to "Accès au portail de soins de santé du bloc de Chandrapuri",
        "email_or_id" to "Identifiant ou Email",
        "email_or_id_hint" to "ex. asha.anita@chandrapuri.health ou ASHA-101",
        "password" to "Mot de passe",
        "password_hint" to "Entrez un mot de passe sécurisé",
        "sign_in" to "Se Connecter",
        "signing_in" to "Vérification...",
        "remember_me" to "Rester connecté hors ligne",
        "quick_demo_login" to "Identifiants de Démo",
        "password_strength" to "Force du mot de passe",
        "strength_weak" to "Faible (6 caractères minimum)",
        "strength_medium" to "Moyen (Bon pour le hors-ligne)",
        "strength_strong" to "Fort (Sécurité optimale)",
        "error_invalid_email" to "Format d'email invalide ou ID d'agent (ex. ASHA-101)",
        "error_weak_password" to "Le mot de passe doit comporter au moins 6 caractères",
        "error_auth_failed" to "Identifiant ou mot de passe incorrect. Utilisez la démo.",

        // Nav
        "nav_home" to "Accueil",
        "nav_assistant" to "Assistant",
        "nav_planner" to "Visites",
        "nav_protocols" to "Protocoles",
        "nav_register" to "Registre",
        "nav_diagnostics" to "Diagnostic",
        "nav_settings" to "Paramètres",

        // Dashboard
        "status_offline" to "Mode Hors Ligne (Entièrement Fonctionnel)",
        "status_online" to "En Ligne & Synchronisé",
        "sync_pending" to "Synchronisation en attente",
        "sync_now" to "Synchroniser",
        "stat_households" to "Ménages",
        "stat_members" to "Membres Enregistrés",
        "stat_pending_visits" to "Visites en attente",
        "stat_high_priority" to "Priorité Haute (P1)",
        "quick_action_ask" to "Consulter Protocole",
        "quick_action_plan" to "Visites du Jour",
        "quick_action_emergency" to "Guide d'Urgence",
        "block_chandrapuri" to "Bloc de Chandrapuri",
        "hospital_referral" to "Hôpital de District de Chandrapuri",

        // Emergency
        "urgent_refer_immediately" to "URGENT : Référer Immédiatement",
        "danger_sign_detected" to "Signe de Danger Détecté !",
        "ambulance_call" to "Ambulance : 108 | Hôpital : +91-755-224411",
        "emergency_action" to "Action Immédiate Requise",
        "view_cited_protocol" to "Voir la section du protocole",

        // Assistant
        "ask_placeholder" to "Poser une question sur le protocole (ex. calendrier prénatal, vaccins 6 semaines)",
        "send_question" to "Envoyer",
        "clear" to "Effacer",
        "sample_queries" to "Exemples de Questions",
        "grounded_answer" to "Réponse Fondée sur le Protocole",
        "cited_sections" to "Sections Citées",
        "scope_refusal_title" to "Hors du Périmètre du Protocole",
        "scope_refusal_msg" to "Cette demande dépasse les protocoles de soins primaires. Veuillez référer au médecin du centre de santé.",
        "search_hint" to "Réponses basées sur les 9 directives médicales officielles.",

        // Visit Planner
        "visit_planner_title" to "Planificateur Quotidien des Visites",
        "validation_date" to "Date de Planification : 26 Oct 2026",
        "filter_village" to "Filtrer par Village",
        "all_villages" to "Tous les Villages",
        "priority_1" to "Priorité 1 : Haut Risque & En Retard",
        "priority_2" to "Priorité 2 : Routine Due",
        "priority_3" to "Priorité 3 : Prévu Cette Semaine",
        "mark_completed" to "Marquer Effectuée",
        "completed_badge" to "Effectuée",
        "export_csv" to "Exporter CSV H5",
        "csv_copied" to "Liste copiée dans le presse-papier !",

        // Protocols
        "protocol_library" to "Bibliothèque des Protocoles de Santé",
        "protocol_disclaimer" to "AVERTISSEMENT : Outil d'aide à la décision pour agents de santé. Ne remplace pas l'avis d'un médecin.",
        "search_protocols" to "Rechercher des protocoles ou sections...",

        // Settings
        "settings_title" to "Paramètres de l'Application",
        "language_setting" to "Langue d'Affichage",
        "language_desc" to "Choisissez votre langue préférée pour l'interface",
        "network_mode" to "Simulation de Connexion Réseau",
        "toggle_offline" to "Forcer le Mode Hors Ligne",
        "toggle_online" to "Simuler la Connexion En Ligne",
        "sync_queue_title" to "File d'Attente de Synchronisation",
        "worker_profile" to "Profil de l'Agent de Santé",
        "logout" to "Se Déconnecter",
        "logout_confirm" to "Êtes-vous sûr de vouloir vous déconnecter ?"
    )

    private val hi = mapOf(
        "app_name" to "SwasthyaSaathi",
        "app_tagline" to "नेटवर्क के बिना भी, विश्वसनीय स्वास्थ्य मार्गदर्शन",
        "login_title" to "स्वास्थ्य कार्यकर्ता लॉगिन",
        "login_subtitle" to "चंद्रापुरी ब्लॉक फ्रंटलाइन स्वास्थ्य पोर्टल में प्रवेश करें",
        "email_or_id" to "कार्यकर्ता आईडी या ईमेल",
        "email_or_id_hint" to "उदा. asha.anita@chandrapuri.health या ASHA-101",
        "password" to "पासवर्ड",
        "password_hint" to "सुरक्षित पासवर्ड दर्ज करें",
        "sign_in" to "लॉगिन करें",
        "signing_in" to "सत्यापित कर रहे हैं...",
        "remember_me" to "ऑफ़लाइन साइन इन रखें",
        "quick_demo_login" to "डेमो लॉगिन क्रेडेंशियल्स",
        "password_strength" to "पासवर्ड मजबूती",
        "strength_weak" to "कमजोर (कम से कम 6 अक्षर चाहिए)",
        "strength_medium" to "मध्यम (ऑफ़लाइन उपयोग के लिए ठीक)",
        "strength_strong" to "मजबूत (उत्कृष्ट सुरक्षा)",
        "error_invalid_email" to "मान्य ईमेल या आईडी दर्ज करें (जैसे ASHA-101)",
        "error_weak_password" to "पासवर्ड कम से कम 6 अक्षरों का होना चाहिए",
        "error_auth_failed" to "गलत आईडी या पासवर्ड। डेमो का उपयोग करें।",

        // Nav
        "nav_home" to "डैशबोर्ड",
        "nav_assistant" to "प्रोटोकॉल पूछें",
        "nav_planner" to "दैनिक विजिट",
        "nav_protocols" to "प्रोटोकॉल",
        "nav_register" to "रजिस्टर",
        "nav_diagnostics" to "डायग्नोस्टिक्स",
        "nav_settings" to "सेटिंग्स",

        // Dashboard
        "status_offline" to "ऑफ़लाइन मोड (पूरी तरह चालू)",
        "status_online" to "ऑनलाइन और सिंक किया हुआ",
        "sync_pending" to "लंबित सिंक",
        "sync_now" to "अभी सिंक करें",
        "stat_households" to "कुल परिवार",
        "stat_members" to "दर्ज सदस्य",
        "stat_pending_visits" to "आज की विजिट",
        "stat_high_priority" to "अति-प्राथमिक (P1)",
        "quick_action_ask" to "स्वास्थ्य सवाल पूछें",
        "quick_action_plan" to "आज की विजिट सूची",
        "quick_action_emergency" to "आपातकालीन गाइड",
        "block_chandrapuri" to "चंद्रापुरी ब्लॉक",
        "hospital_referral" to "चंद्रापुरी जिला अस्पताल",

        // Emergency
        "urgent_refer_immediately" to "अति आवश्यक: तुरंत रेफर करें!",
        "danger_sign_detected" to "खतरे का लक्षण (Danger Sign) पाया गया!",
        "ambulance_call" to "एम्बुलेंस: 108 | जिला अस्पताल: +91-755-224411",
        "emergency_action" to "तुरंत आवश्यक कार्रवाई",
        "view_cited_protocol" to "प्रोटोकॉल अनुभाग देखें",

        // Assistant
        "ask_placeholder" to "सवाल पूछें (उदा. 'ANC kab hota hai', 'Bacche ko 6 hafte me tika')",
        "send_question" to "पूछें",
        "clear" to "साफ करें",
        "sample_queries" to "उदाहरण सवाल",
        "grounded_answer" to "प्रोटोकॉल आधारित उत्तर",
        "cited_sections" to "प्रोटोकॉल संदर्भ अनुभाग",
        "scope_refusal_title" to "प्रोटोकॉल सीमा से बाहर",
        "scope_refusal_msg" to "यह प्रश्न फ्रंटलाइन आशा/एएनएम प्रोटोकॉल के बाहर है। कृपया मरीज को प्राथमिक स्वास्थ्य केंद्र / जिला अस्पताल डॉक्टर के पास भेजें।",
        "search_hint" to "उत्तर केवल 9 आधिकारिक राष्ट्रीय स्वास्थ्य प्रोटोकॉल पर आधारित हैं।",

        // Visit Planner
        "visit_planner_title" to "दैनिक गृह भ्रमण योजनाकार (Visit Planner)",
        "validation_date" to "नियोजन तिथि: 26 अक्टूबर 2026",
        "filter_village" to "गांव चुनें",
        "all_villages" to "सभी गांव",
        "priority_1" to "प्राथमिकता 1: उच्च जोखिम और बकाया",
        "priority_2" to "प्राथमिकता 2: नियमित बकाया",
        "priority_3" to "प्राथमिकता 3: इस सप्ताह देय",
        "mark_completed" to "पूर्ण दर्ज करें",
        "completed_badge" to "पूर्ण",
        "export_csv" to "H5 CSV निर्यात करें",
        "csv_copied" to "विजिट सूची क्लिपबोर्ड पर कॉपी हो गई!",

        // Protocols
        "protocol_library" to "स्वास्थ्य प्रोटोकॉल पुस्तकालय",
        "protocol_disclaimer" to "अस्वीकरण: प्रशिक्षित फ्रंटलाइन स्वास्थ्य कार्यकर्ताओं के लिए परिचालन सहायता। यह योग्य डॉक्टर के निर्णय का विकल्प नहीं है।",
        "search_protocols" to "प्रोटोकॉल या सेक्शन खोजें...",

        // Settings
        "settings_title" to "ऐप सेटिंग्स",
        "language_setting" to "भाषा चुनें (Language)",
        "language_desc" to "अपनी पसंदीदा भाषा का चयन करें",
        "network_mode" to "नेटवर्क कनेक्टिविटी सिमुलेशन",
        "toggle_offline" to "ऑफ़लाइन मोड चालू करें",
        "toggle_online" to "ऑनलाइन मोड चालू करें",
        "sync_queue_title" to "स्थानीय ऑफ़लाइन सिंक कतार",
        "worker_profile" to "स्वास्थ्य कार्यकर्ता प्रोफाइल",
        "logout" to "लॉगआउट करें",
        "logout_confirm" to "क्या आप वाकई लॉगआउट करना चाहते हैं?",

        // Offline Emergency Intake
        "nav_emergency_intake" to "आपातकालीन SOS",
        "emergency_intake_title" to "ऑफ़लाइन आपातकालीन मरीज दाखिला व ट्राइएज",
        "emergency_intake_desc" to "इंटरनेट न होने पर नए/अदर्ज मरीज के लिए तुरंत रेफरल पर्ची व प्राथमिक उपचार",
        "patient_info" to "मरीज की जानकारी (यदि ज्ञात हो)",
        "patient_name" to "मरीज का नाम",
        "patient_age" to "अनुमानित आयु",
        "patient_gender" to "लिंग",
        "patient_village" to "गांव / वर्तमान स्थान",
        "contact_phone" to "तीमारदार का फोन नंबर",
        "emergency_category" to "आपातकाल श्रेणी",
        "danger_signs_checklist" to "देखे गए खतरे के लक्षण चुनें",
        "symptoms_notes" to "लक्षण व वाइटल्स (रक्तचाप, तापमान, नाड़ी)",
        "first_aid_administered" to "दिया गया प्राथमिक उपचार",
        "generate_referral_pass" to "सुरक्षित करें व आपातकालीन रेफरल पर्ची बनाएं",
        "offline_saved_alert" to "केस स्थानीय रूप से सुरक्षित! नया मरीज रजिस्टर में दर्ज और ऑनलाइन होने पर सिंक कतार में शामिल।",
        "emergency_slip_title" to "ऑफ़लाइन आपातकालीन रेफरल पर्ची",
        "referral_hospital" to "चंद्रापुरी जिला अस्पताल (हेल्पलाइन: 108)",
        "share_slip" to "रेफरल पर्ची कॉपी करें"
    )
}

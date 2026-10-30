package com.example.i18n

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val badge: String
) {
    ENGLISH("en", "English", "English", "EN"),
    SPANISH("es", "Spanish", "Español", "ES"),
    FRENCH("fr", "French", "Français", "FR"),
    HINDI("hi", "Hindi", "हिन्दी", "HI");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return when (code?.lowercase()) {
                "es" -> SPANISH
                "fr" -> FRENCH
                "hi" -> HINDI
                else -> ENGLISH
            }
        }
    }
}

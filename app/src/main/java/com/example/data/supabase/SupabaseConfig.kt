package com.example.data.supabase

import com.example.BuildConfig

object SupabaseConfig {
    const val DEFAULT_PROJECT_NAME = "chidanandhkosuru17's Project"
    const val DEFAULT_PROJECT_ID = "nyaxlianwlofwwkasupl"
    const val DEFAULT_URL = "https://nyaxlianwlofwwkasupl.supabase.co"
    const val DEFAULT_ANON_KEY = "sb_publishable_080RQkHefjqAR4MAyTqdzQ_8QgVil1p"

    val projectName: String
        get() = DEFAULT_PROJECT_NAME

    val projectId: String
        get() = DEFAULT_PROJECT_ID

    val supabaseUrl: String
        get() {
            val url = BuildConfig.SUPABASE_URL
            return if (url.isNotBlank() && !url.contains("YOUR_") && url.startsWith("http")) {
                url.trimEnd('/')
            } else {
                DEFAULT_URL
            }
        }

    val supabaseAnonKey: String
        get() {
            val key = BuildConfig.SUPABASE_ANON_KEY
            return if (key.isNotBlank() && !key.contains("YOUR_") && key != "MY_SUPABASE_ANON_KEY") {
                key
            } else {
                DEFAULT_ANON_KEY
            }
        }
}

package com.weblink360.app.data

import android.content.Context
import com.weblink360.app.config.ApiConfig

/** Guarda en SharedPreferences la URL del servidor y la sesión del usuario. */
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("weblink360", Context.MODE_PRIVATE)

    var baseUrl: String
        get() = prefs.getString(KEY_URL, ApiConfig.DEFAULT_BASE_URL) ?: ApiConfig.DEFAULT_BASE_URL
        set(value) = prefs.edit().putString(KEY_URL, value.trim().trimEnd('/')).apply()

    val token: String? get() = prefs.getString(KEY_TOKEN, null)
    val usuario: String get() = prefs.getString(KEY_USUARIO, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED, false)

    fun saveSession(usuario: String, token: String?) {
        prefs.edit()
            .putBoolean(KEY_LOGGED, true)
            .putString(KEY_USUARIO, usuario)
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun clear() {
        prefs.edit()
            .putBoolean(KEY_LOGGED, false)
            .remove(KEY_USUARIO)
            .remove(KEY_TOKEN)
            .apply()
    }

    private companion object {
        const val KEY_URL = "base_url"
        const val KEY_TOKEN = "token"
        const val KEY_USUARIO = "usuario"
        const val KEY_LOGGED = "logged"
    }
}

package com.weblink360.app.util

/** Validaciones de formularios. Devuelven el mensaje de error o null si es válido. */
object Validators {
    const val MIN_USUARIO = 3
    const val MIN_PASSWORD = 6

    fun validarUsuario(usuario: String): String? = when {
        usuario.isBlank() -> "Ingresa el usuario"
        usuario.trim().length < MIN_USUARIO -> "El usuario debe tener mínimo $MIN_USUARIO caracteres"
        else -> null
    }

    fun validarPassword(password: String): String? = when {
        password.isEmpty() -> "Ingresa la contraseña"
        password.length < MIN_PASSWORD -> "La contraseña debe tener mínimo $MIN_PASSWORD caracteres"
        else -> null
    }

    fun validarConfirmacion(password: String, confirmacion: String): String? =
        if (password != confirmacion) "Las contraseñas no coinciden" else null

    fun validarUrl(url: String): String? =
        if (url.trim().startsWith("http://") || url.trim().startsWith("https://")) null
        else "La URL debe empezar por http:// o https://"
}

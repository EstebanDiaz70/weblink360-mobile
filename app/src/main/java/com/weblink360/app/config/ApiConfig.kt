package com.weblink360.app.config

/**
 * Configuración central de la conexión con la API de WebLink360 (Node.js/Express + MySQL,
 * la misma que se probó en Postman en las evidencias AA5-EV02 y AA5-EV04).
 *
 * Si algún endpoint o nombre de campo de tu API es distinto, SOLO se cambia aquí.
 */
object ApiConfig {
    /** 10.0.2.2 = el localhost de tu PC visto desde el emulador de Android Studio. */
    const val DEFAULT_BASE_URL = "http://10.0.2.2:3000"

    // Autenticación
    const val PATH_LOGIN = "/api/login"
    const val PATH_REGISTRO = "/api/registro"

    // Nombres de los campos del JSON que recibe la API
    const val FIELD_USUARIO = "usuario"
    const val FIELD_PASSWORD = "password"
}

package com.weblink360.app.data

import com.weblink360.app.config.ApiConfig
import org.json.JSONObject

/** Repositorio de autenticación: registro e inicio de sesión contra la API. */
class AuthRepository(private val session: SessionManager) {

    fun login(usuario: String, password: String, callback: (Result<ApiResponse>) -> Unit) {
        ApiClient.request("POST", session.baseUrl + ApiConfig.PATH_LOGIN, credenciales(usuario, password), null, callback)
    }

    fun registrar(usuario: String, password: String, callback: (Result<ApiResponse>) -> Unit) {
        ApiClient.request("POST", session.baseUrl + ApiConfig.PATH_REGISTRO, credenciales(usuario, password), null, callback)
    }

    private fun credenciales(usuario: String, password: String): JSONObject =
        JSONObject()
            .put(ApiConfig.FIELD_USUARIO, usuario)
            .put(ApiConfig.FIELD_PASSWORD, password)
}

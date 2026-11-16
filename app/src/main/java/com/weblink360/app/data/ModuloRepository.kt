package com.weblink360.app.data

import com.weblink360.app.config.Modulo

/** Repositorio de los módulos del panel: consulta los listados a la API. */
class ModuloRepository(private val session: SessionManager) {

    fun listar(modulo: Modulo, callback: (Result<ApiResponse>) -> Unit) {
        ApiClient.request("GET", session.baseUrl + modulo.path, null, session.token, callback)
    }
}

package com.weblink360.app.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/** Elemento listo para mostrar en pantalla. */
data class ItemRegistro(val titulo: String, val detalle: String)

/** Interpretación tolerante de las respuestas JSON de la API. */
object JsonParsers {
    private val CLAVES_MENSAJE = listOf("mensaje", "message", "msg", "error")
    private val CLAVES_TOKEN = listOf("token", "accessToken", "access_token")
    private val CLAVES_TITULO = listOf("nombre", "name", "usuario", "username", "titulo", "descripcion", "id")
    private val CLAVES_OCULTAS = listOf("password", "contrasena", "contraseña", "hash")

    fun extraerMensaje(body: String, porDefecto: String): String = try {
        val obj = JSONObject(body)
        CLAVES_MENSAJE.map { if (obj.isNull(it)) "" else obj.optString(it, "") }
            .firstOrNull { it.isNotBlank() } ?: porDefecto
    } catch (e: JSONException) {
        porDefecto
    }

    fun extraerToken(body: String): String? = try {
        val obj = JSONObject(body)
        CLAVES_TOKEN.map { if (obj.isNull(it)) "" else obj.optString(it, "") }
            .firstOrNull { it.isNotBlank() }
    } catch (e: JSONException) {
        null
    }

    /** Acepta un arreglo JSON directo o un objeto que contenga un arreglo (p. ej. {"data":[...]}). */
    fun parsearItems(body: String): List<ItemRegistro> {
        val array = try {
            val texto = body.trim()
            if (texto.startsWith("[")) JSONArray(texto) else primerArreglo(JSONObject(texto))
        } catch (e: JSONException) {
            null
        } ?: return emptyList()

        val items = mutableListOf<ItemRegistro>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val claveTitulo = CLAVES_TITULO.firstOrNull { obj.has(it) && !obj.isNull(it) }
            val titulo = if (claveTitulo != null) obj.optString(claveTitulo) else "Registro ${i + 1}"
            val detalle = obj.keys().asSequence()
                .filter { it != claveTitulo && !obj.isNull(it) }
                .filter { clave -> CLAVES_OCULTAS.none { clave.contains(it, ignoreCase = true) } }
                .joinToString("  ·  ") { "$it: ${obj.optString(it)}" }
            items.add(ItemRegistro(titulo, detalle))
        }
        return items
    }

    private fun primerArreglo(obj: JSONObject): JSONArray? {
        for (clave in obj.keys()) {
            val valor = obj.opt(clave)
            if (valor is JSONArray) return valor
        }
        return null
    }
}

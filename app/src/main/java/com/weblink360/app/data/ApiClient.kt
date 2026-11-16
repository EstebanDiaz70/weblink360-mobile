package com.weblink360.app.data

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Respuesta cruda de la API: código HTTP + cuerpo. */
data class ApiResponse(val code: Int, val body: String) {
    val ok: Boolean get() = code in 200..299
}

/**
 * Cliente HTTP mínimo (HttpURLConnection, sin librerías externas).
 * La petición corre en un hilo secundario y el callback vuelve al hilo principal.
 */
object ApiClient {
    private val executor: ExecutorService = Executors.newFixedThreadPool(2)
    private val mainHandler = Handler(Looper.getMainLooper())

    fun request(
        method: String,
        url: String,
        json: JSONObject? = null,
        token: String? = null,
        callback: (Result<ApiResponse>) -> Unit
    ) {
        executor.execute {
            val result = runCatching { execute(method, url, json, token) }
            mainHandler.post { callback(result) }
        }
    }

    private fun execute(method: String, url: String, json: JSONObject?, token: String?): ApiResponse {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = method
            conn.connectTimeout = 10_000
            conn.readTimeout = 15_000
            conn.setRequestProperty("Accept", "application/json")
            if (!token.isNullOrBlank()) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }
            if (json != null) {
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                conn.outputStream.use { it.write(json.toString().toByteArray(Charsets.UTF_8)) }
            }
            val code = conn.responseCode
            val stream = if (code >= 400) conn.errorStream else conn.inputStream
            val body = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
            return ApiResponse(code, body)
        } finally {
            conn.disconnect()
        }
    }
}

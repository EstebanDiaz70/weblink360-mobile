package com.weblink360.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.weblink360.app.R
import com.weblink360.app.data.AuthRepository
import com.weblink360.app.data.SessionManager
import com.weblink360.app.util.JsonParsers
import com.weblink360.app.util.Validators

/** Pantalla de inicio de sesión. POST {BASE_URL}/api/login */
class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var repo: AuthRepository
    private lateinit var etUrl: TextInputEditText
    private lateinit var etUsuario: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)
        if (session.isLoggedIn()) {
            irAlPanel()
            return
        }
        setContentView(R.layout.activity_login)
        repo = AuthRepository(session)

        etUrl = findViewById(R.id.etUrl)
        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progress = findViewById(R.id.progress)
        etUrl.setText(session.baseUrl)

        btnLogin.setOnClickListener { intentarLogin() }
        findViewById<TextView>(R.id.tvRegistro).setOnClickListener {
            guardarUrlSiEsValida()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun guardarUrlSiEsValida(): Boolean {
        val url = etUrl.text.toString()
        val error = Validators.validarUrl(url)
        etUrl.error = error
        if (error == null) session.baseUrl = url
        return error == null
    }

    private fun intentarLogin() {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        etUsuario.error = Validators.validarUsuario(usuario)
        etPassword.error = Validators.validarPassword(password)
        if (!guardarUrlSiEsValida() || etUsuario.error != null || etPassword.error != null) return

        setCargando(true)
        repo.login(usuario, password) { resultado ->
            setCargando(false)
            resultado.fold(
                onSuccess = { resp ->
                    if (resp.ok) {
                        session.saveSession(usuario, JsonParsers.extraerToken(resp.body))
                        irAlPanel()
                    } else {
                        mostrar(JsonParsers.extraerMensaje(resp.body, "Error en la autenticación"))
                    }
                },
                onFailure = { mostrar("No se pudo conectar con el servidor. Revisa la URL y que la API esté encendida.") }
            )
        }
    }

    private fun irAlPanel() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setCargando(cargando: Boolean) {
        progress.visibility = if (cargando) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !cargando
    }

    private fun mostrar(mensaje: String) = Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
}

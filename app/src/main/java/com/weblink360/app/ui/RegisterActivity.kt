package com.weblink360.app.ui

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

/** Pantalla de registro. POST {BASE_URL}/api/registro */
class RegisterActivity : AppCompatActivity() {

    private lateinit var repo: AuthRepository
    private lateinit var etUsuario: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmar: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        repo = AuthRepository(SessionManager(this))

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        etConfirmar = findViewById(R.id.etConfirmar)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        progress = findViewById(R.id.progress)

        btnRegistrar.setOnClickListener { intentarRegistro() }
        findViewById<TextView>(R.id.tvVolver).setOnClickListener { finish() }
    }

    private fun intentarRegistro() {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmar = etConfirmar.text.toString()

        etUsuario.error = Validators.validarUsuario(usuario)
        etPassword.error = Validators.validarPassword(password)
        etConfirmar.error = Validators.validarConfirmacion(password, confirmar)
        if (etUsuario.error != null || etPassword.error != null || etConfirmar.error != null) return

        setCargando(true)
        repo.registrar(usuario, password) { resultado ->
            setCargando(false)
            resultado.fold(
                onSuccess = { resp ->
                    if (resp.ok) {
                        mostrar(JsonParsers.extraerMensaje(resp.body, "Usuario registrado correctamente"))
                        finish()
                    } else {
                        mostrar(JsonParsers.extraerMensaje(resp.body, "No se pudo registrar el usuario"))
                    }
                },
                onFailure = { mostrar("No se pudo conectar con el servidor.") }
            )
        }
    }

    private fun setCargando(cargando: Boolean) {
        progress.visibility = if (cargando) View.VISIBLE else View.GONE
        btnRegistrar.isEnabled = !cargando
    }

    private fun mostrar(mensaje: String) = Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
}

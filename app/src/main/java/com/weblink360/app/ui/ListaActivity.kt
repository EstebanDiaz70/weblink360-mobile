package com.weblink360.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.weblink360.app.R
import com.weblink360.app.config.Modulo
import com.weblink360.app.data.ModuloRepository
import com.weblink360.app.data.SessionManager
import com.weblink360.app.util.JsonParsers

/** Pantalla genérica de listado: sirve para Productos, Usuarios y Pedidos. */
class ListaActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var repo: ModuloRepository
    private lateinit var modulo: Modulo
    private val adapter = RegistroAdapter()

    private lateinit var progress: ProgressBar
    private lateinit var panelMensaje: View
    private lateinit var tvMensaje: TextView
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        session = SessionManager(this)
        repo = ModuloRepository(session)
        modulo = Modulo.valueOf(intent.getStringExtra(EXTRA_MODULO) ?: Modulo.PRODUCTOS.name)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = modulo.titulo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        progress = findViewById(R.id.progress)
        panelMensaje = findViewById(R.id.panelMensaje)
        tvMensaje = findViewById(R.id.tvMensaje)
        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        findViewById<Button>(R.id.btnReintentar).setOnClickListener { cargar() }

        cargar()
    }

    private fun cargar() {
        progress.visibility = View.VISIBLE
        panelMensaje.visibility = View.GONE
        repo.listar(modulo) { resultado ->
            progress.visibility = View.GONE
            resultado.fold(
                onSuccess = { resp ->
                    when {
                        resp.code == 401 || resp.code == 403 -> cerrarSesionExpirada()
                        !resp.ok -> mostrarMensaje(JsonParsers.extraerMensaje(resp.body, "Error ${resp.code} al consultar ${modulo.titulo}"))
                        else -> {
                            val items = JsonParsers.parsearItems(resp.body)
                            adapter.actualizar(items)
                            if (items.isEmpty()) mostrarMensaje(getString(R.string.sin_registros))
                        }
                    }
                },
                onFailure = { mostrarMensaje("No se pudo conectar con el servidor.") }
            )
        }
    }

    private fun mostrarMensaje(texto: String) {
        tvMensaje.text = texto
        panelMensaje.visibility = View.VISIBLE
    }

    private fun cerrarSesionExpirada() {
        session.clear()
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    companion object {
        const val EXTRA_MODULO = "modulo"
    }
}

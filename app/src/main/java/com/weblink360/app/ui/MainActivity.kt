package com.weblink360.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.weblink360.app.R
import com.weblink360.app.config.Modulo
import com.weblink360.app.data.SessionManager

/** Panel principal: acceso a los módulos y cierre de sesión. */
class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)

        findViewById<TextView>(R.id.tvBienvenida).text = "Hola, ${session.usuario}"
        findViewById<Button>(R.id.btnProductos).setOnClickListener { abrir(Modulo.PRODUCTOS) }
        findViewById<Button>(R.id.btnUsuarios).setOnClickListener { abrir(Modulo.USUARIOS) }
        findViewById<Button>(R.id.btnPedidos).setOnClickListener { abrir(Modulo.PEDIDOS) }
        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun abrir(modulo: Modulo) {
        startActivity(Intent(this, ListaActivity::class.java).putExtra(ListaActivity.EXTRA_MODULO, modulo.name))
    }
}

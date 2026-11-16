package com.weblink360.app.config

/** Módulos del panel administrativo que consumen la API (GET de listados). */
enum class Modulo(val titulo: String, val path: String) {
    PRODUCTOS("Productos", "/api/productos"),
    USUARIOS("Usuarios", "/api/usuarios"),
    PEDIDOS("Pedidos", "/api/pedidos")
}

package com.weblink360.app

import com.weblink360.app.util.JsonParsers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JsonParsersTest {
    @Test fun extraerMensaje_leeClaveMensaje() =
        assertEquals("Autenticación satisfactoria", JsonParsers.extraerMensaje("""{"mensaje":"Autenticación satisfactoria"}""", "x"))

    @Test fun extraerMensaje_cuerpoInvalido_usaPorDefecto() =
        assertEquals("por defecto", JsonParsers.extraerMensaje("no es json", "por defecto"))

    @Test fun extraerToken_presente() = assertEquals("abc", JsonParsers.extraerToken("""{"token":"abc"}"""))
    @Test fun extraerToken_ausente() = assertNull(JsonParsers.extraerToken("""{"mensaje":"ok"}"""))

    @Test fun parsearItems_arregloDirecto() {
        val items = JsonParsers.parsearItems("""[{"id":1,"nombre":"Plan Web","precio":100000}]""")
        assertEquals(1, items.size)
        assertEquals("Plan Web", items[0].titulo)
        assertTrue(items[0].detalle.contains("precio: 100000"))
    }

    @Test fun parsearItems_objetoConArreglo() {
        val items = JsonParsers.parsearItems("""{"data":[{"usuario":"ana"},{"usuario":"luis"}]}""")
        assertEquals(2, items.size)
        assertEquals("luis", items[1].titulo)
    }

    @Test fun parsearItems_ocultaPassword() {
        val items = JsonParsers.parsearItems("""[{"usuario":"ana","password":"secreto"}]""")
        assertFalse(items[0].detalle.contains("secreto"))
    }

    @Test fun parsearItems_invalido_devuelveListaVacia() = assertTrue(JsonParsers.parsearItems("error").isEmpty())
}

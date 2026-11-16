package com.weblink360.app

import com.weblink360.app.util.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {
    @Test fun usuarioVacio_devuelveError() = assertNotNull(Validators.validarUsuario("  "))
    @Test fun usuarioCorto_devuelveError() = assertNotNull(Validators.validarUsuario("ab"))
    @Test fun usuarioValido_noDevuelveError() = assertNull(Validators.validarUsuario("esteban"))
    @Test fun passwordCorta_devuelveError() = assertNotNull(Validators.validarPassword("12345"))
    @Test fun passwordValida_noDevuelveError() = assertNull(Validators.validarPassword("123456"))
    @Test fun confirmacionDistinta_devuelveError() =
        assertEquals("Las contraseñas no coinciden", Validators.validarConfirmacion("abc123", "abc124"))
    @Test fun confirmacionIgual_noDevuelveError() = assertNull(Validators.validarConfirmacion("abc123", "abc123"))
    @Test fun urlSinHttp_devuelveError() = assertNotNull(Validators.validarUrl("10.0.2.2:3000"))
    @Test fun urlConHttp_noDevuelveError() = assertNull(Validators.validarUrl("http://10.0.2.2:3000"))
}

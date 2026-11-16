# WebLink360 Mobile — GA8-220501096-AA2-EV02

Aplicación Android (Kotlin) del proyecto formativo WebLink360. Consume la API Node.js/Express + MySQL
(`weblink360-api`) que se desarrolló en la AA5-EV03 y se probó con Postman en la AA5-EV04.

## 1. Ambiente de desarrollo
- Android Studio (Koala o superior), JDK 17 (incluido en Android Studio), Android SDK 34.
- minSdk 24 · targetSdk 34 · Kotlin 1.9.24 · AGP 8.5.2 · Gradle 8.7.
- Librerías (capa de presentación): AndroidX AppCompat, Material Components, RecyclerView. Sin Retrofit: la capa de red usa `HttpURLConnection`.

## 2. Cómo abrir y ejecutar
1. Descomprimir el ZIP y en Android Studio: **File > Open** y elegir la carpeta `WebLink360Mobile`.
2. Esperar el *Gradle Sync* (necesita internet la primera vez).
3. En tu PC, encender la API: `npm start` dentro de `weblink360-api` (puerto 3000) y MySQL activo.
4. Ejecutar la app (▶) en un emulador.

## 3. Conexión con la API
| Dónde corre la app | URL del servidor |
|---|---|
| Emulador de Android Studio | `http://10.0.2.2:3000` (valor por defecto) |
| Celular físico (mismo Wi‑Fi) | `http://IP_DE_TU_PC:3000` (ej. `http://192.168.1.10:3000`) |

La URL se puede cambiar en la pantalla de login sin recompilar. Rutas y nombres de campos se cambian en
`config/ApiConfig.kt` y `config/Modulo.kt`.

| Pantalla | Método y endpoint | Cuerpo |
|---|---|---|
| Login | `POST /api/login` | `{"usuario","password"}` |
| Registro | `POST /api/registro` | `{"usuario","password"}` |
| Productos | `GET /api/productos` | — (envía `Authorization: Bearer <token>` si el login devolvió token) |
| Usuarios | `GET /api/usuarios` | — |
| Pedidos | `GET /api/pedidos` | — |

> Verifica que estos endpoints coincidan con tu colección de Postman; si difieren, ajústalos en `ApiConfig.kt` / `Modulo.kt`.

## 4. Arquitectura (capas y paquetes)
```
com.weblink360.app
├── config/  ApiConfig, Modulo            -> configuración
├── data/    ApiClient, SessionManager,   -> acceso a datos / red (patrón Repository)
│            AuthRepository, ModuloRepository
├── ui/      Login, Register, Main,       -> presentación (Activities + Adapter)
│            ListaActivity, RegistroAdapter
└── util/    Validators, JsonParsers      -> utilidades puras (con pruebas unitarias)
```
Patrones: Repository, Singleton (`ApiClient`), Adapter (RecyclerView), componente reutilizable (`ListaActivity` sirve a 3 módulos).

## 5. Mapa de navegación
`Login` ⇄ `Registro` → `Panel` → `Lista (Productos | Usuarios | Pedidos)` → `Cerrar sesión` → `Login`

## 6. Pruebas unitarias
`Validators` y `JsonParsers`: Android Studio > clic derecho en `app/src/test` > *Run Tests*, o `./gradlew testDebugUnitTest`.

## 7. Generar el APK
**Build > Build Bundle(s) / APK(s) > Build APK(s)** → `app/build/outputs/apk/debug/app-debug.apk`.

## 8. Control de versiones
```
git init
git add .
git commit -m "feat: app Android WebLink360 conectada a la API"
git remote add origin https://github.com/EstebanDiaz70/weblink360-mobile.git
git push -u origin main
```

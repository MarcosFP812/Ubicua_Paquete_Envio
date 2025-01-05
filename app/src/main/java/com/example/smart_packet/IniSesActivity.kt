
package com.example.smart_packet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.HttpURLConnection
import java.net.URL

class IniSesActivity : AppCompatActivity() {
    private val tag = "Iniciar Sesion"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate")
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_inises)
        supportActionBar!!.hide()

        val editNombre = findViewById<EditText>(R.id.miEditText)
        val editPw = findViewById<EditText>(R.id.miEditText2)

        val btnI = findViewById<Button>(R.id.btnI)
        btnI.setOnClickListener {
            val nombre = editNombre.text.toString()
            val pw = editPw.text.toString()

            if (nombre.isNotEmpty() && pw.isNotEmpty()) {
                // Llamamos a la función para validar al cliente
                validarCliente(nombre, pw)
            } else {
                Toast.makeText(this, "Por favor, ingrese nombre y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        val registrarse = findViewById<TextView>(R.id.clickableText)
        registrarse.setOnClickListener {
            val i = Intent(this@IniSesActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        val btnAtras = findViewById<ImageView>(R.id.flecha)
        btnAtras.setOnClickListener {
            val i = Intent(this@IniSesActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Función para validar al cliente
    private fun validarCliente(nombre: String, pw: String) {
        val urlString = "http://192.168.1.203:8080/ServerExampleUbicomp-1.0-SNAPSHOT/ValidarCliente?nombre=$nombre&pw=$pw"

        // Hacemos la solicitud en un hilo en segundo plano para no bloquear el hilo principal
        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"  // Hacemos una solicitud GET

                // Establecemos tiempos de espera para la conexión
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode

                // Si la respuesta del servidor es OK (200), validamos al cliente
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Aquí puedes leer la respuesta del servidor si lo deseas
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // En este caso, consideramos que si el servidor responde con "validado", el cliente está autenticado
                    if (response.contains("validado")) {
                        runOnUiThread {
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            val i = Intent(this@IniSesActivity, HistorialActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Si la respuesta no es OK (200), mostramos un error
                    runOnUiThread {
                        Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                }
                Log.e(tag, "Error en la conexión al servidor", e)
            }
        }
        thread.start()  // Iniciamos el hilo
    }
}

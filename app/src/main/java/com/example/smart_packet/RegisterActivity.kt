package com.example.smart_packet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.location.Geocoder
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.HttpURLConnection
import java.net.URL
import android.location.Address
import com.example.smart_packet.data.Receptor
import com.example.smart_packet.data.Ubicacion
import java.io.IOException
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private val tag = "Register"
    private val listaReceptores: HashMap<String, Receptor> = hashMapOf()
    private var idCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate")
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        supportActionBar!!.hide()
        findViewById<View>(R.id.txt4).visibility = View.GONE
        findViewById<View>(R.id.miEditText3).visibility = View.GONE

        val btnU: Button = findViewById(R.id.btnU)
        val btnE: Button = findViewById(R.id.btnE)
        val editNombre = findViewById<EditText>(R.id.miEditText)
        val editPw = findViewById<EditText>(R.id.miEditText2)
        val editDirection = findViewById<EditText>(R.id.miEditText3)
        var direccion: List<Double>? = listOf(0.0, 0.0)

        btnU.setOnClickListener {
            btnU.setBackgroundColor(Color.parseColor("#028BC3")) // Color seleccionado
            btnE.setBackgroundColor(Color.GRAY) // Color deseleccionado
            findViewById<View>(R.id.txt4).visibility = View.VISIBLE
            findViewById<View>(R.id.miEditText3).visibility = View.VISIBLE
        }

        btnE.setOnClickListener {
            btnE.setBackgroundColor(Color.parseColor("#028BC3")) // Color seleccionado
            btnU.setBackgroundColor(Color.GRAY) // Color deseleccionado
            findViewById<View>(R.id.txt4).visibility = View.GONE
            findViewById<View>(R.id.miEditText3).visibility = View.GONE
            direccion = listOf(0.0,0.0)
        }

        val btnR = findViewById<Button>(R.id.btn3)
        btnR.setOnClickListener {
            val nombre = editNombre.text.toString()
            val pw = editPw.text.toString()
            val tipo = if (btnU.isActivated) "Receptor" else "Remitente"
            if(tipo == "Receptor"){
                direccion = obtenerCoordenadas(editDirection.text.toString())
            }

            if (nombre.isNotEmpty() && pw.isNotEmpty() && direccion!=null) {
                val coord1 = direccion?.get(0)
                val coord2 = direccion?.get(1)
                if (coord1 != null && coord2 != null){
                    sendClientDataToServer(nombre, pw, coord1, coord2, tipo)
                    val i = Intent(this@RegisterActivity, IniSesActivity::class.java)
                    if (tipo == "Receptor"){
                        idCount++
                        listaReceptores[nombre] = Receptor(idCount, nombre, pw, tipo, Ubicacion(coord1, coord2))
                        i.putExtra("idReceptor", idCount)
                    }
                    Toast.makeText(this, "Registro completado", Toast.LENGTH_SHORT).show()
                    startActivity(i)
                    finish()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }

        }

        val btniniciarSesion = findViewById<TextView>(R.id.clickableText)
        btniniciarSesion.setOnClickListener {
            val i = Intent(this@RegisterActivity, IniSesActivity::class.java)
            startActivity(i)
            finish()
        }

        val btnAtras = findViewById<ImageView>(R.id.flecha)
        btnAtras.setOnClickListener {
            val i = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun obtenerCoordenadas(direccion: String): List<Double>? {
        val geocoder = Geocoder(this, Locale.getDefault())

        return try {
            // Intentamos obtener la dirección
            val direcciones: List<Address>? = geocoder.getFromLocationName(direccion, 1)

            if (direcciones != null && direcciones.isNotEmpty()) {
                // Obtenemos la primera dirección encontrada
                val direccionObtenida = direcciones[0]
                val latitud = direccionObtenida.latitude
                val longitud = direccionObtenida.longitude

                listOf(longitud, latitud)
            } else {
                // Si no se encuentra la dirección, mostramos el mensaje y retornamos null
                Toast.makeText(this, "No se encontraron resultados para la dirección. Pruebe con otra.", Toast.LENGTH_SHORT).show()
                null
            }
        } catch (e: IOException) {
            // En caso de error de entrada/salida (por ejemplo, sin conexión a Internet)
            e.printStackTrace()
            Toast.makeText(this, "Error al obtener las coordenadas.", Toast.LENGTH_SHORT).show()
            null
        }
    }


    private fun sendClientDataToServer(nombre: String, pw: String, longitud: Double, latitud: Double, tipo: String) {
        val thread = Thread {
            try {
                val url = URL("http://192.168.1.203:8080/ServerExampleUbicomp-1.0-SNAPSHOT/RegistrarCliente?nombre=$nombre&pw=$pw&longitud=$longitud&latitud=$latitud&tipo=$tipo")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET" // O "POST" si necesitas enviar los datos en el cuerpo de la solicitud
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(tag, "Cliente registrado exitosamente")
                } else {
                    Log.e(tag, "Error al registrar cliente: Código $responseCode")
                }
                conn.disconnect()
            } catch (e: Exception) {
                Log.e(tag, "Error en la conexión al servidor", e)
            }
        }
        thread.start()
    }
}

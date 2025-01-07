package com.example.smart_packet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.appcompat.app.AlertDialog
import com.example.smart_packet.data.GlobalVariables
import org.json.JSONArray
import org.json.JSONException
import java.net.HttpURLConnection
import java.net.URL


class VerPaqueteActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var tipo: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Comprobar si es empresa o cliente
        var tipo = intent.getStringExtra("tipo")
        if (tipo == "Remitente"){
            setContentView(R.layout.activity_ver_paquete_e) // Usa el layout que definimos antes
            val idEnvio = intent.getStringExtra("idEnvio")
            val btnCancelar: Button = findViewById(R.id.btnCancelar)
            if (idEnvio != null) {
                btnCancelar.setOnClickListener {
                    // Crear el AlertDialog
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("¿Seguro que desea cancelar el envío?")
                        .setCancelable(false) // Evita que se cierre al tocar fuera del diálogo
                        .setPositiveButton("Sí") { dialog, id ->
                            // Acción si el usuario elige "Sí"
                            cancelarEnvio(idEnvio)
                        }
                        .setNegativeButton("No") { dialog, id ->
                            // Acción si el usuario elige "No"
                            dialog.dismiss()  // Cierra el diálogo
                        }

                    // Mostrar el diálogo
                    val alert = builder.create()
                    alert.show()
                }
            }
        } else if (tipo == "Receptor"){
            setContentView((R.layout.activity_ver_paquete_c))
            val btnPin: Button = findViewById(R.id.btnPin)
            btnPin.setOnClickListener {
                val idEnvio = intent.getStringExtra("idEnvio")
                // Crear el código que deseas mostrar
                if (idEnvio!=null) {
                    val codigo = generarCodigoEnvio(idEnvio)
                    // Crear el AlertDialog para mostrar el código
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Código de Seguridad")  // Título del diálogo
                        .setMessage("Tu código es: $codigo")  // Mostrar el código en el mensaje
                        .setCancelable(true)  // Permitir que se cierre tocando fuera del diálogo
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()  // Cerrar el diálogo al presionar "OK"
                        }

                    // Mostrar el diálogo
                    val alert = builder.create()
                    alert.show()
                } else {
                    Log.e("Error", "El ID no fue recibido en el Intent")
                }
            }
        }

        supportActionBar!!.hide()
        // Configurar el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapa) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val idEnvio = intent.getIntExtra("idEnvio", -1)
        if (idEnvio != -1) {
            obtenerTemperaturasYHumedades(idEnvio)
        } else {
            Toast.makeText(this, "ID de envío no válido", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val idEnvio = intent.getIntExtra("idEnvio", -1)
        if (idEnvio != -1) {
            obtenerUbicaciones(idEnvio)
        } else {
            Toast.makeText(this, "ID de envío no válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerUbicaciones(idEnvio: Int) {
        val urlString = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerUbicaciones?idEnvio=$idEnvio"

        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val ubicaciones = parseUbicaciones(response)
                    runOnUiThread {
                        mostrarUbicacionesEnMapa(ubicaciones)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al obtener ubicaciones", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show()
                }
                Log.e("VerPaqueteActivity", "Error al obtener ubicaciones", e)
            }
        }
        thread.start()
    }

    private fun parseUbicaciones(response: String): List<LatLng> {
        val ubicaciones = mutableListOf<LatLng>()
        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val lat = jsonObject.getDouble("latitud")
                val lng = jsonObject.getDouble("longitud")
                ubicaciones.add(LatLng(lat, lng))
            }
        } catch (e: JSONException) {
            Log.e("VerPaqueteActivity", "Error al parsear JSON", e)
        }
        return ubicaciones
    }

    private fun mostrarUbicacionesEnMapa(ubicaciones: List<LatLng>) {
        if (mMap == null) return

        for (ubicacion in ubicaciones) {
            mMap!!.addMarker(MarkerOptions().position(ubicacion).title("Ubicación del paquete"))
        }

        if (ubicaciones.isNotEmpty()) {
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicaciones[0], 15f))
        }
    }

    private fun obtenerTemperaturasYHumedades(idEnvio: Int) {
        val urlString = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerTemperaturasHumedades?idEnvio=$idEnvio"

        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val datos = parseTemperaturasYHumedades(response)
                    runOnUiThread {
                        mostrarDatosEnGrafico(datos)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al obtener temperaturas y humedades", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show()
                }
                Log.e("VerPaqueteActivity", "Error al obtener temperaturas y humedades", e)
            }
        }
        thread.start()
    }


    private fun parseTemperaturasYHumedades(response: String): List<Pair<Float, Float>> {
        val datos = mutableListOf<Pair<Float, Float>>()
        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val temperatura = jsonObject.getDouble("temperatura").toFloat()
                val humedad = jsonObject.getDouble("humedad").toFloat()
                datos.add(Pair(temperatura, humedad))
            }
        } catch (e: JSONException) {
            Log.e("VerPaqueteActivity", "Error al parsear JSON", e)
        }
        return datos
    }

    private fun mostrarDatosEnGrafico(datos: List<Pair<Float, Float>>) {
        val combinedChart = findViewById<LineChart>(R.id.combinedChart)

        val temperatureData = datos.mapIndexed { index, pair ->
            Entry(index.toFloat() + 1, pair.first)
        }

        val humidityData = datos.mapIndexed { index, pair ->
            Entry(index.toFloat() + 1, pair.second)
        }

        val temperatureDataSet = LineDataSet(temperatureData, "Temperatura (°C)")
        temperatureDataSet.color = Color.RED
        temperatureDataSet.valueTextColor = Color.BLACK
        temperatureDataSet.lineWidth = 2f
        temperatureDataSet.setDrawCircles(true)
        temperatureDataSet.setCircleColor(Color.RED)
        temperatureDataSet.axisDependency = YAxis.AxisDependency.LEFT

        val humidityDataSet = LineDataSet(humidityData, "Humedad (%)")
        humidityDataSet.color = Color.BLUE
        humidityDataSet.valueTextColor = Color.BLACK
        humidityDataSet.lineWidth = 2f
        humidityDataSet.setDrawCircles(true)
        humidityDataSet.setCircleColor(Color.BLUE)
        humidityDataSet.axisDependency = YAxis.AxisDependency.RIGHT

        val lineData = LineData(temperatureDataSet, humidityDataSet)
        combinedChart.data = lineData
        combinedChart.setBackgroundColor(Color.WHITE)
        combinedChart.setDrawGridBackground(false)

        val leftAxis = combinedChart.axisLeft
        leftAxis.textColor = Color.RED
        leftAxis.axisMinimum = 0f

        val rightAxis = combinedChart.axisRight
        rightAxis.textColor = Color.BLUE
        rightAxis.axisMinimum = 0f

        combinedChart.description.text = "Temperatura y Humedad"
        combinedChart.description.textSize = 12f

        combinedChart.invalidate()
    }

    // Función para cancelar el envío
    fun cancelarEnvio() {
        // Lógica para cancelar el envío
        // Aquí puedes agregar lo que debe suceder cuando el usuario confirma la cancelación
        Toast.makeText(this, "Envío cancelado", Toast.LENGTH_SHORT).show()
    }

    // Función para generar el código de seguridad PIN
    private fun generarCodigoEnvio(idEnvio: String) {
        // URL del servlet para generar el PIN
        val urlString = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/GenerarPinEnvio?idEnvio=$idEnvio"

        // Realizamos la solicitud HTTP en un hilo separado
        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"  // Hacemos una solicitud GET

                // Establecemos tiempos de espera para la conexión
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode

                // Si la respuesta del servidor es OK (200), leemos el PIN
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Supongamos que el servidor devuelve el PIN directamente como una cadena
                    runOnUiThread {
                        mostrarCodigoPin(response)  // Mostramos el código en un AlertDialog
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al generar el PIN", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show()
                }
                Log.e("VerPaqueteActivity", "Error al generar el PIN", e)
            }
        }
        thread.start()  // Iniciamos el hilo
    }

    // Función para mostrar el código PIN en un AlertDialog
    private fun mostrarCodigoPin(codigo: String) {
        // Crear el AlertDialog para mostrar el código
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Código de Seguridad")  // Título del diálogo
            .setMessage("Tu código es: $codigo")  // Mostrar el código en el mensaje
            .setCancelable(true)  // Permitir que se cierre tocando fuera del diálogo
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()  // Cerrar el diálogo al presionar "OK"
            }

        // Mostrar el diálogo
        val alert = builder.create()
        alert.show()
    }


    private fun cancelarEnvio(idEnvio: String) {
        // URL del servlet para generar el PIN
        val urlString = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/CancelarEnvio?idEnvio=$idEnvio"

        // Realizamos la solicitud HTTP en un hilo separado
        val thread = Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"  // Hacemos una solicitud GET

                // Establecemos tiempos de espera para la conexión
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode

                // Si la respuesta del servidor es OK (200), leemos la respuesta
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }


                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al cancelar el envio", Toast.LENGTH_SHORT).show()
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error en la conexión con el servidor", Toast.LENGTH_SHORT).show()
                }
                Log.e("VerPaqueteActivity", "Error al cancelar el envio", e)
            }
        }.start()
    }
}



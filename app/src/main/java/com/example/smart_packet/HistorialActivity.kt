package com.example.smart_packet


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_packet.data.ListAdapter
import com.example.smart_packet.data.ListElement
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class HistorialActivity : AppCompatActivity() {
    private var elementsE: ArrayList<ListElement>? = ArrayList() // Enviado
    private var elementsEs: ArrayList<ListElement>? = ArrayList() // Envio
    private var elementsC: ArrayList<ListElement>? = ArrayList() // Cancelado


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        supportActionBar!!.hide()
        init()
    }

    fun init() {

        obtenerEnviosCliente(intent.getIntExtra("idReceptor", -1))

        //Envio
        val listAdapterE = ListAdapter(elementsE ?: emptyList(), this)
        val recyclerViewE = findViewById<RecyclerView>(R.id.enviorv)
        recyclerViewE.setHasFixedSize(true)
        recyclerViewE.layoutManager = LinearLayoutManager(this)
        recyclerViewE.adapter = listAdapterE

        //Enviados
        val listAdapterEs = ListAdapter(elementsEs ?: emptyList(), this)
        val recyclerViewEs = findViewById<RecyclerView>(R.id.enviadosrv)
        recyclerViewEs.setHasFixedSize(true)
        recyclerViewEs.layoutManager = LinearLayoutManager(this)
        recyclerViewEs.adapter = listAdapterEs

        //Cancelados
        val listAdapterC = ListAdapter(elementsC ?: emptyList(), this)
        val recyclerViewC = findViewById<RecyclerView>(R.id.canceladosrv)
        recyclerViewC.setHasFixedSize(true)
        recyclerViewC.layoutManager = LinearLayoutManager(this)
        recyclerViewC.adapter = listAdapterC
    }

    fun generarColor(): String {
        // Definir una lista de códigos de colores en formato String
        val colores = listOf(
            "#FF0000",  // Rojo
            "#00FF00",  // Verde
            "#0000FF",  // Azul
            "#FFFF00",  // Amarillo
            "#FF00FF",  // Magenta
            "#00FFFF",  // Cian
            "#808080",  // Gris
            "#000000",  // Negro
            "#A52A2A",  // Marrón
            "#D2691E",  // Chocolate
            "#32CD32",  // Verde Lima
            "#FFD700",  // Dorado
            "#FF6347",  // Tomate
            "#8A2BE2",  // Azul Violeta
            "#F0E68C"   // Amarillo Claro
        )

        // Generar un índice aleatorio y devolver el código de color correspondiente
        return colores.random() // Devuelve un código de color aleatorio
    }

    private fun obtenerEnviosCliente(idCliente: Int) {
        // Ejecutamos la solicitud en un hilo en segundo plano para no bloquear el UI
        Thread {
            try {
                // Definir la URL
                val url = URL("http://192.168.1.153:8080/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerEnviosCliente?idCliente=$idCliente")
                val connection = url.openConnection() as HttpURLConnection

                // Configuración de la conexión HTTP
                connection.requestMethod = "GET"  // Método GET
                connection.connectTimeout = 15000  // Tiempo de espera para la conexión (15 segundos)
                connection.readTimeout = 15000  // Tiempo de espera para leer datos (15 segundos)
                connection.doInput = true  // Permite la entrada de datos (lectura)

                // Conectar a la URL
                connection.connect()

                // Leer la respuesta de la solicitud
                val responseCode = connection.responseCode  // Obtener el código de respuesta HTTP
                if (responseCode == HttpURLConnection.HTTP_OK) { // Si la respuesta es OK (200)
                    val reader = InputStreamReader(connection.inputStream)
                    val stringBuilder = StringBuilder()
                    var character: Int
                    while (reader.read().also { character = it } != -1) {
                        stringBuilder.append(character.toChar())
                    }
                    reader.close()

                    // Procesar la respuesta (stringBuilder.toString() contiene el cuerpo de la respuesta)
                    val response = stringBuilder.toString()
                    // Aquí procesamos la respuesta para filtrar por estado
                    procesarEnvios(response)

                } else {
                    // Manejar el error si la respuesta no es OK
                    Log.e("HistorialActivity", "Error en la conexión: $responseCode")
                    runOnUiThread {
                        Toast.makeText(this@HistorialActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
                    }
                }

                // Cerrar la conexión
                connection.disconnect()

            } catch (e: Exception) {
                // Manejar cualquier excepción
                Log.e("HistorialActivity", "Excepción: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@HistorialActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()  // Ejecutar el hilo en segundo plano
    }

    // Función para procesar los envíos y filtrarlos por estado
    private fun procesarEnvios(response: String) {
        try {
            val jsonArray = JSONArray(response)


            for (i in 0 until jsonArray.length()) {
                val envio = jsonArray.getJSONObject(i)
                val estado = envio.getString("estado")
                val idEnvio = envio.getString("idEnvio")
                val detalle = envio.getString("detalle")

                // Crear el ListElement con el detalle del paquete y su estado
                val listElement = ListElement(generarColor(), detalle)

                // Filtrar y añadir el elemento a la lista correspondiente
                when (estado) {
                    "enviado" -> elementsEs?.add(listElement)  // Enviado
                    "en envio" -> elementsE?.add(listElement)  // En envio
                    "cancelado" -> elementsC?.add(listElement)  // Cancelado
                }
            }

            // Actualizar las vistas de los RecyclerView en el hilo principal
            runOnUiThread {
                actualizarRecyclerViews()
            }
        } catch (e: Exception) {
            Log.e("HistorialActivity", "Error al procesar los datos: ${e.message}")
        }
    }

    // Función para actualizar los RecyclerView con los datos filtrados
    private fun actualizarRecyclerViews() {
        // Enviados
        val listAdapterEs = ListAdapter(elementsEs ?: emptyList(), this)
        val recyclerViewEs = findViewById<RecyclerView>(R.id.enviadosrv)
        recyclerViewEs.setHasFixedSize(true)
        recyclerViewEs.layoutManager = LinearLayoutManager(this)
        recyclerViewEs.adapter = listAdapterEs

        // En envio
        val listAdapterE = ListAdapter(elementsE ?: emptyList(), this)
        val recyclerViewE = findViewById<RecyclerView>(R.id.enviorv)
        recyclerViewE.setHasFixedSize(true)
        recyclerViewE.layoutManager = LinearLayoutManager(this)
        recyclerViewE.adapter = listAdapterE

        // Cancelados
        val listAdapterC = ListAdapter(elementsC ?: emptyList(), this)
        val recyclerViewC = findViewById<RecyclerView>(R.id.canceladosrv)
        recyclerViewC.setHasFixedSize(true)
        recyclerViewC.layoutManager = LinearLayoutManager(this)
        recyclerViewC.adapter = listAdapterC
    }

}
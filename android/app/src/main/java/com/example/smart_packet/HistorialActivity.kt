package com.example.smart_packet


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_packet.data.GlobalVariables
import com.example.smart_packet.data.ListAdapter
import com.example.smart_packet.data.ListElement
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class HistorialActivity : AppCompatActivity() {
    private var tipo: String = ""
    private var elementsE: ArrayList<ListElement>? = ArrayList() // Enviado
    private var elementsEs: ArrayList<ListElement>? = ArrayList() // Envio
    private var elementsC: ArrayList<ListElement>? = ArrayList() // Cancelado
    private var idCliente: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        supportActionBar!!.hide()
        mostrarMenu()
        init()
    }

    override fun onResume() {
        super.onResume()

        // Limpiar las listas para evitar duplicados
        elementsE?.clear()
        elementsEs?.clear()
        elementsC?.clear()

        // Volver a cargar los datos del cliente
        if (idCliente.isNotEmpty()) {
            obtenerEnviosCliente(idCliente) // Esto recargará los datos desde el servidor
        } else {
            Log.e("HistorialActivity", "El ID del cliente no está disponible para recargar los datos")
        }
    }


    fun mostrarMenu(){
        val menu: ImageView = findViewById(R.id.menu)
        menu.setOnClickListener { view ->
            // Crear el objeto PopupMenu
            val popupMenu = PopupMenu(this, view)

            // Inflar el menú desde el archivo XML
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

            // Configurar el evento para manejar los clics en las opciones del menú
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_option1 -> {
                        val intent = Intent(this, HistorialActivity::class.java)
                        intent.putExtra("id", idCliente)
                        startActivity(intent)
                        true
                    }
                    R.id.item_option2 -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            // Mostrar el menú emergente
            popupMenu.show()
        }
    }

    fun init() {
        val id = intent.getStringExtra("id")
        if (id != null) {
            idCliente = id;
            obtenerEnviosCliente(id)
            obtenerTipo(id)
        } else {
            Log.e("Error", "El ID no fue recibido en el Intent")
        }

        val listAdapterE = ListAdapter(elementsE ?: emptyList(), this, tipo)
        val recyclerViewE = findViewById<RecyclerView>(R.id.enviorv)
        recyclerViewE.setHasFixedSize(true)
        recyclerViewE.layoutManager = LinearLayoutManager(this)
        recyclerViewE.adapter = listAdapterE

        //Enviados
        val listAdapterEs = ListAdapter(elementsEs ?: emptyList(), this, tipo)
        val recyclerViewEs = findViewById<RecyclerView>(R.id.enviadosrv)
        recyclerViewEs.setHasFixedSize(true)
        recyclerViewEs.layoutManager = LinearLayoutManager(this)
        recyclerViewEs.adapter = listAdapterEs

        //Cancelados
        val listAdapterC = ListAdapter(elementsC ?: emptyList(), this, tipo)
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

    private fun obtenerEnviosCliente(idCliente: String) {
        // Ejecutamos la solicitud en un hilo en segundo plano para no bloquear el UI
        Thread {
            try {
                // Definir la URL
                val url = URL("http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerEnviosCliente?idCliente=$idCliente")
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

    private fun procesarEnvios(response: String) {
        try {
            // Parsear la respuesta JSON
            val jsonObject = JSONObject(response)

            // Obtener los diferentes grupos de envíos
            val envios = jsonObject.getJSONArray("Envio")
            val enviados = jsonObject.getJSONArray("Enviado")
            val cancelados = jsonObject.getJSONArray("Cancelado")

            // Procesar cada uno de los envíos en "Envio"
            for (i in 0 until envios.length()) {
                val envio = envios.getJSONObject(i)
                val estado = envio.getString("finalizado")
                val idEnvio = envio.getString("idEnvio")
                val detalle = envio.getInt("paqueteId")  // Asumimos que "paqueteId" es el detalle del paquete

                // Crear el ListElement con el detalle del paquete y su estado
                val listElement = ListElement(generarColor(), idEnvio, estado, idCliente)

                // Filtrar y añadir el elemento a la lista correspondiente según el estado
                when (estado) {
                    "Envio" -> elementsE?.add(listElement)  // Envio
                }
            }

            // Procesar los envíos en "Enviado"
            for (i in 0 until enviados.length()) {
                val envio = enviados.getJSONObject(i)
                val estado = envio.getString("finalizado")
                val idEnvio = envio.getString("idEnvio")
                val detalle = envio.getInt("paqueteId")

                // Crear el ListElement
                val listElement = ListElement(generarColor(), idEnvio,estado, idCliente)

                // Filtrar y añadir el elemento a la lista correspondiente
                when (estado) {
                    "Enviado" -> elementsEs?.add(listElement)  // Enviado
                }
            }

            // Procesar los envíos en "Cancelado"
            for (i in 0 until cancelados.length()) {
                val envio = cancelados.getJSONObject(i)
                val estado = envio.getString("finalizado")
                val idEnvio = envio.getString("idEnvio")
                val detalle = envio.getInt("paqueteId")

                // Crear el ListElement
                val listElement = ListElement(generarColor(), idEnvio, estado, idCliente)

                // Filtrar y añadir el elemento a la lista correspondiente
                when (estado) {
                    "Cancelado" -> elementsC?.add(listElement)  // Cancelado
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
        val listAdapterEs = ListAdapter(elementsEs ?: emptyList(), this, tipo)
        val recyclerViewEs = findViewById<RecyclerView>(R.id.enviadosrv)
        recyclerViewEs.setHasFixedSize(true)
        recyclerViewEs.layoutManager = LinearLayoutManager(this)
        recyclerViewEs.adapter = listAdapterEs

        // En envio
        val listAdapterE = ListAdapter(elementsE ?: emptyList(), this, tipo)
        val recyclerViewE = findViewById<RecyclerView>(R.id.enviorv)
        recyclerViewE.setHasFixedSize(true)
        recyclerViewE.layoutManager = LinearLayoutManager(this)
        recyclerViewE.adapter = listAdapterE

        // Cancelados
        val listAdapterC = ListAdapter(elementsC ?: emptyList(), this, tipo)
        val recyclerViewC = findViewById<RecyclerView>(R.id.canceladosrv)
        recyclerViewC.setHasFixedSize(true)
        recyclerViewC.layoutManager = LinearLayoutManager(this)
        recyclerViewC.adapter = listAdapterC
    }

    private fun obtenerTipo(idCliente: String) {
        // Ejecutamos la solicitud en un hilo en segundo plano para no bloquear el UI
        Thread {
            try {
                // Definir la URL del servlet
                val url = URL("http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerTipo?idCliente=$idCliente")
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

                    // Procesar la respuesta
                    val response = stringBuilder.toString()

                    // Aquí asumimos que el servlet devuelve un string como "Remitente" o "Destinatario"
                    runOnUiThread {
                        tipo = response.trim()  // Asigna el valor de tipo desde la respuesta
                        actualizarInterfaz()    // Llama a una función para actualizar la interfaz según el tipo
                    }
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

    private fun actualizarInterfaz() {
        val btnEnvio = findViewById<Button>(R.id.btnEnvio)
        if (tipo == "Remitente") {
            btnEnvio.visibility = View.VISIBLE
            btnEnvio.setOnClickListener {
                val i = Intent(this@HistorialActivity, EnvioActivity::class.java)
                i.putExtra("id", idCliente)
                startActivity(i)
                finish()
            }
        } else {
            btnEnvio.visibility = View.GONE
        }
    }


}
package com.example.smart_packet
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.example.smart_packet.data.GlobalVariables
import java.io.OutputStream
import kotlin.math.roundToInt


class EnvioActivity : AppCompatActivity() {

    private var selectedReceptorId: String? = null
    private var selectedTransportistaId: String? = null
    private var lastSelectedRowReceptor: TableRow? = null
    private var lastSelectedRowTransportista: TableRow? = null
    private var idCliente: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        supportActionBar!!.hide()
        setContentView(R.layout.activity_envio)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("id")
        if (id != null) {
            idCliente = id
        };
        // Permitir operaciones de red en el hilo principal (solo para pruebas, usa AsyncTask o coroutines para producción)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // URLs de los servidores
        val urlReceptores =
            "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerReceptores"

        val tablaReceptores = findViewById<TableLayout>(R.id.tabla)
        cargarTabla(tablaReceptores, urlReceptores, "Id", false)
        setupTableRowSelection(tablaReceptores, isReceptor = true)

        val tablaTransportistas = findViewById<TableLayout>(R.id.tabla1)
        val urlTransportistas =
            "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerTransportistas?idReceptor=$selectedReceptorId&idRemitente=$idCliente"
        cargarTabla(tablaTransportistas, urlTransportistas, "id", true)
        setupTableRowSelection(tablaTransportistas, isReceptor = false)

        val enviarButton = findViewById<View>(R.id.btn1)
        val edit = findViewById<EditText>(R.id.max)
        val edit1 = findViewById<EditText>(R.id.min)
        val edit2 = findViewById<EditText>(R.id.paquete)
        enviarButton.setOnClickListener {
            if (selectedReceptorId == null || selectedTransportistaId == null) {
                Toast.makeText(
                    this,
                    "Debe seleccionar un receptor y un transportista.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edit == null || edit1==null || edit2 == null ){
                Toast.makeText(
                    this,
                    "Debe llenar todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val tmax = edit.text.toString()
                val tmin = edit.text.toString()
                val idpaquete = edit.text.toString()
                val url = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/CrearEnvio?idTransportista=$selectedTransportistaId&idPaquete=$idpaquete&idReceptor=$selectedReceptorId&idRemitente=$id&temperatura_max=$tmax&temperatura_min=$tmin"
                val params = mapOf(
                    "idTransportista" to selectedTransportistaId!!,
                    "idReceptor" to selectedReceptorId!!,
                    "idPaquete" to edit2,
                    "idRemitente" to id,
                    "temperatura_max" to edit,
                    "temperatura_min" to edit1
                )
                val datosString: Map<String, String> = params.mapValues { it.value.toString() }
                sendPostRequest(url, datosString)

            }
        }
        mostrarMenu()
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

    private fun cargarTabla(tabla: TableLayout, urlString: String, idKey: String, es_transp: Boolean) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                // Parsear el JSON
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getString(idKey)
                    val nombre = jsonObject.getString("nombre")
                    if (es_transp) {
                        val tiempoEnvio = jsonObject.getDouble("TiempoEnvio")
                        val tiempoPerdida = jsonObject.getDouble("TiempoPerdida")
                    }
                    // Crear una nueva fila para la tabla
                    val fila = TableRow(this)
                    val textView = TextView(this)
                    textView.text = nombre
                    textView.setPadding(16, 16, 16, 16)
                    fila.addView(textView)

                    // Añadir la fila a la tabla
                    tabla.addView(fila)

                    // Agregar el clic en la fila para seleccionar el transportista
                    fila.setOnClickListener {
                        selectedTransportistaId = id
                        if(es_transp) {
                            mostrarMensajeDeTiempo(tiempoEnvio, tiempoPerdida)
                        }
                    }
                }
            } else {
                Log.e("HTTP_ERROR", "Código de respuesta: ${connection.responseCode}")
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Error al cargar datos: ${e.message}")
        }
    }

    private fun mostrarMensajeDeTiempo(tiempoEnvio: Double, tiempoPerdida: Double) {
        val mensajePerdida = clasificarTiempo(tiempoPerdida)

        // Mostrar el mensaje en un Toast o en un cuadro de texto
        val mensaje = "Tiempo de Envío: $tiempoEnvio minutos\n" +
                "Tiempo de Pérdida: $tiempoPerdida minutos ($mensajePerdida)"

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun clasificarTiempo(tiempo: Double): String {
        return when {
            tiempo <= 3 -> "Bien conservado"
            tiempo > 3 && tiempo <= 5 -> "Aceptable"
            else -> "Mal"
        }
    }


    private fun setupTableRowSelection(table: TableLayout, isReceptor: Boolean) {
        for (i in 0 until table.childCount) {
            val row = table.getChildAt(i) as TableRow

            row.setOnClickListener {
                if (isReceptor) {
                    // Selección de receptor
                    lastSelectedRowReceptor?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    lastSelectedRowReceptor = row
                    val idTextView = row.getChildAt(0) as TextView
                    selectedReceptorId = idTextView.text.toString()
                    row.setBackgroundColor(Color.parseColor("#01A0E1"))
                } else {
                    // Validación: No permitir seleccionar transportista si no se ha seleccionado receptor
                    if (selectedReceptorId == null) {
                        Toast.makeText(
                            this,
                            "Debe seleccionar un receptor antes de elegir un transportista.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Selección de transportista
                        lastSelectedRowTransportista?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        lastSelectedRowTransportista = row
                        val idTextView = row.getChildAt(0) as TextView
                        selectedTransportistaId = idTextView.text.toString()
                        row.setBackgroundColor(Color.parseColor("#01A0E1"))
                    }
                }
            }
        }
    }


    private fun sendPostRequest(urlString: String, params: Map<String, Any>) {
                // Habilitar política de red para operaciones en el hilo principal (solo para demostración)
                StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder().permitNetwork().build()
                )

                Thread {
                    try {
                        val url = URL(urlString)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "POST"
                        connection.doOutput = true
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                        val postData = params.entries.joinToString("&") { "${it.key}=${it.value}" }

                        // Escribir los datos en el cuerpo de la solicitud
                        val outputStream: OutputStream = connection.outputStream
                        outputStream.write(postData.toByteArray())
                        outputStream.flush()
                        outputStream.close()

                        // Leer la respuesta
                        val responseCode = connection.responseCode

                        runOnUiThread {
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                Toast.makeText(this, "Envío exitoso", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Error en el envío: $responseCode", Toast.LENGTH_LONG).show()
                            }
                        }
                        connection.disconnect()
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }.start()


}
}



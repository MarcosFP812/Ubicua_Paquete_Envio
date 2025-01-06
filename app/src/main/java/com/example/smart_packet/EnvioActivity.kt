package com.example.smart_packet
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
import android.widget.Toast
import com.example.smart_packet.data.GlobalVariables
import java.io.OutputStream


class EnvioActivity : AppCompatActivity() {

    private var selectedReceptorId: String? = null
    private var selectedTransportistaId: String? = null
    private var lastSelectedRowReceptor: TableRow? = null
    private var lastSelectedRowTransportista: TableRow? = null
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

        // Permitir operaciones de red en el hilo principal (solo para pruebas, usa AsyncTask o coroutines para producción)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // URLs de los servidores
        val urlReceptores =
            "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerReceptores"
        val urlTransportistas =
            "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/ObtenerTransportistas"

        // Tablas en el diseño
        val tablaReceptore = findViewById<TableLayout>(R.id.tabla)
        val tablaTransportista = findViewById<TableLayout>(R.id.tabla1)

        // Cargar datos en las tablas
        cargarTabla(tablaReceptore, urlReceptores, "id")
        cargarTabla(tablaTransportista, urlTransportistas, "id")

        val tablaReceptores = findViewById<TableLayout>(R.id.tabla)
        val tablaTransportistas = findViewById<TableLayout>(R.id.tabla1)

        setupTableRowSelection(tablaReceptores, isReceptor = true)
        setupTableRowSelection(tablaTransportistas, isReceptor = false)

        val enviarButton = findViewById<View>(R.id.btn1)
        enviarButton.setOnClickListener {
            if (selectedReceptorId == null || selectedTransportistaId == null) {
                Toast.makeText(
                    this,
                    "Debe seleccionar un receptor y un transportista.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val url = "http://${GlobalVariables.myGlobalUrl}/ServerExampleUbicomp-1.0-SNAPSHOT/CrearEnvio"
                val params = mapOf(
                    "idTransportista" to selectedTransportistaId!!,
                    "idReceptor" to selectedReceptorId!!,
                    "idPaquete" to "1",
                    "idRemitente" to "2",
                    "temperatura_max" to "8",
                    "temperatura_min" to "2"
                )
                sendPostRequest(url, params)


            }
        }
    }

    private fun cargarTabla(tabla: TableLayout, urlString: String, idKey: String) {
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

                    // Crear una nueva fila para la tabla
                    val fila = TableRow(this)
                    val textView = TextView(this)
                    textView.text = id
                    textView.setPadding(16, 16, 16, 16)
                    fila.addView(textView)

                    // Añadir la fila a la tabla
                    tabla.addView(fila)
                }
            } else {
                Log.e("HTTP_ERROR", "Código de respuesta: ${connection.responseCode}")
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Error al cargar datos: ${e.message}")
        }
    }
            private fun setupTableRowSelection(table: TableLayout, isReceptor: Boolean) {
                for (i in 0 until table.childCount) {
                    val row = table.getChildAt(i) as TableRow

                    row.setOnClickListener {
                        if (isReceptor) {
                            lastSelectedRowReceptor?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            lastSelectedRowReceptor = row
                            val idTextView = row.getChildAt(0) as TextView
                            selectedReceptorId = idTextView.text.toString()
                        } else {
                            lastSelectedRowTransportista?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            lastSelectedRowTransportista = row
                            val idTextView = row.getChildAt(0) as TextView
                            selectedTransportistaId = idTextView.text.toString()
                        }
                        row.setBackgroundColor(android.graphics.Color.LTGRAY)
                    }
                }
            }

            private fun sendPostRequest(urlString: String, params: Map<String, String>) {
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
                        val responseMessage = connection.inputStream.bufferedReader().readText()

                        runOnUiThread {
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                Toast.makeText(this, "Envío exitoso: $responseMessage", Toast.LENGTH_LONG).show()
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



package com.example.smart_packet

import android.graphics.Color
import android.os.Bundle
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
import java.util.Random


class VerPaqueteActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Comprobar si es empresa o cliente

        /*
        setContentView(R.layout.activity_ver_paquete_e) // Usa el layout que definimos antes
        val btnCancelar: Button = findViewById(R.id.btnCancelar)

        btnCancelar.setOnClickListener {
            // Crear el AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Seguro que desea cancelar el envío?")
                .setCancelable(false) // Evita que se cierre al tocar fuera del diálogo
                .setPositiveButton("Sí") { dialog, id ->
                    // Acción si el usuario elige "Sí"
                    cancelarEnvio()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Acción si el usuario elige "No"
                    dialog.dismiss()  // Cierra el diálogo
                }

            // Mostrar el diálogo
            val alert = builder.create()
            alert.show()
        }*/



        setContentView((R.layout.activity_ver_paquete_c))
        val btnPin: Button = findViewById(R.id.btnPin)
        btnPin.setOnClickListener {
            // Crear el código que deseas mostrar
            val codigo = generarCodigo();

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

        supportActionBar!!.hide()
        // Configurar el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapa) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        val combinedChart = findViewById<LineChart>(R.id.combinedChart)
        val temperatureData = listOf(
            Entry(1f, 20f),  // Tiempo: 1, Temperatura: 20°C
            Entry(2f, 22f),
            Entry(3f, 24f),
            Entry(4f, 23f),
            Entry(5f, 25f)
        )

        val humidityData = listOf(
            Entry(1f, 50f),  // Tiempo: 1, Humedad: 50%
            Entry(2f, 55f),
            Entry(3f, 53f),
            Entry(4f, 60f),
            Entry(5f, 58f)
        )

        // Crear los DataSet para temperatura y humedad
        val temperatureDataSet = LineDataSet(temperatureData, "Temperatura (°C)")
        temperatureDataSet.color = Color.RED
        temperatureDataSet.valueTextColor = Color.BLACK
        temperatureDataSet.lineWidth = 2f
        temperatureDataSet.setDrawCircles(true)
        temperatureDataSet.setCircleColor(Color.RED)
        temperatureDataSet.axisDependency = YAxis.AxisDependency.LEFT // Eje izquierdo

        val humidityDataSet = LineDataSet(humidityData, "Humedad (%)")
        humidityDataSet.color = Color.BLUE
        humidityDataSet.valueTextColor = Color.BLACK
        humidityDataSet.lineWidth = 2f
        humidityDataSet.setDrawCircles(true)
        humidityDataSet.setCircleColor(Color.BLUE)
        humidityDataSet.axisDependency = YAxis.AxisDependency.RIGHT // Eje derecho

        // Configurar el gráfico
        val lineData = LineData(temperatureDataSet, humidityDataSet)
        combinedChart.data = lineData
        combinedChart.setBackgroundColor(Color.WHITE)
        combinedChart.setDrawGridBackground(false)

        // Personalizar ejes
        val leftAxis = combinedChart.axisLeft
        leftAxis.textColor = Color.RED
        leftAxis.axisMinimum = 0f // Ajusta según tus datos

        val rightAxis = combinedChart.axisRight
        rightAxis.textColor = Color.BLUE
        rightAxis.axisMinimum = 0f // Ajusta según tus datos

        // Descripción
        combinedChart.description.text = "Temperatura y Humedad"
        combinedChart.description.textSize = 12f

        // Actualizar el gráfico
        combinedChart.invalidate()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Coordenadas de la ubicación del paquete (ejemplo de coordenadas)
        val packageLocation =
            LatLng(40.748817, -73.985428) // Nueva York, cambia a las coordenadas que desees
        mMap!!.addMarker(MarkerOptions().position(packageLocation).title("Ubicación del paquete"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(packageLocation, 15f)) // Zoom nivel 15
    }

    // Función para cancelar el envío
    fun cancelarEnvio() {
        // Lógica para cancelar el envío
        // Aquí puedes agregar lo que debe suceder cuando el usuario confirma la cancelación
        Toast.makeText(this, "Envío cancelado", Toast.LENGTH_SHORT).show()
    }

    fun generarCodigo() : String {
        val caracteres = "0123456789"  // Solo números para el PIN
        val random = Random()
        val pin = StringBuilder()

        // Generar el PIN con la longitud especificada
        for (i in 0 until 5) {
            val randomIndex = random.nextInt(caracteres.length)
            pin.append(caracteres[randomIndex])
        }
        return pin.toString()
    }


}
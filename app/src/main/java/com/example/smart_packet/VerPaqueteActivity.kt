package com.example.smart_packet

import android.graphics.Color
import android.os.Bundle
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

class VerPaqueteActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Comprobar si es empresa o cliente
        setContentView(R.layout.activity_ver_paquete_e) // Usa el layout que definimos antes
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
}
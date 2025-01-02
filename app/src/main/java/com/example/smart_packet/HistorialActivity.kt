package com.example.smart_packet


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_packet.data.ListAdapter
import com.example.smart_packet.data.ListElement


class HistorialActivity : AppCompatActivity() {
    private var elementsE: ArrayList<ListElement>? = null
    private var elementsEs: ArrayList<ListElement>? = null
    private var elementsC: ArrayList<ListElement>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        supportActionBar!!.hide()
        init()
    }

    fun init() {
        elementsE = ArrayList()
        elementsEs = ArrayList()
        elementsC = ArrayList()

        //Envio
        elementsE?.add(ListElement(generarColor(), "0000-0000"))
        elementsE?.add(ListElement(generarColor(), "0000-0010"))
        elementsE?.add(ListElement(generarColor(), "0000-0011"))
        elementsE?.add(ListElement(generarColor(), "0000-0012"))
        val listAdapterE = ListAdapter(elementsE ?: emptyList(), this)
        val recyclerViewE = findViewById<RecyclerView>(R.id.enviorv)
        recyclerViewE.setHasFixedSize(true)
        recyclerViewE.layoutManager = LinearLayoutManager(this)
        recyclerViewE.adapter = listAdapterE
        //Enviados
        elementsEs?.add(ListElement(generarColor(), "0000-0001"))
        elementsEs?.add(ListElement(generarColor(), "0000-0006"))
        elementsEs?.add(ListElement(generarColor(), "0000-0004"))
        elementsEs?.add(ListElement(generarColor(), "0000-0007"))
        val listAdapterEs = ListAdapter(elementsEs ?: emptyList(), this)
        val recyclerViewEs = findViewById<RecyclerView>(R.id.enviadosrv)
        recyclerViewEs.setHasFixedSize(true)
        recyclerViewEs.layoutManager = LinearLayoutManager(this)
        recyclerViewEs.adapter = listAdapterEs
        //Cancelados
        elementsC?.add(ListElement(generarColor(), "0000-0002"))
        elementsC?.add(ListElement(generarColor(), "0000-0005"))
        elementsC?.add(ListElement(generarColor(), "0000-0008"))
        elementsC?.add(ListElement(generarColor(), "0000-0009"))
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


}
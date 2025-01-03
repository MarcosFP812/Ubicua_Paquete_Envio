package com.example.smart_packet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    private val tag = "Register"

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
        //Almacenar el valor seleccionado
        // Comprobar si ha sido pulsado el boton usuario
        btnU.setOnClickListener {
            btnU.setBackgroundColor(Color.parseColor("#028BC3")) // Color seleccionado
            btnE.setBackgroundColor(Color.GRAY) // Color deseleccionado
            findViewById<View>(R.id.txt4).visibility = View.VISIBLE
            findViewById<View>(R.id.miEditText3).visibility = View.VISIBLE
        }

        // Comprobar si ha sido pulsado el boton empresa
        btnE.setOnClickListener {
            btnE.setBackgroundColor(Color.parseColor("#028BC3")) // Color seleccionado
            btnU.setBackgroundColor(Color.GRAY) // Color deseleccionado
            findViewById<View>(R.id.txt4).visibility = View.GONE
            findViewById<View>(R.id.miEditText3).visibility = View.GONE
        }

        //Almacenar nuevo usuario

        val btnR  = findViewById<Button>(R.id.btn3)
        btnR.setOnClickListener {
            Toast.makeText(this, "Registro completado", Toast.LENGTH_SHORT).show()
            val i = Intent(this@RegisterActivity, IniSesActivity::class.java)
            startActivity(i)
            finish()
        }

        val btniniciarSesion = findViewById<TextView>(R.id.clickableText)
        btniniciarSesion.setOnClickListener{
            val i = Intent(this@RegisterActivity, IniSesActivity::class.java)
            startActivity(i)
            finish()
        }

        val btnAtras = findViewById<ImageView>(R.id.flecha)
        btnAtras.setOnClickListener{
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

        /*

    //Init the spinners and the button
    this.spinnerCities = this.findViewById(R.id.spinnerCity);
    Log.e(tag, "cities");
    this.spinnerStations = this.findViewById(R.id.spinnerStation);
    Log.e(tag, "stations");
    this.buttonStation = this.findViewById(R.id.buttonStation);
    Log.e(tag, "button");

    //init the arraylist to incorpore the information
    this.listCities = new ArrayList<>();
    this.listStation = new ArrayList<>();
    this.arrayCities = new ArrayList<>();
    this.arrayStations = new ArrayList<>();

    //Add action when the spinner of the cities changes
    spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int id = listCities.get(i).getId();//Get the id of the selected position
            Log.i(tag, "City selected:" + listCities.get(i).getName());

            //Get the list of stations of the selected city and set them into the spinner
            loadStations(listCities.get(i).getId());
            spinnerStations.setAdapter(new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, arrayStations));
            if(arrayStations.size()>0) {
                spinnerStations.setSelection(0);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    });

    //Add action when the spinner of the stations changes
    spinnerStations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                idStation = listStation.get(i).getId();//Get the id of the selected position
                nameStation = listStation.get(i).getName();
                Log.i(tag, "Station selected:" + listStation.get(i).getName());
            }catch (Exception e){
                Log.e(tag, "Error on selecting Station:" + e.toString());
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    });

    buttonStation.setOnClickListener( new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(tag, "Button pressed");
            Intent i = new Intent(SelectStationActivity.this, StationActivity.class);
            i.putExtra("stationId", "station" + idStation);
            i.putExtra("stationName", nameStation);
            startActivity(i);
            finish();
        }
    });
    //Initial load of cities and stations
    loadCities();
    if(arrayCities.size()>0) {
        spinnerCities.setSelection(0);

        Log.e(tag, "mayor");
    }else {

        Log.e(tag, "menor");
    }*/
    }

/*
    //Search the cities and fill the spinner with the information
    private fun loadCities() {
        val url = "http://192.168.1.21:8080/ServerExampleUbicomp/GetCities"

        Log.e(tag, "loadcities")
        val thread = ServerConnectionThread(this, url)
        try {
            thread.join()
        } catch (e: InterruptedException) {
        }
    }

    //Search the stations of the selected city and fill the spinner with the information
    private fun loadStations(cityId: Int) {
        val url =
            "http://192.168.1.21:8080/ServerExampleUbicomp/GetStationsCity?cityId=$cityId"
        this.listStation = ArrayList()
        this.arrayStations = ArrayList()
        val thread = ServerConnectionThread(this, url)
        try {
            thread.join()
        } catch (e: InterruptedException) {
        }
    }

    //Select the Cities from JSON response
    fun setListCities(jsonCities: JSONArray) {
        try {
            for (i in 0 until jsonCities.length()) {
                val jsonobject = jsonCities.getJSONObject(i)
                listCities!!.add(
                    City(
                        jsonobject.getInt("id"),
                        jsonobject.getString("name")
                    )
                )
                arrayCities!!.add(jsonobject.getString("name"))
            }
            spinnerCities!!.adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item, arrayCities
            )
        } catch (e: Exception) {
            Log.e(tag, "Error: $e")
        }
    }

    //Select the stations from JSON response
    fun setListStations(jsonCities: JSONArray) {
        Log.e(tag, "Loading stations $jsonCities")
        try {
            for (i in 0 until jsonCities.length()) {
                val jsonobject = jsonCities.getJSONObject(i)
                listStation!!.add(
                    Station(
                        jsonobject.getInt("id"),
                        jsonobject.getString("name"),
                        jsonobject.getString("latitude").toDouble(),
                        jsonobject.getString("longitude").toDouble()
                    )
                )
                arrayStations!!.add(jsonobject.getString("name"))
                Log.e(tag, "Station " + jsonobject.getString("name"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Error: $e")
        }
    }*/
}
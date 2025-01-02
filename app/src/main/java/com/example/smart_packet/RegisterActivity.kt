package com.example.smart_packet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smart_packet.data.City
import com.example.smart_packet.data.Station

class RegisterActivity : AppCompatActivity() {
    private val tag = "Register"
    private val spinnerCities: Spinner? = null
    private val spinnerStations: Spinner? = null
    private val buttonStation: Button? = null
    var arrayCities: ArrayList<String>? = null
    private val listCities: ArrayList<City>? = null
    var arrayStations: ArrayList<String>? = null
    private var listStation: ArrayList<Station>? = null
    private val context: Context = this
    private val idStation = 0
    private val nameStation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate")
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        supportActionBar!!.hide()

        val btnR  = findViewById<Button>(R.id.btn3)
        btnR.setOnClickListener {
            val i = Intent(this@RegisterActivity, HistorialActivity::class.java)
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
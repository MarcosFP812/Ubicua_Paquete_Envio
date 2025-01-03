
package com.example.smart_packet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IniSesActivity : AppCompatActivity() {
    private val tag = "Iniciar Sesion"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(tag, "onCreate")
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_inises)
        supportActionBar!!.hide()

        //Comprobar que el usuario exite

        val btnI  = findViewById<Button>(R.id.btnI)
        btnI.setOnClickListener {
            val i = Intent(this@IniSesActivity, HistorialActivity::class.java)
            startActivity(i)
            finish()
        }

        val registrarse = findViewById<TextView>(R.id.clickableText)
        registrarse.setOnClickListener{
            val i = Intent(this@IniSesActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        val btnAtras = findViewById<ImageView>(R.id.flecha)
        btnAtras.setOnClickListener{
            val i = Intent(this@IniSesActivity, MainActivity::class.java)
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
    }


}
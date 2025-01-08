package com.example.smart_packet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        val btnR  = findViewById<Button>(R.id.btn1)
        btnR.setOnClickListener {
            val i = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }
        val btnI = findViewById<Button>(R.id.btn2)
        btnI.setOnClickListener{
            val i = Intent(this@MainActivity, IniSesActivity::class.java)
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
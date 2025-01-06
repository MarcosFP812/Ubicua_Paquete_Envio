package com.example.smart_packet
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EnvioActivity : AppCompatActivity()
    {
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

        }
    }


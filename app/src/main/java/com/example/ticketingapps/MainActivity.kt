package com.example.ticketingapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val seatsView = findViewById<SeatsView>(R.id.seatsView)
        val button = findViewById<Button>(R.id.finishButton)
        button.setOnClickListener {
            seatsView.seat?.let {
                Toast.makeText(this, "Kursi anda adalah nomor ${it.name}.", Toast.LENGTH_SHORT)
                    .show()
            } ?: run {
                Toast.makeText(this, "Silakan pilih kursi terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
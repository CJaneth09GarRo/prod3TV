package com.example.mytvcjangr

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ClimaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clima)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
        btnVolver.requestFocus()
    }
}
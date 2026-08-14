package com.example.mytvcjangr

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvHora: TextView
    private lateinit var tvFecha: TextView

    private lateinit var btnJuego: Button
    private lateinit var btnMusica: Button
    private lateinit var btnVideo: Button
    private lateinit var btnProtector: Button
    private lateinit var btnStreaming: Button
    private lateinit var btnClima: Button

    private val handler = Handler(Looper.getMainLooper())

    private val relojRunnable = object : Runnable {
        override fun run() {

            actualizarFechaHora()

            handler.postDelayed(this, 1000)
        }
    }

    private val screensaverRunnable = Runnable {
        abrirScreensaver()
    }

    private lateinit var tvFooter: TextView
    private lateinit var mainRoot: View
    private lateinit var mainHeader: View
    private lateinit var tvTitulo: TextView
    private lateinit var tvSubtitulo: TextView

    private var themeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarBotones()
        configurarAnimacionFoco()

        actualizarFechaHora()

        handler.post(relojRunnable)

        reiniciarTemporizador()
        
        tvFooter.setOnClickListener {
            cambiarTema()
        }
        tvFooter.isFocusable = true
    }

    private fun inicializarVistas() {
        mainRoot = findViewById(R.id.mainRoot)
        mainHeader = findViewById(R.id.mainHeader)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvSubtitulo = findViewById(R.id.tvSubtitulo)
        tvHora = findViewById(R.id.tvHora)
        tvFecha = findViewById(R.id.tvFecha)
        tvFooter = findViewById(R.id.tvFooter)

        btnJuego = findViewById(R.id.btnJuego)
        btnMusica = findViewById(R.id.btnMusica)
        btnVideo = findViewById(R.id.btnVideo)
        btnProtector = findViewById(R.id.btnProtector)
        btnStreaming = findViewById(R.id.btnStreaming)
        btnClima = findViewById(R.id.btnClima)
    }

    private fun cambiarTema() {
        themeIndex = (themeIndex + 1) % 3
        when (themeIndex) {
            0 -> { // Classic Light
                mainRoot.setBackgroundColor(android.graphics.Color.parseColor("#FFF8F0"))
                mainHeader.setBackgroundColor(android.graphics.Color.parseColor("#FFE4D1"))
                tvTitulo.setTextColor(android.graphics.Color.parseColor("#5D4037"))
                tvFooter.text = "Tema: Clásico (Presiona para cambiar)"
            }
            1 -> { // Dark Mode
                mainRoot.setBackgroundColor(android.graphics.Color.parseColor("#121212"))
                mainHeader.setBackgroundColor(android.graphics.Color.parseColor("#1F1F1F"))
                tvTitulo.setTextColor(android.graphics.Color.WHITE)
                tvSubtitulo.setTextColor(android.graphics.Color.LTGRAY)
                tvFooter.text = "Tema: Oscuro Elegante"
                tvFooter.setTextColor(android.graphics.Color.WHITE)
            }
            2 -> { // Cyberpunk/Gamer
                mainRoot.setBackgroundColor(android.graphics.Color.parseColor("#0D0221"))
                mainHeader.setBackgroundColor(android.graphics.Color.parseColor("#261447"))
                tvTitulo.setTextColor(android.graphics.Color.parseColor("#FF0054"))
                tvSubtitulo.setTextColor(android.graphics.Color.parseColor("#39FF14"))
                tvFooter.text = "Tema: Cyber Gamer"
                tvFooter.setTextColor(android.graphics.Color.parseColor("#39FF14"))
            }
        }
    }

    private fun configurarBotones() {

        btnJuego.setOnClickListener {

            reiniciarTemporizador()

            val intent = Intent(
                this,
                JuegoPizzeriaActivity::class.java
            )

            startActivity(intent)
        }

        btnMusica.setOnClickListener {

            reiniciarTemporizador()

            val intent = Intent(
                this,
                MusicaActivity::class.java
            )

            startActivity(intent)
        }

        btnVideo.setOnClickListener {

            reiniciarTemporizador()

            val intent = Intent(
                this,
                VideoActivity::class.java
            )

            startActivity(intent)
        }

        btnProtector.setOnClickListener {

            reiniciarTemporizador()

            abrirScreensaver()
        }

        btnStreaming.setOnClickListener {

            reiniciarTemporizador()

            val intent = Intent(
                this,
                StreamingActivity::class.java
            )

            startActivity(intent)
        }

        btnClima.setOnClickListener {

            reiniciarTemporizador()

            val intent = Intent(
                this,
                ClimaActivity::class.java
            )

            startActivity(intent)
        }
    }

    private fun configurarAnimacionFoco() {

        val botones = listOf(
            btnJuego,
            btnMusica,
            btnVideo,
            btnProtector,
            btnStreaming,
            btnClima
        )

        botones.forEach { boton ->

            boton.setOnFocusChangeListener { vista, tieneFoco ->

                if (tieneFoco) {

                    vista.animate()
                        .scaleX(1.08f)
                        .scaleY(1.08f)
                        .setDuration(150)
                        .start()

                } else {

                    vista.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start()
                }
            }
        }
    }

    private fun actualizarFechaHora() {

        val ahora = Date()

        val formatoHora =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val formatoFecha =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        tvHora.text = formatoHora.format(ahora)

        tvFecha.text = formatoFecha.format(ahora)
    }

    private fun reiniciarTemporizador() {

        handler.removeCallbacks(screensaverRunnable)

        handler.postDelayed(
            screensaverRunnable,
            10_000
        )
    }

    private fun abrirScreensaver() {

        handler.removeCallbacks(screensaverRunnable)

        val intent = Intent(
            this,
            ScreensaverActivity::class.java
        )

        startActivity(intent)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {

            reiniciarTemporizador()
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onResume() {
        super.onResume()

        reiniciarTemporizador()
        handler.post(relojRunnable)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(screensaverRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(relojRunnable)
        handler.removeCallbacks(screensaverRunnable)
    }
}
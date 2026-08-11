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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarBotones()
        configurarAnimacionFoco()

        actualizarFechaHora()

        handler.post(relojRunnable)

        reiniciarTemporizador()
    }

    private fun inicializarVistas() {

        tvHora = findViewById(R.id.tvHora)
        tvFecha = findViewById(R.id.tvFecha)

        btnJuego = findViewById(R.id.btnJuego)
        btnMusica = findViewById(R.id.btnMusica)
        btnVideo = findViewById(R.id.btnVideo)
        btnProtector = findViewById(R.id.btnProtector)
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

        /*
         * Actualmente el proyecto solamente tiene las Activities
         * solicitadas para Juego, Video y Protector.
         *
         * El botón Música queda preparado para que posteriormente
         * puedas agregar MusicActivity.
         */
        btnMusica.setOnClickListener {

            reiniciarTemporizador()

            android.widget.Toast.makeText(
                this,
                "Módulo de Música próximamente",
                android.widget.Toast.LENGTH_SHORT
            ).show()
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
    }

    private fun configurarAnimacionFoco() {

        val botones = listOf(
            btnJuego,
            btnMusica,
            btnVideo,
            btnProtector
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
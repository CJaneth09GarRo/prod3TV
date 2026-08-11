package com.example.mytvcjangr

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ScreensaverActivity : AppCompatActivity() {

    private lateinit var root: FrameLayout
    private lateinit var dvdLogo: LinearLayout
    private lateinit var tvTitle: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvCopyright: TextView

    private val handler = Handler(Looper.getMainLooper())

    private var posX = 0f
    private var posY = 0f

    private var velocidadX = 5f
    private var velocidadY = 4f

    private val animationRunnable = object : Runnable {

        override fun run() {

            moverLogo()

            actualizarHora()

            handler.postDelayed(
                this,
                16
            )
        }
    }

    private val colores = arrayOf(
        "#FF5252",
        "#FF4081",
        "#E040FB",
        "#7C4DFF",
        "#536DFE",
        "#448AFF",
        "#40C4FF",
        "#18FFFF",
        "#64FFDA",
        "#69F0AE",
        "#B2FF59",
        "#EEFF41",
        "#FFFF00",
        "#FFD740",
        "#FFAB40",
        "#FF6E40"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_screensaver)

        inicializarVistas()

        root.requestFocus()

        root.post {

            posX = 0f
            posY = 0f

            dvdLogo.translationX = posX
            dvdLogo.translationY = posY

            handler.post(animationRunnable)
        }
    }

    private fun inicializarVistas() {

        root = findViewById(R.id.screensaverRoot)

        dvdLogo = findViewById(R.id.dvdLogo)

        tvTitle = findViewById(R.id.tvScreenTitle)
        tvTime = findViewById(R.id.tvScreenTime)
        tvCopyright = findViewById(R.id.tvScreenCopyright)
    }

    private fun moverLogo() {

        posX += velocidadX
        posY += velocidadY

        val maxX =
            root.width - dvdLogo.width

        val maxY =
            root.height - dvdLogo.height

        if (posX <= 0) {

            posX = 0f

            velocidadX = -velocidadX

            cambiarColor()
        }

        if (posX >= maxX) {

            posX = maxX.toFloat()

            velocidadX = -velocidadX

            cambiarColor()
        }

        if (posY <= 0) {

            posY = 0f

            velocidadY = -velocidadY

            cambiarColor()
        }

        if (posY >= maxY) {

            posY = maxY.toFloat()

            velocidadY = -velocidadY

            cambiarColor()
        }

        dvdLogo.translationX = posX
        dvdLogo.translationY = posY
    }

    private fun cambiarColor() {

        val colorHex =
            colores[
                Random.nextInt(colores.size)
            ]

        val color =
            Color.parseColor(colorHex)

        tvTitle.setTextColor(color)
        tvTime.setTextColor(color)
        tvCopyright.setTextColor(color)
    }

    private fun actualizarHora() {

        val formato =
            SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
            )

        tvTime.text =
            formato.format(Date())
    }

    private fun esDpad(keyCode: Int): Boolean {

        return keyCode == KeyEvent.KEYCODE_DPAD_UP ||
                keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
                keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN &&
            esDpad(event.keyCode)
        ) {

            finish()

            return true
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(animationRunnable)
    }
}
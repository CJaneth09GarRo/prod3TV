package com.example.mytvcjangr

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ScreensaverActivity : AppCompatActivity() {

    private lateinit var tvClock: TextView
    private lateinit var tvDate: TextView
    private lateinit var ivScreensaver: ImageView
    private lateinit var movingBox: LinearLayout
    private val handler = Handler(Looper.getMainLooper())

    // Variables para el movimiento
    private var stepX = 4
    private var stepY = 4
    private var curX = 0f
    private var curY = 0f

    private val updateTask = object : Runnable {
        override fun run() {
            val now = Calendar.getInstance().time
            tvClock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
            tvDate.text = SimpleDateFormat("EEEE, d 'de' MMMM", Locale.getDefault()).format(now)
            handler.postDelayed(this, 60000)
        }
    }

    private val bounceTask = object : Runnable {
        override fun run() {
            val root = findViewById<View>(android.R.id.content)
            val screenWidth = root.width
            val screenHeight = root.height
            val boxWidth = movingBox.width
            val boxHeight = movingBox.height

            if (screenWidth > 0 && screenHeight > 0) {
                curX += stepX
                curY += stepY

                // Rebote en bordes
                if (curX <= 0 || curX + boxWidth >= screenWidth) {
                    stepX *= -1
                }
                if (curY <= 0 || curY + boxHeight >= screenHeight) {
                    stepY *= -1
                }

                movingBox.translationX = curX
                movingBox.translationY = curY
            }
            handler.postDelayed(this, 20) // Aproximadamente 50 FPS
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screensaver)

        tvClock = findViewById(R.id.tvClock)
        tvDate = findViewById(R.id.tvDate)
        ivScreensaver = findViewById(R.id.ivScreensaver)
        movingBox = findViewById(R.id.movingBox)

        handler.post(updateTask)
        handler.post(bounceTask)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        finish() // Cualquier tecla cierra el screensaver
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTask)
        handler.removeCallbacks(bounceTask)
    }
}
package com.example.mytvcjangr

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var videoControls: View

    private lateinit var btnPlayPause: Button
    private lateinit var btnRewind: Button
    private lateinit var btnForward: Button

    private lateinit var seekBar: SeekBar

    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView

    private val handler =
        Handler(Looper.getMainLooper())

    private val hideControlsRunnable =
        Runnable {
            ocultarControles()
        }

    private val updateProgressRunnable =
        object : Runnable {

            override fun run() {

                if (videoView.isPlaying) {

                    actualizarProgreso()
                }

                handler.postDelayed(
                    this,
                    500
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video)

        inicializarVistas()

        configurarVideo()

        configurarControles()

        mostrarControles()

        handler.post(updateProgressRunnable)
    }

    private fun inicializarVistas() {

        videoView =
            findViewById(R.id.videoView)

        videoControls =
            findViewById(R.id.videoControls)

        btnPlayPause =
            findViewById(R.id.btnPlayPause)

        btnRewind =
            findViewById(R.id.btnRewind)

        btnForward =
            findViewById(R.id.btnForward)

        seekBar =
            findViewById(R.id.videoSeekBar)

        tvCurrentTime =
            findViewById(R.id.tvCurrentTime)

        tvTotalTime =
            findViewById(R.id.tvTotalTime)
    }

    private fun configurarVideo() {

        val videoUri =
            Uri.parse(
                "android.resource://$packageName/${R.raw.video_prueba}"
            )

        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener {

            seekBar.max =
                videoView.duration

            tvTotalTime.text =
                formatearTiempo(videoView.duration)

            videoView.start()

            actualizarBotonPlay()

            mostrarControles()
        }

        videoView.setOnCompletionListener {

            videoView.seekTo(0)

            actualizarBotonPlay()

            mostrarControles()
        }
    }

    private fun configurarControles() {

        btnPlayPause.setOnClickListener {

            alternarPlayPause()

            mostrarControles()
        }

        btnRewind.setOnClickListener {

            retroceder()

            mostrarControles()
        }

        btnForward.setOnClickListener {

            adelantar()

            mostrarControles()
        }

        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if (fromUser) {

                        videoView.seekTo(progress)

                        tvCurrentTime.text =
                            formatearTiempo(progress)
                    }
                }

                override fun onStartTrackingTouch(
                    seekBar: SeekBar?
                ) {

                    mostrarControles()
                }

                override fun onStopTrackingTouch(
                    seekBar: SeekBar?
                ) {

                    mostrarControles()
                }
            }
        )
    }

    private fun alternarPlayPause() {

        if (videoView.isPlaying) {

            videoView.pause()

        } else {

            videoView.start()
        }

        actualizarBotonPlay()
    }

    private fun actualizarBotonPlay() {

        if (videoView.isPlaying) {

            btnPlayPause.text = "⏸ Pausa"

        } else {

            btnPlayPause.text = "▶ Play"
        }
    }

    private fun retroceder() {

        var nuevoTiempo =
            videoView.currentPosition - 10_000

        if (nuevoTiempo < 0) {
            nuevoTiempo = 0
        }

        videoView.seekTo(nuevoTiempo)

        actualizarProgreso()
    }

    private fun adelantar() {

        var nuevoTiempo =
            videoView.currentPosition + 10_000

        if (nuevoTiempo > videoView.duration) {

            nuevoTiempo =
                videoView.duration
        }

        videoView.seekTo(nuevoTiempo)

        actualizarProgreso()
    }

    private fun actualizarProgreso() {

        if (videoView.duration > 0) {

            seekBar.max =
                videoView.duration

            seekBar.progress =
                videoView.currentPosition

            tvCurrentTime.text =
                formatearTiempo(
                    videoView.currentPosition
                )

            tvTotalTime.text =
                formatearTiempo(
                    videoView.duration
                )
        }
    }

    private fun formatearTiempo(
        milisegundos: Int
    ): String {

        val segundosTotales =
            milisegundos / 1000

        val minutos =
            segundosTotales / 60

        val segundos =
            segundosTotales % 60

        return String.format(
            "%02d:%02d",
            minutos,
            segundos
        )
    }

    private fun mostrarControles() {

        videoControls.visibility =
            View.VISIBLE

        handler.removeCallbacks(
            hideControlsRunnable
        )

        handler.postDelayed(
            hideControlsRunnable,
            3500
        )
    }

    private fun ocultarControles() {

        videoControls.visibility =
            View.INVISIBLE
    }

    override fun dispatchKeyEvent(
        event: KeyEvent
    ): Boolean {

        if (event.action ==
            KeyEvent.ACTION_DOWN
        ) {

            when (event.keyCode) {

                KeyEvent.KEYCODE_DPAD_CENTER -> {

                    alternarPlayPause()

                    mostrarControles()

                    return true
                }

                KeyEvent.KEYCODE_DPAD_LEFT -> {

                    retroceder()

                    mostrarControles()

                    return true
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {

                    adelantar()

                    mostrarControles()

                    return true
                }

                KeyEvent.KEYCODE_DPAD_UP,
                KeyEvent.KEYCODE_DPAD_DOWN -> {

                    mostrarControles()

                    return true
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(
            hideControlsRunnable
        )

        handler.removeCallbacks(
            updateProgressRunnable
        )

        videoView.stopPlayback()
    }
}
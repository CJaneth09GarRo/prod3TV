package com.example.mytvcjangr

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MusicaActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var tvStatus: TextView
    private lateinit var tvSongTitle: TextView
    private lateinit var btnPlayPause: Button

    private val playlist = listOf(
        Song("A donde va el viento", R.raw.a_donde_va_el_viento),
        Song("Meditación Guiada", R.raw.meditacion_guiada),
        Song("The Man", R.raw.the_man)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musica)

        tvStatus = findViewById(R.id.tvStatus)
        tvSongTitle = findViewById(R.id.tvSongTitle)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        val btnPrev = findViewById<Button>(R.id.btnPrev)
        val btnNext = findViewById<Button>(R.id.btnNext)

        loadSong(currentIndex)

        btnPlayPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                btnPlayPause.text = "▶"
                tvStatus.text = "Pausado"
            } else {
                mediaPlayer?.start()
                btnPlayPause.text = "⏸"
                tvStatus.text = "Reproduciendo..."
            }
        }

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % playlist.size
            playCurrent()
        }

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else playlist.size - 1
            playCurrent()
        }

        btnPlayPause.requestFocus()
    }

    private fun loadSong(index: Int) {
        mediaPlayer?.release()
        val song = playlist[index]
        mediaPlayer = MediaPlayer.create(this, song.resId)
        tvSongTitle.text = song.title
        btnPlayPause.text = "▶"
    }

    private fun playCurrent() {
        loadSong(currentIndex)
        mediaPlayer?.start()
        btnPlayPause.text = "⏸"
        tvStatus.text = "Reproduciendo..."
    }

    data class Song(val title: String, val resId: Int)

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
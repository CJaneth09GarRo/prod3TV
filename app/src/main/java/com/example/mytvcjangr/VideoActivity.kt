package com.example.mytvcjangr

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var tvVideoTitle: TextView
    private lateinit var btnPlayPause: Button

    private val videos = listOf(
        VideoItem("Prueba de Video", R.raw.video_prueba),
        VideoItem("Películas", R.raw.movies),
        VideoItem("Entrevista Taylor", R.raw.taylor_entrevista)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.videoView)
        tvVideoTitle = findViewById(R.id.tvVideoTitle)
        btnPlayPause = findViewById(R.id.btnPlayPauseVideo)
        val btnPrev = findViewById<Button>(R.id.btnPrevVideo)
        val btnNext = findViewById<Button>(R.id.btnNextVideo)

        setupVideo(currentIndex)

        btnPlayPause.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
                btnPlayPause.text = "▶"
            } else {
                videoView.start()
                btnPlayPause.text = "⏸"
            }
        }

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % videos.size
            setupVideo(currentIndex)
            videoView.start()
            btnPlayPause.text = "⏸"
        }

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else videos.size - 1
            setupVideo(currentIndex)
            videoView.start()
            btnPlayPause.text = "⏸"
        }

        videoView.setOnCompletionListener {
            btnNext.performClick()
        }

        btnPlayPause.requestFocus()
    }

    private fun setupVideo(index: Int) {
        val video = videos[index]
        tvVideoTitle.text = video.title
        val uri = Uri.parse("android.resource://" + packageName + "/" + video.resId)
        videoView.setVideoURI(uri)
    }

    data class VideoItem(val title: String, val resId: Int)
}
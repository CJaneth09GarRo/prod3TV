package com.example.mytvcjangr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StreamingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streaming)

        val rvTrending = findViewById<RecyclerView>(R.id.rvTrending)
        val rvRecommended = findViewById<RecyclerView>(R.id.rvRecommended)

        setupRecyclerView(rvTrending, listOf("Película 1", "Serie A", "Documental 1", "Película 2", "Serie B"))
        setupRecyclerView(rvRecommended, listOf("Comedia 1", "Acción 2", "Drama 3", "Terror 4", "Sci-Fi 5"))
    }

    private fun setupRecyclerView(rv: RecyclerView, items: List<String>) {
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = PosterAdapter(items)
    }

    class PosterAdapter(private val items: List<String>) : RecyclerView.Adapter<PosterAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
            val tvTitle: TextView = view.findViewById(R.id.tvPosterTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvTitle.text = items[position]
            
            // Efecto de escala al ganar foco
            holder.itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
                } else {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                }
            }
        }

        override fun getItemCount() = items.size
    }
}
package com.example.mytvcjangr

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class JuegoPizzeriaActivity : AppCompatActivity() {

    private lateinit var btnTomate: Button
    private lateinit var btnQueso: Button
    private lateinit var btnChampinon: Button
    private lateinit var btnPeperoni: Button

    private lateinit var tvRecipe: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvPizzas: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var tvPizza: TextView

    private val handler =
        Handler(Looper.getMainLooper())

    private var puntuacion = 0
    private var pizzasHorneadas = 0

    private val ingredientes =
        listOf(
            Ingrediente(
                nombre = "Tomate",
                tecla = KeyEvent.KEYCODE_DPAD_UP,
                emoji = "🍅"
            ),

            Ingrediente(
                nombre = "Queso",
                tecla = KeyEvent.KEYCODE_DPAD_DOWN,
                emoji = "🧀"
            ),

            Ingrediente(
                nombre = "Champiñón",
                tecla = KeyEvent.KEYCODE_DPAD_LEFT,
                emoji = "🍄"
            ),

            Ingrediente(
                nombre = "Peperoni",
                tecla = KeyEvent.KEYCODE_DPAD_RIGHT,
                emoji = "🌶️"
            )
        )

    private var recetaActual =
        mutableListOf<Ingrediente>()

    private var posicionActual = 0

    private var juegoTerminado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_juego_pizzeria
        )

        inicializarVistas()

        configurarBotones()

        nuevaReceta()

        btnTomate.requestFocus()
    }

    private fun inicializarVistas() {

        btnTomate =
            findViewById(R.id.btnTomate)

        btnQueso =
            findViewById(R.id.btnQueso)

        btnChampinon =
            findViewById(R.id.btnChampinon)

        btnPeperoni =
            findViewById(R.id.btnPeperoni)

        tvRecipe =
            findViewById(R.id.tvRecipe)

        tvProgress =
            findViewById(R.id.tvProgress)

        tvScore =
            findViewById(R.id.tvScore)

        tvPizzas =
            findViewById(R.id.tvPizzas)

        tvFeedback =
            findViewById(R.id.tvFeedback)

        tvPizza =
            findViewById(R.id.tvPizza)
    }

    private fun configurarBotones() {

        btnTomate.setOnClickListener {

            comprobarIngrediente(
                KeyEvent.KEYCODE_DPAD_UP
            )
        }

        btnQueso.setOnClickListener {

            comprobarIngrediente(
                KeyEvent.KEYCODE_DPAD_DOWN
            )
        }

        btnChampinon.setOnClickListener {

            comprobarIngrediente(
                KeyEvent.KEYCODE_DPAD_LEFT
            )
        }

        btnPeperoni.setOnClickListener {

            comprobarIngrediente(
                KeyEvent.KEYCODE_DPAD_RIGHT
            )
        }

        configurarAnimaciones()
    }

    private fun configurarAnimaciones() {

        val botones = listOf(
            btnTomate,
            btnQueso,
            btnChampinon,
            btnPeperoni
        )

        botones.forEach { boton ->

            boton.setOnFocusChangeListener { vista, tieneFoco ->

                if (tieneFoco) {

                    vista.animate()
                        .scaleX(1.10f)
                        .scaleY(1.10f)
                        .setDuration(120)
                        .start()

                } else {

                    vista.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(120)
                        .start()
                }
            }
        }
    }

    private fun nuevaReceta() {

        recetaActual =
            ingredientes
                .shuffled(Random)
                .take(2)
                .toMutableList()

        posicionActual = 0

        juegoTerminado = false

        mostrarReceta()

        tvFeedback.text =
            "¡Usa las flechas y ENTER!"

        tvFeedback.setTextColor(
            Color.parseColor("#5D4037")
        )

        tvPizza.text =
            "🍕\nMASA"
    }

    private fun mostrarReceta() {

        val texto =
            recetaActual.joinToString(
                separator = " → "
            ) {

                "${it.emoji} ${it.nombre}"
            }

        tvRecipe.text = texto

        tvProgress.text =
            "Ingrediente ${posicionActual + 1} de 2"
    }

    private fun comprobarIngrediente(
        teclaPresionada: Int
    ) {

        if (juegoTerminado) {
            return
        }

        if (posicionActual >= recetaActual.size) {
            return
        }

        val ingredienteEsperado =
            recetaActual[posicionActual]

        if (
            teclaPresionada ==
            ingredienteEsperado.tecla
        ) {

            ingredienteCorrecto(
                ingredienteEsperado
            )

        } else {

            ingredienteIncorrecto()
        }
    }

    private fun ingredienteCorrecto(
        ingrediente: Ingrediente
    ) {

        tvFeedback.text =
            "✓ ¡Correcto! ${ingrediente.nombre}"

        tvFeedback.setTextColor(
            Color.parseColor("#2E7D32")
        )

        tvPizza.text =
            "🍕\n${ingrediente.emoji} ${ingrediente.nombre}"

        posicionActual++

        if (posicionActual >= 2) {

            hornearPizza()

        } else {

            tvProgress.text =
                "Ingrediente ${posicionActual + 1} de 2"
        }
    }

    private fun ingredienteIncorrecto() {

        tvFeedback.text =
            "✗ ¡Casi! Intenta de nuevo"

        tvFeedback.setTextColor(
            Color.parseColor("#C62828")
        )

        tvPizza.text =
            "🍕\n¡Opps!"

        handler.postDelayed({

            if (!isFinishing) {

                posicionActual = 0

                tvProgress.text =
                    "Ingrediente 1 de 2"

                tvFeedback.text =
                    "¡Vamos de nuevo!"

                tvFeedback.setTextColor(
                    Color.parseColor("#5D4037")
                )

                tvPizza.text =
                    "🍕\nMASA"
            }

        }, 800)
    }

    private fun hornearPizza() {

        juegoTerminado = true

        puntuacion += 100

        pizzasHorneadas++

        tvScore.text =
            "Puntos: $puntuacion"

        tvPizzas.text =
            "Pizzas: $pizzasHorneadas"

        tvProgress.text =
            "¡Excelente!"

        tvFeedback.text =
            "🎉 ¡PIZZA LISTA! +100"

        tvFeedback.setTextColor(
            Color.parseColor("#2E7D32")
        )

        tvPizza.text =
            "🍕🔥\n¡YUMMY!"

        handler.postDelayed({

            if (!isFinishing) {

                nuevaReceta()
            }

        }, 1200)
    }

    override fun dispatchKeyEvent(
        event: KeyEvent
    ): Boolean {
        // Dejamos que el sistema maneje la navegación por defecto (flechas -> foco)
        // Y el click (Enter -> onClickListener)
        return super.dispatchKeyEvent(event)
    }

    private data class Ingrediente(
        val nombre: String,
        val tecla: Int,
        val emoji: String
    )

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacksAndMessages(
            null
        )
    }
}
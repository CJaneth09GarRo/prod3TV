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
                .take(3)
                .toMutableList()

        posicionActual = 0

        juegoTerminado = false

        mostrarReceta()

        tvFeedback.text =
            "¡Prepara la pizza!"

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
            "Ingrediente ${posicionActual + 1} de 3"
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

        if (posicionActual >= 3) {

            hornearPizza()

        } else {

            tvProgress.text =
                "Ingrediente ${posicionActual + 1} de 3"
        }
    }

    private fun ingredienteIncorrecto() {

        tvFeedback.text =
            "✗ ¡Ese no es! Inténtalo de nuevo"

        tvFeedback.setTextColor(
            Color.parseColor("#C62828")
        )

        tvPizza.text =
            "🍕\n¡ERROR!"

        handler.postDelayed({

            if (!isFinishing) {

                posicionActual = 0

                tvProgress.text =
                    "Ingrediente 1 de 3"

                tvFeedback.text =
                    "La pizza se reinició"

                tvFeedback.setTextColor(
                    Color.parseColor("#5D4037")
                )

                tvPizza.text =
                    "🍕\nMASA"
            }

        }, 1200)
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
            "¡Pizza completada!"

        tvFeedback.text =
            "🎉 ¡PIZZA HORNEADA! +100 puntos"

        tvFeedback.setTextColor(
            Color.parseColor("#2E7D32")
        )

        tvPizza.text =
            "🍕🔥\n¡LISTA!"

        handler.postDelayed({

            if (!isFinishing) {

                nuevaReceta()
            }

        }, 1800)
    }

    override fun dispatchKeyEvent(
        event: KeyEvent
    ): Boolean {

        if (event.action ==
            KeyEvent.ACTION_DOWN
        ) {

            when (event.keyCode) {

                KeyEvent.KEYCODE_DPAD_UP -> {

                    comprobarIngrediente(
                        KeyEvent.KEYCODE_DPAD_UP
                    )

                    return true
                }

                KeyEvent.KEYCODE_DPAD_DOWN -> {

                    comprobarIngrediente(
                        KeyEvent.KEYCODE_DPAD_DOWN
                    )

                    return true
                }

                KeyEvent.KEYCODE_DPAD_LEFT -> {

                    comprobarIngrediente(
                        KeyEvent.KEYCODE_DPAD_LEFT
                    )

                    return true
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {

                    comprobarIngrediente(
                        KeyEvent.KEYCODE_DPAD_RIGHT
                    )

                    return true
                }
            }
        }

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
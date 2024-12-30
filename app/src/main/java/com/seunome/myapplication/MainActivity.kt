package com.seunome.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var textViewFrase: TextView
    private lateinit var buttonNovaFrase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewFrase = findViewById(R.id.textViewFrase)
        buttonNovaFrase = findViewById(R.id.buttonNovaFrase)

        // Exibe uma frase inicial
        gerarFraseMotivacional()

        // Botão para gerar uma nova frase
        buttonNovaFrase.setOnClickListener {
            gerarFraseMotivacional()
        }
    }

    private fun gerarFraseMotivacional() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val frase = chamarApiGemini()
                withContext(Dispatchers.Main) {
                    textViewFrase.text = frase
                    Toast.makeText(this@MainActivity, "Frase atualizada!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erro ao gerar frase: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun chamarApiGemini(): String {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash", // Modelo recomendado para entradas multimodais
            apiKey = BuildConfig.apiKey // Acessa a chave da API
        )

        val prompt = "Gere uma frase motivacional curta."
        val response = generativeModel.generateContent(prompt)
        return response.text ?: "Frase não gerada."
    }
}
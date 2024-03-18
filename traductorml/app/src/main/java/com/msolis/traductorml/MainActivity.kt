package com.msolis.traductorml

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var Et_Idioma_Origen : EditText
    private lateinit var Tv_Idioma_Destino : TextView
    private lateinit var Btn_Elegir_Idioma : MaterialButton
    private lateinit var Btn_Idioma_Elegido : MaterialButton
    private lateinit var Btn_Traducir : MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        InicializarVistas()

        Btn_Elegir_Idioma.setOnClickListener {
            Toast.makeText(applicationContext, "Elegir idioma", Toast.LENGTH_SHORT).show()
        }

        Btn_Idioma_Elegido.setOnClickListener {
            Toast.makeText(applicationContext, "Idioma elegido", Toast.LENGTH_SHORT).show()
        }

        Btn_Traducir.setOnClickListener {
            Toast.makeText(applicationContext, "Traducir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun InicializarVistas(){
        Et_Idioma_Origen = findViewById(R.id.Et_Idioma_Origen)
        Tv_Idioma_Destino = findViewById(R.id.Tv_Idioma_destino)
        Btn_Elegir_Idioma = findViewById(R.id.Btn_Elegir_Idioma)
        Btn_Idioma_Elegido = findViewById(R.id.Btn_Idioma_Elegido)
        Btn_Traducir = findViewById(R.id.Btn_Traducir)
    }
}
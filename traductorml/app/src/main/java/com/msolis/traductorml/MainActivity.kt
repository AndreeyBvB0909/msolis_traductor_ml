package com.msolis.traductorml

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.mlkit.nl.translate.TranslateLanguage
import com.msolis.traductorml.Modelo.Idioma
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var Et_Idioma_Origen : EditText
    private lateinit var Tv_Idioma_Destino : TextView
    private lateinit var Btn_Elegir_Idioma : MaterialButton
    private lateinit var Btn_Idioma_Elegido : MaterialButton
    private lateinit var Btn_Traducir : MaterialButton

    private var IdiomasArrayList : ArrayList<Idioma> ?= null

    companion object{
        private const val REGISTRO = "Mis_registros"
    }

    private var codigo_idioma_origen = "es"
    private var titulo_idioma_origen = "Español"

    private var codigo_idioma_destino = "en"
    private var titulo_idioma_destino = "Inglés"

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
        IdiomasDisponibles()

        Btn_Elegir_Idioma.setOnClickListener {
            //Toast.makeText(applicationContext, "Elegir idioma", Toast.LENGTH_SHORT).show()
            ElegirIdiomaOrigen()
        }

        Btn_Idioma_Elegido.setOnClickListener {
            //Toast.makeText(applicationContext, "Idioma elegido", Toast.LENGTH_SHORT).show()
            ElegirIdiomaDestino()
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

    private fun IdiomasDisponibles(){
        IdiomasArrayList = ArrayList()

        val ListaCodigoIdioma = TranslateLanguage.getAllLanguages()

        for(codigo_lenguaje in ListaCodigoIdioma){

            val titulo_lenguaje = Locale(codigo_lenguaje).displayLanguage

            //Log.d(REGISTRO, "IdiomasDisponibles : codigo_lenguaje $codigo_lenguaje")
            //Log.d(REGISTRO, "IdiomasDisponibles : titulo_lenguaje $titulo_lenguaje")

            val modeloIdioma = Idioma(codigo_lenguaje, titulo_lenguaje)

            IdiomasArrayList!!.add(modeloIdioma)

        }
    }

    private fun ElegirIdiomaOrigen(){
        val popupMenu = PopupMenu(this, Btn_Elegir_Idioma)

        for(i in IdiomasArrayList!!.indices){
            popupMenu.menu.add(Menu.NONE, i, i, IdiomasArrayList!![i].titulo_idioma)
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->

            val position = menuItem.itemId

            codigo_idioma_origen = IdiomasArrayList!![position].codigo_idioma
            titulo_idioma_origen = IdiomasArrayList!![position].titulo_idioma

            Btn_Elegir_Idioma.text = titulo_idioma_origen
            Et_Idioma_Origen.hint = "Ingrese texto en $titulo_idioma_origen"

            Log.d(REGISTRO, "ElegirIdiomaOrigen : codigo_idioma_origen $codigo_idioma_origen")
            Log.d(REGISTRO, "ElegirIdiomaOrigen : titulo_idioma_origen $titulo_idioma_origen")


            false
        }
    }

    private fun ElegirIdiomaDestino(){
        val popupMenu = PopupMenu(this, Btn_Idioma_Elegido)

        for(i in IdiomasArrayList!!.indices){
            popupMenu.menu.add(Menu.NONE, i, i, IdiomasArrayList!![i].titulo_idioma)
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->

            val position = menuItem.itemId

            codigo_idioma_destino = IdiomasArrayList!![position].codigo_idioma
            titulo_idioma_destino = IdiomasArrayList!![position].titulo_idioma

            Btn_Idioma_Elegido.text = titulo_idioma_destino

            Log.d(REGISTRO, "ElegirIdiomaDestino : codigo_idioma_destino $codigo_idioma_destino")
            Log.d(REGISTRO, "ElegirIdiomaDestino : titulo_idioma_destino $titulo_idioma_destino")


            false
        }
    }
}
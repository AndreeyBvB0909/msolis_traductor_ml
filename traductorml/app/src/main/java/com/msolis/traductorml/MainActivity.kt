package com.msolis.traductorml

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.msolis.traductorml.Modelo.Idioma
import java.util.Locale
import android.graphics.drawable.ColorDrawable
import android.graphics.Color



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

    private lateinit var translateOptions: TranslatorOptions
    private lateinit var translator: Translator
    private lateinit var progressDialog : ProgressDialog

    private var Texto_idioma_origen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InicializarVistas()
        IdiomasDisponibles()

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5898ff")))

        Btn_Elegir_Idioma.setOnClickListener {
            //Toast.makeText(applicationContext, "Elegir idioma", Toast.LENGTH_SHORT).show()
            ElegirIdiomaOrigen()
        }

        Btn_Idioma_Elegido.setOnClickListener {
            //Toast.makeText(applicationContext, "Idioma elegido", Toast.LENGTH_SHORT).show()
            ElegirIdiomaDestino()
        }

        Btn_Traducir.setOnClickListener {
            //Toast.makeText(applicationContext, "Traducir", Toast.LENGTH_SHORT).show()
            ValidarDatos()
        }
    }



    private fun InicializarVistas(){
        Et_Idioma_Origen = findViewById(R.id.Et_Idioma_Origen)
        Tv_Idioma_Destino = findViewById(R.id.Tv_Idioma_destino)
        Btn_Elegir_Idioma = findViewById(R.id.Btn_Elegir_Idioma)
        Btn_Idioma_Elegido = findViewById(R.id.Btn_Idioma_Elegido)
        Btn_Traducir = findViewById(R.id.Btn_Traducir)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
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

    private fun ValidarDatos() {
        Texto_idioma_origen = Et_Idioma_Origen.text.toString().trim()
        if(Texto_idioma_origen.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese texto", Toast.LENGTH_SHORT).show()
        }
        else
        {
            TraducirTexto()
        }
    }

    private fun TraducirTexto() {
        progressDialog.setMessage("Procesando")
        progressDialog.show()

        translateOptions = TranslatorOptions.Builder()
            .setSourceLanguage(codigo_idioma_origen)
            .setTargetLanguage(codigo_idioma_destino)
            .build()

        translator = Translation.getClient(translateOptions)

        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                Log.d(REGISTRO, "El paquete de traducción esta listo")
                progressDialog.setMessage("Traduciendo texto")

                translator.translate(Texto_idioma_origen)
                    .addOnSuccessListener { texto_traducido ->
                        Log.d(REGISTRO, "texto_traducido $texto_traducido")
                        progressDialog.dismiss()
                        Tv_Idioma_Destino.text = texto_traducido
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "$e", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "$e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun MostrarDialog(){
        val Btn_entendido : Button
        val dialog = Dialog(this@MainActivity)

        dialog.setContentView(R.layout.custom_dialog)
        Btn_entendido = dialog.findViewById(R.id.Btn_entendido)

        Btn_entendido.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_traductor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Menu_Limpiar_texto -> {
                val S_traduccion = "Traducción"
                Et_Idioma_Origen.setText("")
                Et_Idioma_Origen.hint = "Ingrese texto"
                Tv_Idioma_Destino.text = S_traduccion

                true
            }

            R.id.Menu_Informacion -> {
                MostrarDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
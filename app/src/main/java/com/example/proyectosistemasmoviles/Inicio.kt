package com.example.proyectosistemasmoviles
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.fragment_fragmentoperfil.view.*
import java.util.*

class Inicio : AppCompatActivity() {
    val reviewsList = mutableListOf<ReviewPreview>()
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        AppBarConfiguration(setOf(R.id.fragmentoinicio,R.id.fragmentobuscar,R.id.fragmentoperfil))

        generalonnav.setupWithNavController(findNavController(R.id.fragmentogeneral))

       botoninicio.setOnClickListener {
            val frag = fragmentocms()
            val args = Bundle()
            frag.setArguments(args)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentogeneral, frag)
            transaction.commit()

        }

        btnRegresar.setOnClickListener {

           val frag = fragmentoresenas()
            val args = Bundle()
            frag.setArguments(args)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentogeneral, frag)
            transaction.commit()
        }

        val  pref = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        var image = pref?.getString("Image","");
        if(!image!!.isEmpty()){
            val imageBytes = Base64.getDecoder().decode(image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imagenPerfilToolbar.setImageBitmap(imageBitmap)
        }
    }


}
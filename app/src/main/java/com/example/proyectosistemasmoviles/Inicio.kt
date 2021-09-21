package com.example.proyectosistemasmoviles
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_inicio.*

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val frajj = AppBarConfiguration(setOf(R.id.fragmentoinicio,R.id.fragmentobuscar,R.id.fragmentoperfil))
        generalonnav.setupWithNavController(findNavController(R.id.fragmentogeneral))
      /*  botonflotante.setOnClickListener {
            val frag = fragmentoresenas()
            val args = Bundle()
            frag.setArguments(args)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentogeneral, frag)
            transaction.commit()
        }*/
    }
}
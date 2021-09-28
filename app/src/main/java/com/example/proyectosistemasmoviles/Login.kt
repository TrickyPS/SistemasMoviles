package com.example.proyectosistemasmoviles
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

      Binicia.setOnClickListener {
          muestraInicio()
      }
     Baregistro.setOnClickListener {
         muestraRegistro()
     }

    }

    private fun muestraInicio() {
        val activityHome = Intent(this, Inicio::class.java)
        startActivity(activityHome)
        finish()
    }
    private fun muestraRegistro() {
        val activityRegistro = Intent(this, SignIn::class.java)
        startActivity(activityRegistro)
        finish()
    }


}
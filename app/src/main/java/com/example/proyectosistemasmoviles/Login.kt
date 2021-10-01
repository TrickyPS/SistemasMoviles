package com.example.proyectosistemasmoviles
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

      Binicia.setOnClickListener {
   iniciaSesion()
      }
     Baregistro.setOnClickListener {
         muestraRegistro()
     }
        pref = getSharedPreferences("usuario",MODE_PRIVATE);
    }

    private fun muestraInicio() {
        val activityHome = Intent(this, Inicio::class.java)
        startActivity(activityHome)
       /* finish()*/
    }
    private fun muestraRegistro() {
        val activityRegistro = Intent(this, SignIn::class.java)
        startActivity(activityRegistro)
     /*   finish()*/
    }
    private fun iniciaSesion(){
        val email: String = Campocorreo.toString()
        val password: String = Campocontraseña.toString()
        if (email.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_LONG).show()
        }else{
            val userServices: UserService = RestEngine.getRestEngine().create(UserService::class.java)
            val result: Call<Usuario> = userServices.BuscarUsuario(
                Usuario(
                    null,
                    null,
                    null,
                    email,
                    password,
                    null,
                    null,
                    null,
                    null
                )
            )
            result.enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    val item = response.body()
                    if (item != null) {

                        //TODO AQUI COLOCAR EL CODIGO

                        val id: Int = item.id.toString().toInt()
                        val nombre: String = item.nombre.toString()
                        val apellido: String = item.apellido.toString()
                        val email: String = item.email.toString()
                        val password: String = item.password.toString()

                        val editor = pref?.edit()
                        editor?.putInt("Id", id)
                        editor?.putString("Nombre", nombre)
                        editor?.putString("Apellido", apellido)
                        editor?.putString("Email", email)
                        editor?.putString("Password", password)
                        editor?.commit()
                        muestraInicio()

                    } else {
                        Toast.makeText(
                            this@Login,
                            "No se ha podido iniciar sesion",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    println(t.toString())
                }
            })

        }


    }


}
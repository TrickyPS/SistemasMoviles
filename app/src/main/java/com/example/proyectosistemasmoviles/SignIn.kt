package com.example.proyectosistemasmoviles
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.Binicia
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {
    var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Bainicia.setOnClickListener {
            muestraLogin()
        }
     Bregistro.setOnClickListener {
         AgregarUsuario()
     }
        pref = getSharedPreferences("usuario",MODE_PRIVATE);
    }

    private fun muestraLogin() {
        val activityLogin = Intent(this, Login::class.java)
        startActivity(activityLogin)
        finish()
    }
    private fun AgregarUsuario() {
        val nombre: String = CampoNombre.text.toString()
        val apellido: String = CampoApellido.text.toString()
        val email: String = CampoCorreo.text.toString()
        val password: String = CampoContraseña.text.toString()

        if (nombre.isEmpty() && apellido.isEmpty() && email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_LONG).show()
        } else {
            val userServices: UserService =
                RestEngine.getRestEngine().create(UserService::class.java)
            val result: Call<Usuario> = userServices.AgregaUsuario(
                Usuario(
                    null,
                    nombre,
                    apellido,
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
                        if(item?.status =="exists"){
                            Toast.makeText(this@SignIn, "Este correo ya esta registrado", Toast.LENGTH_LONG)
                                .show()
                        }else{
                            //TODO AQUI COLOCAR EL CODIGO

                                val id: Int= item.id.toString().toInt()
                                val nombre: String =   item.nombre.toString()
                                val apellido: String = item.apellido.toString()
                                val email: String =    item.email.toString()
                                val password: String = item.password.toString()

                            val editor = pref?.edit()
                            editor?.putInt("Id",id)
                            editor?.putString("Nombre", nombre)
                            editor?.putString("Apellido", apellido)
                            editor?.putString("Email", email)
                            editor?.putString("Password", password)

                            editor?.commit()
                        }
                    }
                    else {


                        Toast.makeText(this@SignIn, "No se ha podido registrar", Toast.LENGTH_LONG)
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


package com.example.proyectosistemasmoviles
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        video2.setOnCompletionListener(MediaPlayer.OnCompletionListener {

            video2.start() //need to make transition seamless.

        })
        var uri = Uri.parse("android.resource://$packageName/${R.raw.libro}")
        video2.setVideoURI(uri)

        video2.requestFocus()

        video2.start()

        Bainicia.setOnClickListener {
            muestraLogin()
        }
     Bregistro.setOnClickListener {
         AgregarUsuario()
     }
        pref = getSharedPreferences("usuario",MODE_PRIVATE);
    }
    override fun onResume() {
        super.onResume()
        video2.resume()
        video2.requestFocus()
        video2.start()
    }
    private fun muestraLogin() {
        val activityLogin = Intent(this, Login::class.java)
        startActivity(activityLogin)
        finish()
    }
    private fun AgregarUsuario() {
        val nombre: String = CampoApellido.text.toString()
        val apellido: String = CampoNombre.text.toString()
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
                            Toast.makeText(this@SignUp, "Este correo ya esta registrado", Toast.LENGTH_LONG)
                                .show()
                        }else{
                            //TODO AQUI COLOCAR EL CODIGO

                             var activityLogin = Intent(this@SignUp,Login::class.java)
                            activityLogin.putExtra("Registrar", "hola")
                            startActivity(activityLogin)
                        }
                    }
                    else {


                        Toast.makeText(this@SignUp, "No se ha podido registrar", Toast.LENGTH_LONG)
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


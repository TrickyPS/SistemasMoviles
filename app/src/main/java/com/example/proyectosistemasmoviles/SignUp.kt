package com.example.proyectosistemasmoviles
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import java.util.regex.Pattern

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
    fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$");
        return passwordREGEX.matcher(password).matches()
    }
    private fun AgregarUsuario() {
        val nombre: String = CampoApellido.text.toString()
        val apellido: String = CampoNombre.text.toString()
        val email: String = CampoCorreo.text.toString()
        val password: String = CampoContraseña.text.toString()
        val comprueba = isValidPasswordFormat(password)
        val o = 2
        if (comprueba == false || nombre.isEmpty() && apellido.isEmpty() && email.isEmpty() && password.isEmpty()) {
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
                            Toast.makeText(this@SignUp, "Te registraste correctamente", Toast.LENGTH_LONG)
                                .show()
                            Handler().postDelayed({
                                var activityLogin = Intent(this@SignUp,Login::class.java)
                                activityLogin.putExtra("Registrar", "hola")
                                startActivity(activityLogin)
                            }, 1200)

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


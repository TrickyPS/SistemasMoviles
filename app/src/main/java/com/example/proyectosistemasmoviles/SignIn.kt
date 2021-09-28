package com.example.proyectosistemasmoviles
import android.content.Intent
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Bainicia.setOnClickListener {
            muestraLogin()
        }
     Bregistro.setOnClickListener {
         AgregarUsuario()
     }
    }

    private fun muestraLogin() {
        val activityLogin = Intent(this, Login::class.java)
        startActivity(activityLogin)
        finish()
    }
    private fun AgregarUsuario(){
        val nombre:String = CampoNombre.text.toString()
        val apellido:String = CampoNombre.text.toString()
        val email:String = CampoApellido.text.toString()
        val password:String = CampoContraseña.text.toString()

        if(nombre.isEmpty() && apellido.isEmpty() && email.isEmpty() && password.isEmpty()){
            Toast.makeText(this,"Llene todos los campos",Toast.LENGTH_LONG).show()
        }
        else{
            val userServices: UserService = RestEngine.getRestEngine().create(UserService::class.java)
            val result: Call<Usuario> = userServices.AgregaUsuario(Usuario(null,nombre,apellido,email,password,null,null,null))

            result.enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    val item = response.body()
                    if(item != null) {

                    }else{


                     Toast.makeText(this@SignIn,"No se ha podido registrar",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    println(t.toString())
                }
            })
        }
}
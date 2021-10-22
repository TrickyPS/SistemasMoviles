package com.example.proyectosistemasmoviles

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.proyectosistemasmoviles.Modelos.Cambiar
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_fragmentocontrasena.*
import kotlinx.android.synthetic.main.fragment_fragmentocontrasena.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class fragmentocontrasena : Fragment() {

    var pref: SharedPreferences? = null
    var id : Int? = null
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
            id = pref?.getInt("Id",0)
            // Inflate the layout for this fragment
            var vista = inflater.inflate(R.layout.fragment_fragmentocontrasena, container, false)

            vista.contrasenn.setOnClickListener{

              cambiarcontra()

            }


            vista.vistacompleta

            return vista
        }

    private fun cambiarcontra(){
        val contran: String = contranueva.text.toString()
        val contrav: String = contravieja.text.toString()
        if (contran.isEmpty() && contrav.isEmpty()){
            Toast.makeText(context,"Agrega los campos requeridos", Toast.LENGTH_SHORT).show()
        }else{
            val userServices: UserService = RestEngine.getRestEngine().create(UserService::class.java)
            val result: Call<Estatus> = userServices.Cambiar(
                Cambiar(
                   contrav,contran
                ),
           id
            )
            result.enqueue(object : Callback<Estatus> {
                override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {
                    val item = response.body()
                    Toast.makeText(context,item?.estatus.toString(), Toast.LENGTH_SHORT).show()
                    if (item != null) {
                        Toast.makeText(context,item?.estatus.toString(), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context,"Cambia la contraseña esta mal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Estatus>, t: Throwable) {
                    println(t.toString())
                }
            })

        }


    }
    }


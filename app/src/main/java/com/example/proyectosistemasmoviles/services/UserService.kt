package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @Headers( "Content-Type: application/json")
    @POST("addUser")
    fun AgregaUsuario(@Body user: Usuario): Call<Usuario>
}
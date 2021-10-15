package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @Headers( "Content-Type: application/json")
    @POST("UserController.php")
    fun AgregaUsuario(@Body user: Usuario): Call<Usuario>

    @POST("Auth.php")
    fun BuscarUsuario(@Body user: Usuario): Call<Usuario>

    @PUT("UserController.php")
    fun AgregarImagen(@Body user: Usuario, @Query("id") id: Int?): Call<Usuario>

}
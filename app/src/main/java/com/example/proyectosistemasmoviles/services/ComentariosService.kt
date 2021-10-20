package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Comentarios
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Images
import retrofit2.Call
import retrofit2.http.*

interface ComentariosService {
    @POST("comentarios.php")
    fun addComentario(@Body comment: Comentarios): Call<Estatus>

    @GET("comentarios.php")
    fun getComentarios(@Query("id")id:Int): Call<List<Comentarios>>
}
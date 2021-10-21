package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Favoritos
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.Modelos.Votos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoritosService {
    @POST("favoritos.php")
    fun addFavorito(@Body favoritos: Favoritos): Call<Estatus>

    @GET("favoritos.php")
    fun getFavoritos(@Query("id_user") id_user: Int): Call<List<ReviewPreview>>
}
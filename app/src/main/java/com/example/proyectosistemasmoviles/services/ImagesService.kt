package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Images
import com.example.proyectosistemasmoviles.Modelos.Review
import retrofit2.Call
import retrofit2.http.*

interface ImagesService {
    @Headers( "Content-Type: application/json")
    @POST("images.php")
    fun saveImage(@Body images: Images): Call<Estatus>

    @Headers( "Content-Type: application/json")
    @GET("images.php")
    fun getAllImages(@Query("id")id:Int?): Call<List<Images>>
}
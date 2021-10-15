package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Reseñas {
    @POST("Auth.php")
    fun saveReview(@Body user: Review): Call<Review>


}
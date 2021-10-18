package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.Modelos.ReviewPublic
import com.example.proyectosistemasmoviles.Modelos.Usuario
import retrofit2.Call
import retrofit2.http.*

interface Reseñas {
    @POST("review.php")
    fun saveReview(@Body user: Review): Call<Review>

    @Headers( "Content-Type: application/json")
    @GET("review.php")
    fun getAllReviews(): Call<List<ReviewPreview>>

    @GET("review.php")
    fun getReviewById(@Query("id_review")id_review:Int?,@Query("id")id:Int?): Call<ReviewPublic>
}
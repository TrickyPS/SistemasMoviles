package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.*
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

    @GET("review.php")
    fun getReviewsCreatedById(@Query("id_user")id_user:Int?): Call<List<ReviewPreview>>
}
package com.example.proyectosistemasmoviles.services

import com.example.proyectosistemasmoviles.Modelos.Comentarios
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Votos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface VotoService {
    @POST("votos.php")
    fun addVoto(@Body votos: Votos): Call<Estatus>
}
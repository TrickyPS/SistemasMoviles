package com.example.proyectosistemasmoviles.services
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

import com.google.gson.Gson





class RestEngine {

    companion object {
        fun getRestEngine() : Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            //val gson = GsonBuilder()
              //  .setLenient()
              //  .create()

            val client =  OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeou
                .build()
            val retrofit =  Retrofit.Builder()
                .baseUrl("http://localhost:3000/controllers/") // tu url
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()




            return retrofit
        }
    }

}
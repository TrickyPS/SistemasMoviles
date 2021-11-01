package com.example.proyectosistemasmoviles



import android.app.Application
import com.example.proyectosistemasmoviles.SQliteDB.DataDBHelper

/* ESTE ES EL SINGLETON
* Porque? Escribir aqui la definicion del singleton*/
class DBApplication: Application() {

    companion object{
        lateinit var dataDBHelper: DataDBHelper
    }

    override fun onCreate() {
        super.onCreate()
        dataDBHelper =  DataDBHelper(applicationContext)
    }
}
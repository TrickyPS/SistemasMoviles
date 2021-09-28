package com.example.proyectosistemasmoviles



import android.app.Application
import com.example.proyectosistemasmoviles.SQliteDB.DataDBHelper


class DBApplication: Application() {

    companion object{
        lateinit var dataDBHelper: DataDBHelper
    }

    override fun onCreate() {
        super.onCreate()
        dataDBHelper =  DataDBHelper(applicationContext)
    }
}
package com.example.proyectosistemasmoviles.SQliteDB
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.lang.Exception

class DataDBHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION) {
// Se va a ejecutar una vez cada que se instale la aplicacion
    //Aqui van scripts para crear las tablas
    override fun onCreate(db: SQLiteDatabase?) {

        try {

            val createUserAvatar: String = "CREATE TABLE " + SetDB.user.TABLE_NAME + "(" +
                    SetDB.user.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.user.COL_UPLOAD + " INTEGER DEFAULT 0," +
                    SetDB.user.COL_AVATAR + " BLOB)"

            db?.execSQL(createUserAvatar)

            val createPublicationTable: String = "CREATE TABLE publication (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " titlePublication  VARCHAR(100)," +
                    "description VARCHAR(250)," +
                    "price INTEGER," +
                    "location VARCHAR(100)," +
                    "category VARCHAR(20)," +
                    "owner INTEGER ," +
                    "createdAt VARCHAR(50)," +
                    "upload INTEGER " +
                    ")"

            db?.execSQL(createPublicationTable)



            Log.e("ENTRO", "CREO TABLAS")

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
        }

    }
// Se llamara cada vez que se abra la aplicacion
    //Aqui va to do el funcionamiento para actualizar la aplcacion ( BORRAR TABLAS ETC)
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}
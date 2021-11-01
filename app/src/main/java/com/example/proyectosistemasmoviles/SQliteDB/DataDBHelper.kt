package com.example.proyectosistemasmoviles.SQliteDB
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.example.proyectosistemasmoviles.Modelos.Images
import com.example.proyectosistemasmoviles.Modelos.ImagesT
import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import java.io.ByteArrayOutputStream
import java.lang.Exception

class DataDBHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION) {
    // Se va a ejecutar una vez cada que se instale la aplicacion
    //Aqui van scripts para crear las tablas
    override fun onCreate(db: SQLiteDatabase?) {

        try {
            val CrearPublicacion: String = "CREATE TABLE publicacion (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " titulo  VARCHAR(100)," +
                    "subtitulo VARCHAR(250)," +
                    "contenido VARCHAR(250)," +
                    "id_user INTEGER," +
                    "subido INTEGER" +
                    ")"
            val PublicacionFoto: String = "CREATE TABLE Foto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "imagen BLOB, " +
                    " id_review  INTEGER" +
                    ")"
            val Publicacioness: String = "CREATE TABLE publicaciones (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " titulo  VARCHAR(100)," +
                    "subtitulo VARCHAR(250)," +
                    "contenido VARCHAR(250)," +
                    "imagen BLOB"+
                    ")"
            val ImagenesT: String = "CREATE TABLE imagenest (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "image BLOB"+
                    ")"

            db?.execSQL(CrearPublicacion)
            db?.execSQL(PublicacionFoto)
            db?.execSQL(Publicacioness)
            db?.execSQL(ImagenesT)
            Log.e("ENTRO", "CREO TABLAS")

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
        }

    }

    // Se llamara cada vez que se abra la aplicacion
    //Aqui va to do el funcionamiento para actualizar la aplicacion ( BORRAR TABLAS ETC)
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    public fun insertPublication(post: Review): Boolean {
        //upload = 1 if is download from api
        //upload = 0 if is not upload to api
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put("titulo", post.titulo)
        values.put("subtitulo", post.subtitulo)
        values.put("contenido", post.contenido)
        values.put("id_user", post.id_user)
        values.put("subido", 0)


        try {
            val result = dataBase.insert("publicacion", null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Su publicacion se subira cuando haya internet", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }



        return boolResult
    }

    @SuppressLint("Range")
    public fun ObtenerUltimoid(): Int? {
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id")

        var id: Int? = null

        val data = dataBase.query(
            "publicacion",
            columns,
            null,
            null,
            null,
            null,
            "id DESC", "1"
        )

        if (data.moveToFirst()) {

            id = data.getInt(data.getColumnIndex("id"))

        }


        return id
    }

    public fun InsertarFoto(id: Int, imagen: ByteArray): Boolean {

        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put("id_review", id)
        values.put("imagen", imagen)

        try {
            val result = dataBase.insert("Foto", null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }
    public fun InsertaIMG( imagen: ByteArray): Boolean {

        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put("image", imagen)

        try {
            val result = dataBase.insert("imagenest", null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }
    public fun truncarimagenes():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM imagenest")
        }catch(e:Exception){
            boolResult=false
            print(e)
        }
        return boolResult
    }
    @SuppressLint("Range")
    public fun traerimagenes():MutableList<ByteArray>{
        val List: MutableList<ByteArray> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("image")


        val data = dataBase.query("imagenest",
            columns,
            null,
            null,
            null,
            null,
            "id DESC"
        )

        if (data.moveToFirst()) {

            do {

                val img: ByteArray = data.getBlob(data.getColumnIndex("image"))



                List.add(img)
            } while (data.moveToNext())

            data.close()

        }
        return List

    }
    @SuppressLint("Range")
    public fun Publicacionesnocargadas(): MutableList<Review> {
        val List: MutableList<Review> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase


        val columns: Array<String> = arrayOf("id", "titulo", "subtitulo", "contenido", "id_user")


        val data = dataBase.query(
            "publicacion",
            columns,
            " subido = 0",
            null,
            null,
            null,
            null
        )

        if (data.moveToFirst()) {

            do {
                val publicar: Review = Review()
                publicar?.id = data.getInt(data.getColumnIndex("id"))
                publicar?.titulo = data.getString(data.getColumnIndex("titulo"))
                publicar?.subtitulo = data.getString(data.getColumnIndex("subtitulo"))
                publicar?.contenido = data.getString(data.getColumnIndex("contenido"))
                publicar?.id_user = data.getInt(data.getColumnIndex("id_user"))

                List.add(publicar)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    public fun publicacioncargada():Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put("subido",1)


        try{

            val result =  dataBase.update("publicacion",
                values,
                "subido = 0",
                null)

            if (result != -1 ) {
                print("success")
            }
            else {
                print("error")

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }
    @SuppressLint("Range")
    public fun fotoscargadas(publicationId: Int): MutableList<ByteArray> {
        val List: MutableList<ByteArray> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("imagen")


        val data = dataBase.query("foto",
            columns,
            " id = $publicationId",
            null,
            null,
            null,
            null)

        if (data.moveToFirst()) {

            do {

                val img: ByteArray = data.getBlob(data.getColumnIndex("imagen"))



                List.add(img)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    public fun truncarpublicacion():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM publicaciones")
        }catch(e:Exception){
            boolResult=false
            print(e)
        }
        return boolResult
    }

    public fun insertPublicationPreview(post: ReviewPreview):Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true
      /*  val baos = ByteArrayOutputStream()
        post.imagePublication?.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val baos2 = ByteArrayOutputStream()
        post.imageOwner?.compress(Bitmap.CompressFormat.JPEG, 25, baos2) */

        values.put("titulo", post.titulo)
        values.put("subtitulo", post.subtitulo)
        values.put("contenido", post.contenido)
        values.put("imagen", post.image)



        try {
            val result = dataBase.insert("publicaciones", null, values)

            if (result == (0).toLong()) {
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }
    @SuppressLint("Range")
    public fun getPublicationPreview():MutableList<ReviewPreview>{
        val List: MutableList<ReviewPreview> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titulo", "subtitulo", "contenido", "imagen")


        val data = dataBase.query("publicaciones",
            columns,
            null,
            null,
            null,
            null,
            "id DESC","6"
        )

        if (data.moveToFirst()) {

            do {
                val post: ReviewPreview = ReviewPreview()
                post?.id = data.getInt(data.getColumnIndex("id"))
                post?.titulo = data.getString(data.getColumnIndex("titulo"))
                post?.subtitulo = data.getString(data.getColumnIndex("subtitulo"))
                post?.contenido = data.getString(data.getColumnIndex("contenido"))
                post?.image = data.getString(data.getColumnIndex("imagen"))

                List.add(post)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    @SuppressLint("Range")
    public fun verpublicacion(id:Int):ReviewPreview{

        val post: ReviewPreview = ReviewPreview()
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titulo", "subtitulo", "contenido", "imagen")


        val data = dataBase.query("publicaciones",
            columns,
            "id = $id",
            null,
            null,
            null,
            "id DESC","1"
        )

        if (data.moveToFirst()) {



            post?.id = data.getInt(data.getColumnIndex("id"))
            post?.titulo = data.getString(data.getColumnIndex("titulo"))
            post?.subtitulo = data.getString(data.getColumnIndex("subtitulo"))
            post?.contenido = data.getString(data.getColumnIndex("contenido"))
            post?.image = data.getString(data.getColumnIndex("imagen"))





            data.close()

        }
        return post
    }
}
package com.example.proyectosistemasmoviles
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.proyectosistemasmoviles.DBApplication.Companion.dataDBHelper
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Images
import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.services.ImagesService
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.fragment_fragmentoperfil.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Inicio : AppCompatActivity() {
    val reviewsList = mutableListOf<ReviewPreview>()
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        AppBarConfiguration(setOf(R.id.fragmentoinicio,R.id.fragmentobuscar,R.id.fragmentoperfil))

        generalonnav.setupWithNavController(findNavController(R.id.fragmentogeneral))

       botoninicio.setOnClickListener {
            val frag = fragmentocms()
            val args = Bundle()
            frag.setArguments(args)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentogeneral, frag)
            transaction.commit()

        }

        val  pref = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        var nombre = pref?.getString("Nombre","");
        var apellido = pref?.getString("Apellido","");
        nombrett.setText(apellido +" "+ nombre)
        var image = pref?.getString("Image","");
        if(!image!!.isEmpty()){
            val imageBytes = Base64.getDecoder().decode(image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imagenPerfilToolbar.setImageBitmap(imageBitmap)
        }
        var List: MutableList<Review> = ArrayList()
        List = dataDBHelper.Publicacionesnocargadas()

        if(List.size>0) {
            for (Review in List) {
            var ListImagenes = Review.id?.let { dataDBHelper.fotoscargadas(it) }
                ListImagenes?.let { subirreseña(Review, it) }
            }
        }
    }


    private fun saveImageReview(idReview: Int, img: ByteArray) {

        val encodedString:String =  Base64.getEncoder().encodeToString(img)
        var images : Images = Images(
            null,
            encodedString,
            idReview
        )
        val imagesService : ImagesService = RestEngine.getRestEngine().create(ImagesService::class.java)

        val result: Call<Estatus> = imagesService.saveImage(images)

        result.enqueue(object : Callback<Estatus> {
            override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {
                var resp = response.body()
                if(resp!= null){
                    println(resp.toString())

                }
            }

            override fun onFailure(call: Call<Estatus>, t: Throwable) {
                println(t.toString())
            }

        })
    }


    private fun subirreseña(review: Review,imageList : MutableList<ByteArray>){

            val resena: Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)
            val result: Call<Review> = resena.saveReview(
               review
            )
            result.enqueue(object : Callback<Review> {
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    var resp = response.body()
                    if(resp!= null){
                        var ide = resp.id;
                        for( image in imageList ){
                            saveImageReview(ide!!,image)
                        }


                    }
                    dataDBHelper.publicacioncargada()
                }

                override fun onFailure(call: Call<Review>, t: Throwable) {


                }

            })

        }

    }

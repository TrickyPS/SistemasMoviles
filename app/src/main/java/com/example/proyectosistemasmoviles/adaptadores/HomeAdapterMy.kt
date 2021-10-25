package com.example.proyectosistemasmoviles.adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import kotlinx.android.synthetic.main.resenasm.view.*
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectosistemasmoviles.*
import com.example.proyectosistemasmoviles.Modelos.Modificar
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.Modelos.elimina
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeAdapterMy(private val context: Context, private val reviewsList: List<ReviewPreview>): RecyclerView.Adapter<HomeAdapterMy.HomeViewHolder>() {
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.resenasm, parent, false)
        return  HomeAdapterMy.HomeViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var temp: Int?
        var view = holder.itemView
        //val stream = reviewsList[position].image
        //val strImage:String =  reviewsList[0].image!!.replace("data:image/png;base64,","")
        val imgEncode = Base64.getDecoder().decode( reviewsList[position].image)
        val imageBitmap:Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode.size)
        view.imgReview.setImageBitmap(imageBitmap)
        view.tituloReview.text = reviewsList[position].titulo
        view.premisaReview.text = reviewsList[position].subtitulo
        temp = reviewsList[position].id
        view.cardHome.setOnClickListener{
            val activity = this.context as AppCompatActivity
            val frag = fragmentoresenas()
            val args = Bundle()
            args.putInt("id", reviewsList[position].id!!);
            frag.setArguments(args)
            //val transaction = activity.supportFragmentManager.beginTransaction()
           // transaction.replace(R.id.fragmentogeneral, frag)
           // transaction.commit()
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit()
        }
        view.eliminar.setOnClickListener {

            Eliminar(temp)
            Toast.makeText(context, "ola amigos", Toast.LENGTH_SHORT).show()


        }

        view.Modificar.setOnClickListener {
            val activity = this.context as AppCompatActivity
            val frag =  fragmentocms2()
            val args = Bundle()
            args.putInt("id", reviewsList[position].id!!);
            args.putString("titulo", reviewsList[position].titulo!!);
            args.putString("resena", reviewsList[position].subtitulo!!);
            args.putString("contenido", reviewsList[position].contenido!!);
            frag.setArguments(args)
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit()

        }
        //holder.itemView.textolista.text = "Recurso " + (position + 1) + " Listo"
       // val bmp = BitmapFactory.decodeByteArray(imageList[position], 0, imageList[position].size)
       // val image: ImageView = holder.itemView.imagenlista as ImageView
       // image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }
    private fun Eliminar( palabra : Int?){
      val reviewid: Int? = palabra
        if (reviewid!! < 0){
            Toast.makeText(context, "no hay ", Toast.LENGTH_LONG).show()
        }else{
            val userServices: Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)
            val result: Call<elimina> = userServices.Eliminar(
                reviewid
            )
            result.enqueue(object : Callback<elimina> {
                override fun onResponse(call: Call<elimina>, response: Response<elimina>) {
                    val item = response.body()
                    if (item != null) {
                        val activity = context as AppCompatActivity
                        val frag = fragment_mis_resenas()
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();


                    } else {
                        Toast.makeText(context, "error al eliminar ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<elimina>, t: Throwable) {
                    println(t.toString())
                }
            })

        }


    }

   /* private fun Modificar( palabra : Int?){
        val reviewid: Int? = palabra
        if (reviewid!! < 0){
            Toast.makeText(context, "no hay ", Toast.LENGTH_LONG).show()
        }else{
            val userServices: Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)
            val result: Call<Modificar> = userServices.Modificar(

            )
            result.enqueue(object : Callback<Modificar> {
                override fun onResponse(call: Call<Modificar>, response: Response<Modificar>) {
                    val item = response.body()
                    if (item != null) {



                    } else {
                        Toast.makeText(context, "error al Modificar ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Modificar>, t: Throwable) {
                    println(t.toString())
                }
            })

        }


    }*/
}

package com.example.proyectosistemasmoviles.adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.R
import com.example.proyectosistemasmoviles.fragmentoresenas
import kotlinx.android.synthetic.main.item_inicio.view.*
import java.util.*
import androidx.appcompat.app.AppCompatActivity




class HomeAdapter(private val context: Context, private val reviewsList: List<ReviewPreview>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_inicio, parent, false)
        return  HomeAdapter.HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var view = holder.itemView
        //val stream = reviewsList[position].image
        //val strImage:String =  reviewsList[0].image!!.replace("data:image/png;base64,","")
        val imgEncode = Base64.getDecoder().decode( reviewsList[position].image)
        val imageBitmap:Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode.size)
        view.imgReview.setImageBitmap(imageBitmap)
        view.tituloReview.text = reviewsList[position].titulo
        view.premisaReview.text = reviewsList[position].subtitulo
        view.resenaReview.text = reviewsList[position].contenido

        view.cardHome.setOnClickListener{
            val activity = this.context as AppCompatActivity
            val frag = fragmentoresenas()
            val args = Bundle()
            args.putInt("id", reviewsList[position].id!!);
            frag.setArguments(args)
            //val transaction = activity.supportFragmentManager.beginTransaction()
           // transaction.replace(R.id.fragmentogeneral, frag)
           // transaction.commit()
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();
        }

        //holder.itemView.textolista.text = "Recurso " + (position + 1) + " Listo"
       // val bmp = BitmapFactory.decodeByteArray(imageList[position], 0, imageList[position].size)
       // val image: ImageView = holder.itemView.imagenlista as ImageView
       // image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }


}
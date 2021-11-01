package com.example.proyectosistemasmoviles.adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.Modelos.buscarrM
import com.example.proyectosistemasmoviles.R
import com.example.proyectosistemasmoviles.fragmentoresenas
import kotlinx.android.synthetic.main.item_data.view.*
import kotlinx.android.synthetic.main.item_data2.view.*
import kotlinx.android.synthetic.main.item_inicio.view.*
import kotlinx.android.synthetic.main.item_list_cms.view.*
import java.util.*


class buscadorr(private val context: Context, private val reviewsList: List<buscarrM>): RecyclerView.Adapter<buscadorr.ImagesViewHolder>() {
    class ImagesViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_data2, parent, false)
        return  buscadorr.ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
       val view = holder.itemView
        val item = reviewsList[position]
        view.tituloresena.text = item.titulo
        view.nombrep.text = item.apellido+" "+item.nombre
        view.fecha.text = item.created_at

        if(item.image != null){
            val imgEncode = Base64.getDecoder().decode( item.image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode.size)
            view.img.setImageBitmap(imageBitmap)
        }
        view.cardbuscar.setOnClickListener{
            val activity = this.context as AppCompatActivity
            val frag = fragmentoresenas()
            val args = Bundle()
            args.putInt("id", reviewsList[position].id!!);
            frag.setArguments(args)
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();
        }

    /*    image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))*/
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }


}
package com.example.proyectosistemasmoviles.adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.Modelos.buscarrM
import com.example.proyectosistemasmoviles.R
import kotlinx.android.synthetic.main.item_data.view.*
import kotlinx.android.synthetic.main.item_data2.view.*
import kotlinx.android.synthetic.main.item_inicio.view.*
import kotlinx.android.synthetic.main.item_list_cms.view.*
import java.util.*


class buscadorr(private val context: Context, private val imageList: List<buscarrM>): RecyclerView.Adapter<buscadorr.ImagesViewHolder>() {
    class ImagesViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_data2, parent, false)
        return  buscadorr.ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
       val view = holder.itemView
        val item = imageList[position]
        view.tituloresena.text = item.titulo
        view.nombrep.text = item.apellido+" "+item.nombre

        if(item.image != null){
            val imgEncode = Base64.getDecoder().decode( item.image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode.size)
            view.img.setImageBitmap(imageBitmap)
        }


    /*    image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))*/
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}
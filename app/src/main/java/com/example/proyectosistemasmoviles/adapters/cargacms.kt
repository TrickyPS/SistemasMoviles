package com.example.proyectosistemasmoviles.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.R
import kotlinx.android.synthetic.main.item_list_cms.view.*


class cargacms(private val context: Context, private val imageList: List<ByteArray>): RecyclerView.Adapter<cargacms.ImagesViewHolder>() {
    class ImagesViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_list_cms, parent, false)
        return  cargacms.ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.itemView.textolista.text = "Recurso " + (position + 1) + " Listo"
        val bmp = BitmapFactory.decodeByteArray(imageList[position], 0, imageList[position].size)
        val image: ImageView = holder.itemView.imagenlista as ImageView
        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}
package com.example.proyectosistemasmoviles.adaptadores

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosistemasmoviles.Modelos.Comentarios
import com.example.proyectosistemasmoviles.R
import kotlinx.android.synthetic.main.item_data.view.*
import kotlinx.android.synthetic.main.item_inicio.view.*
import java.util.*

class ComentariosAdapter(private val context: Context, private val comentariosList: List<Comentarios>): RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder>() {
    class ComentariosViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_data, parent, false)
        return  ComentariosAdapter.ComentariosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComentariosViewHolder, position: Int) {
        val vista = holder.itemView
        val item = comentariosList[position]
        vista.txt_comentario.text = item.comment
        vista.fechaComentario.text = item.created_at
        vista.txt_usuario.text = item.nombre + " " + item.apellido
        if(item.image != null){
            val imgEncode = Base64.getDecoder().decode( item.image)
            val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode.size)
            vista.imgComentario.setImageBitmap(imageBitmap)
        }

    }

    override fun getItemCount(): Int {
        return comentariosList.size
    }


}
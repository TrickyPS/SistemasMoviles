package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class fragmentoresenas : Fragment() {
    var sampleImages = intArrayOf(
        R.drawable.fondo,R.drawable.fondo,R.drawable.fondo,R.drawable.fondo
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

     /*  carouselView.pageCount = sampleImages.size
    carouselView.setImageListener { position, imageView ->
        imageView.setImageResource(position)
    } */



        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoresenas, container, false)








        return vista
    }


}





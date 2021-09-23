package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*


class fragmentoresenas : Fragment() {
    var sampleImages = intArrayOf(
        R.drawable.fotopasillo,R.drawable.fotopasillo,R.drawable.fotopasillo,R.drawable.fotopasillo
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoresenas, container, false)


        vista.carouselView.pageCount = sampleImages.size

        vista.carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
        }






        return vista
    }


}





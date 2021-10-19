package com.example.proyectosistemasmoviles

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.adapters.HomeAdapter
import com.example.proyectosistemasmoviles.adapters.cargacms
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.fragment_cms.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.FragmentActivity

import android.app.Activity
import android.opengl.Visibility
import android.view.ContextMenu
import com.example.proyectosistemasmoviles.Modelos.test
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.*
import java.io.Serializable


class fragmentoinicio : Fragment() {
    var reviewsList = mutableListOf<ReviewPreview>()
    private lateinit var homeAdapter: HomeAdapter
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            var vista = inflater.inflate(R.layout.fragment_fragmentoinicio, container, false)
            homeAdapter = HomeAdapter(vista.context,reviewsList)
            vista.rvHome.adapter = homeAdapter

            getAllPreviews()




            return vista
        }



    private fun getAllPreviews() {
        val resenasService : Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)

        val result: Call<List<ReviewPreview>> = resenasService.getAllReviews()

        result.enqueue(object : Callback<List<ReviewPreview>> {
            override fun onResponse(call: Call<List<ReviewPreview>>, response: Response<List<ReviewPreview>>) {
                var resp = response.body()
                if(resp!= null){

               // for(review in resp){
              //     reviewsList.add(review)

               //  }
                //   homeAdapter.notifyDataSetChanged()

                }
                progressBarInicio.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<ReviewPreview>>, t: Throwable) {
                println(t.toString())
            //    getAllPreviews()
            }

        })
    }



}
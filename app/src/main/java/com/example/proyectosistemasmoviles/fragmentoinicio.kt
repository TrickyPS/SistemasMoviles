package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.adaptadores.HomeAdapter
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlinx.android.synthetic.main.fragment_fragmentoinicio.*


class fragmentoinicio : Fragment() {
    var reviewsList = mutableListOf<ReviewPreview>()
    private lateinit var homeAdapter: HomeAdapter
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            reviewsList.clear()

            // Inflate the layout for this fragment
            var vista = inflater.inflate(R.layout.fragment_fragmentoinicio, container, false)
            homeAdapter = HomeAdapter(vista.context,reviewsList)
            vista.rvHome.adapter = homeAdapter

            getAllPreviews()

            vista.refreshInicio.setColorSchemeResources(R.color.black);
            vista.refreshInicio.setProgressBackgroundColorSchemeResource(R.color.white);
            vista.refreshInicio.setOnRefreshListener {
                reviewsList.clear()

            getAllPreviews()
            }


            return vista
        }



    private fun getAllPreviews() {

        val resenasService : Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)

        val result: Call<List<ReviewPreview>> = resenasService.getAllReviews()

        result.enqueue(object : Callback<List<ReviewPreview>> {
            override fun onResponse(call: Call<List<ReviewPreview>>, response: Response<List<ReviewPreview>>) {
                var resp = response.body()
                if(resp!= null){

                for(review in resp){
                   reviewsList.add(review)

                 }
                  homeAdapter.notifyDataSetChanged()

                }
                progressBarInicio.visibility = View.GONE
                refreshInicio.isRefreshing = false
            }

            override fun onFailure(call: Call<List<ReviewPreview>>, t: Throwable) {
                println(t.toString())
            //    getAllPreviews()
            }

        })
    }



}
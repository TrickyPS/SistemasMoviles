package com.example.proyectosistemasmoviles

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.adaptadores.HomeAdapter
import com.example.proyectosistemasmoviles.services.FavoritosService
import com.example.proyectosistemasmoviles.services.RestEngine
import kotlinx.android.synthetic.main.fragment_fragmentofavoritos.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class fragmentofavoritos : Fragment() {
    var reviewsList = mutableListOf<ReviewPreview>()
    private lateinit var homeAdapter: HomeAdapter
    var pref: SharedPreferences? = null
    var id : Int? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            var vista = inflater.inflate(R.layout.fragment_fragmentofavoritos, container, false)

            homeAdapter = HomeAdapter(vista.context,reviewsList)
            vista.rvFavoritos.adapter = homeAdapter

            pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
            id = pref?.getInt("Id",0)
            getAllPreviews()

            return vista
        }

    private fun getAllPreviews() {
        val favoritosService : FavoritosService = RestEngine.getRestEngine().create(FavoritosService::class.java)

        val result: Call<List<ReviewPreview>> = favoritosService.getFavoritos(id!!)

        result.enqueue(object : Callback<List<ReviewPreview>> {
            override fun onResponse(call: Call<List<ReviewPreview>>, response: Response<List<ReviewPreview>>) {
                var resp = response.body()
                if(resp!= null){

                    for(review in resp){
                        reviewsList.add(review)

                    }
                    homeAdapter.notifyDataSetChanged()

                }


            }

            override fun onFailure(call: Call<List<ReviewPreview>>, t: Throwable) {
                println(t.toString())
            }

        })
    }

}
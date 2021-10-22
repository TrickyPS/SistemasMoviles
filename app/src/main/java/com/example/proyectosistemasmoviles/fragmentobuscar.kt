package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectosistemasmoviles.Modelos.ReviewPreview
import com.example.proyectosistemasmoviles.Modelos.buscarrM
import com.example.proyectosistemasmoviles.adaptadores.HomeAdapter
import com.example.proyectosistemasmoviles.adaptadores.buscadorr
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import kotlinx.android.synthetic.main.fragment_fragmentobuscar.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.*
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.view.rvHome
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class fragmentobuscar : Fragment() {
    var reviewsList = mutableListOf<buscarrM>()
    private lateinit var buscaradapter: buscadorr

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentobuscar, container, false)

        buscaradapter = buscadorr(vista.context,reviewsList)
        vista.recyclerView.adapter = buscaradapter
        llamar()
        vista.vistacompleta
        return vista
    }

    private fun llamar() {
        val resenasService : Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)

        val result: Call<List<buscarrM>> = resenasService.Buscar()
        result.enqueue(object : Callback<List<buscarrM>> {
            override fun onResponse(call: Call<List<buscarrM>>, response: Response<List<buscarrM>>) {
                var resp = response.body()
                if(resp!= null){

                    for(review in resp){
                        reviewsList.add(review)

                    }
                    buscaradapter.notifyDataSetChanged()

                }
            /*    progressBarInicio.visibility = View.GONE*/
               /* refreshInicio.isRefreshing = false */
            }

            override fun onFailure(call: Call<List<buscarrM>>, t: Throwable) {
                println(t.toString())
                //    getAllPreviews()
            }

        })
    }

}
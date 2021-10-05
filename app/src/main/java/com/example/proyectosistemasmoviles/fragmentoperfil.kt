package com.example.proyectosistemasmoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentoperfil.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import android.content.SharedPreferences
import android.os.Build

import kotlinx.android.synthetic.main.fragment_fragmentoperfil.view.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import java.security.AllPermission


class fragmentoperfil : Fragment() {
    var pref: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoperfil, container, false)

        vista.cierraSesion.setOnClickListener {
cierras()
        }
        vista.botoncam.setOnClickListener {


        }
        return vista

    }

    private fun cierras(){
        val  pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        if (pref != null) {
            pref.edit().clear().commit()

            activity?.let{
                val intent = Intent (it, Login::class.java)
                it.startActivity(intent)
            }

        }



    }

    private fun changeImage() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var boolDo:Boolean =  false
            if (checkSelfPermission(this.requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, 1001)
            }
            else{
                //permission already granted
                boolDo =  true

            }


            if(boolDo == true){
                pickImageFromGallery()
            }

        }

    }
    private fun pickImageFromGallery() {
        //Abrir la galería
        val intent  =  Intent()
        intent.setAction(Intent.ACTION_PICK);
        intent.type = "image/*"
        startActivityForResult(intent, 1000)

    }

}


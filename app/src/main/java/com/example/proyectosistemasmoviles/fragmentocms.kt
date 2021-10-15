package com.example.proyectosistemasmoviles

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.adapters.cargacms
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.fragment_cms.*
import kotlinx.android.synthetic.main.fragment_cms.view.*
import kotlinx.android.synthetic.main.fragment_fragmentoperfil.*
import retrofit2.Call
import java.io.ByteArrayOutputStream

class fragmentocms : Fragment() {
    var pref: SharedPreferences? = null
    var id : Int? = null
    val imageList = mutableListOf<ByteArray>()
private lateinit var cargacms: cargacms
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        id = pref?.getInt("Id",0)
        var vista = inflater.inflate(R.layout.fragment_cms, container, false)
        cargacms = cargacms(vista.context,imageList)
        vista.reciclaim.adapter = cargacms
       vista.botoni.setOnClickListener{

       changeImage()

       }

        vista.botonsubir.setOnClickListener {


        }
        return vista
    }
    private fun subirreseña(){
        val titulo: String = titulop.text.toString()
        val premisa: String = premisap.text.toString()
        val descripcion: String= resenap.text.toString()

        if(titulo.isEmpty() || premisa.isEmpty() || descripcion.isEmpty()){
            Toast.makeText(this.context, "Llene todos los campos", Toast.LENGTH_LONG).show()
        }else{
            val resena: Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)
            val result: Call<Review> = resena.saveReview(
                Review(
               null,titulo,premisa,descripcion,id,null,null
                )
            )
        }

    }

    private fun changeImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var boolDo: Boolean = false
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, 1001)
                val ro = 2
            } else {
                //permission already granted
                boolDo = true

            }


            if (boolDo == true) {
                pickImageFromGallery()
            }

        }

    }

    private fun pickImageFromGallery() {
        //Abrir la galería
        val intent = Intent()
        intent.setAction(Intent.ACTION_PICK);
        intent.type = "image/*"
        startActivityForResult(intent, 1000)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1001 -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //PERMISO DENEGADO
                    /* Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()*/
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, requireActivity().intent)

        if (resultcode == Activity.RESULT_OK) {

            if (requestcode == 1000) {

                botoni.setImageURI(data?.data)

                val bitmaps = (botoni.getDrawable() as BitmapDrawable).bitmap

                val comprime = ByteArrayOutputStream()

                bitmaps.compress(Bitmap.CompressFormat.JPEG, 25, comprime)

                /*  dataDBHelper.insertAvatar(baos.toByteArray()) */
                var imdf: ByteArray = comprime.toByteArray()
                imageList.add(imdf)
                cargacms.notifyDataSetChanged()
            }
        }
    }
}
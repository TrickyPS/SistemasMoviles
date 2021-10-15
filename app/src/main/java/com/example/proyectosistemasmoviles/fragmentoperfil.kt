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
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import com.airbnb.lottie.LottieAnimationView
import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_cms.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.security.AllPermission
import java.util.*


class fragmentoperfil : Fragment() {
    var pref: SharedPreferences? = null
    var id : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val  pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
          id = pref?.getInt("Id",0)

        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoperfil, container, false)

        vista.cierraSesion.setOnClickListener {
cierras()
        }
        var like2 = false
        vista.botoncam.setOnClickListener {
            like2 = heartanimation2(fotito,R.raw.camarita,like2)

            Handler().postDelayed({
                changeImage()
            }, 3000)


        }
        return vista

    }
    private fun heartanimation2(imageView: LottieAnimationView,
                                animation: Int,
                                like: Boolean) : Boolean {

        if (!like) {
            imageView.setAnimation(animation)
            imageView.playAnimation()

        } else {
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }

        return !like
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
                val ro = 2
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1001 -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //PERMISO DENEGADO
                   /* Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()*/
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode,requireActivity().intent)

        if (resultcode == Activity.RESULT_OK) {

            if(requestcode == 1000) {

                imagenperfil.setImageURI(data?.data)

              val bitmaps = (imagenperfil.getDrawable() as BitmapDrawable).bitmap

                val comprime = ByteArrayOutputStream()

                bitmaps.compress(Bitmap.CompressFormat.JPEG, 25, comprime)

               /*  dataDBHelper.insertAvatar(baos.toByteArray()) */
               var imdf: ByteArray = comprime.toByteArray()
                saveImageUser(imdf)
            }
        }
    }

    private fun saveImageUser(imgArray: ByteArray) {

        val idu : Int? = id
        val encodedString:String =  Base64.getEncoder().encodeToString(imgArray)
        var uu : Usuario = Usuario(
            null,
            null,
            null,
            null,
            null,
            encodedString,
            null,
            null,
            null
        )
        val userService : UserService = RestEngine.getRestEngine().create(UserService::class.java)

        val result: Call<Usuario> = userService.AgregarImagen(uu,idu)

        result.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                println("NICE")
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println(t.toString())
            }

        })
    }

}


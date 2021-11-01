package com.example.proyectosistemasmoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentoperfil.*
import android.content.SharedPreferences
import android.os.Build

import kotlinx.android.synthetic.main.fragment_fragmentoperfil.view.*
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF.length
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.airbnb.lottie.LottieAnimationView
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Usuario
import com.example.proyectosistemasmoviles.services.RestEngine
import com.example.proyectosistemasmoviles.services.UserService
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.activity_inicio.view.*
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.fragment_cms.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


class fragmentoperfil : Fragment() {
    var pref: SharedPreferences? = null
    var id : Int? = null
    lateinit var dialog: BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoperfil, container, false)
        //SharedPreference
        dialog = BottomSheetDialog(requireContext())
        pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
          id = pref?.getInt("Id",0)
        var name = pref?.getString("Nombre","");
        var email = pref?.getString("Email","");
        var image = pref?.getString("Image","");

        vista.buttonFavoritos.setOnClickListener {
            val activity = context as AppCompatActivity
            val frag = fragmentofavoritos()
            val args = Bundle()
            frag.setArguments(args)
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();
        }

        vista.button6.setOnClickListener {
            val activity = context as AppCompatActivity
            val frag = fragment_mis_resenas()
            val args = Bundle()
            frag.setArguments(args)
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();
        }

        vista.button5.setOnClickListener {
            val activity = context as AppCompatActivity
            val frag = fragmentocontrasena()
            val args = Bundle()
            frag.setArguments(args)
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit();
        }

        vista.cierraSesion.setOnClickListener {
cierras()
        }
        var like2 = false
        vista.botoncam.setOnClickListener {
            like2 = heartanimation2(fotito,R.raw.camarita,like2)

            Handler().postDelayed({

                showDialog()


            }, 3000)


        }
        vista.txtEmail.text = email;
        vista.txtName.text = name;
        if(!image!!.isEmpty()){
            val imageBytes = Base64.getDecoder().decode(image)
            val imageBitmap:Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            vista?.imagenPerfil?.setImageBitmap(imageBitmap)


        }



        return vista

    }
    private fun showDialog() {
        dialog.setContentView(R.layout.dialog_image)
        dialog.show()

        dialog.imageButtonClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_galeria.setOnClickListener {
            changeImage(1)
            dialog.dismiss()
        }
        dialog.btn_camara.setOnClickListener {
            changeImage(2)
            dialog.dismiss()
        }

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

    private fun changeImage(case:Int) {

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


            if (boolDo == true && case == 1) {
                pickImageFromGallery()
            }

            if (boolDo == true && case == 2) {
                pickImageFromCamera()
            }

        }


    }
    private fun pickImageFromCamera() {
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 1002)
    }

    private fun pickImageFromGallery() {
        //Abrir la galería
        val intent = Intent()
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
                    pickImageFromCamera()
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
            if (requestcode == 1000) {
                imagenPerfil.setImageURI(data?.data)
                val bitmaps = (imagenPerfil.getDrawable() as BitmapDrawable).bitmap
                val comprime = ByteArrayOutputStream()
                bitmaps.compress(Bitmap.CompressFormat.JPEG, 10, comprime)
                var imageByteArray: ByteArray = comprime.toByteArray()
                saveImageUser(imageByteArray)



            }
            if(requestcode == 1002) {

                val photo =  data?.extras?.get("data") as Bitmap
                val toolbar: androidx.appcompat.widget.Toolbar? = (activity as Inicio?)?.barran
                toolbar?.imagenPerfilToolbar?.setImageBitmap(photo)
                imagenPerfil.setImageBitmap(photo)
                val comprime = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 25, comprime)
                saveImageUser(comprime.toByteArray())


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
                var resp = response.body()
                if(resp!= null){



                        val editor = pref?.edit()
                        editor?.putString("Image",encodedString)
                        editor?.commit()

                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println(t.toString())
            }

        })
    }

}


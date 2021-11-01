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
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.proyectosistemasmoviles.Modelos.Estatus
import com.example.proyectosistemasmoviles.Modelos.Images
import com.example.proyectosistemasmoviles.Modelos.Modificar
import com.example.proyectosistemasmoviles.Modelos.Review
import com.example.proyectosistemasmoviles.adaptadores.cargacms
import com.example.proyectosistemasmoviles.services.ImagesService
import com.example.proyectosistemasmoviles.services.Reseñas
import com.example.proyectosistemasmoviles.services.RestEngine
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.fragment_cms.*
import kotlinx.android.synthetic.main.fragment_cms.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class fragmentocms2 : Fragment() {
    var pref: SharedPreferences? = null
    var id : Int? = null
    var id_update : Int? = null
    var tituloU : String? = null
    var subtituloU : String? = null
    var contenidoU : String? = null
    val imageList = mutableListOf<ByteArray>()
    lateinit var dialog: BottomSheetDialog
private lateinit var cargacms: cargacms
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        id = pref?.getInt("Id",0)
        var vista = inflater.inflate(R.layout.fragment_cms2, container, false)

        val bundle = this.arguments
        if (bundle != null){

            id_update  = bundle.getInt("id")
            if(id_update != null){
                tituloU    = bundle.getString("titulo")
                subtituloU = bundle.getString("resena")
                contenidoU = bundle.getString("contenido")
                vista.contravieja.setText(tituloU)
                vista.contranueva.setText(subtituloU)
                vista.resenap.setText(contenidoU)
            }

        }

        dialog = BottomSheetDialog(requireContext())
        cargacms = cargacms(vista.context,imageList)
        vista.reciclaim.adapter = cargacms
       vista.botoni.setOnClickListener{
        showDialog()


       }

        vista.botonsubir.setOnClickListener {
            if (bundle != null && id_update != null){
                subirreseñaU()
            }
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

    private fun subirreseñaU(){
        val titulo: String = contravieja.text.toString()
        val premisa: String = contranueva.text.toString()
        val descripcion: String= resenap.text.toString()

        if(titulo.isEmpty() || premisa.isEmpty() || descripcion.isEmpty()){
            Toast.makeText(this.context, "Llene todos los campos", Toast.LENGTH_LONG).show()
        }else{
            val resena: Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)
            val result: Call<Estatus> = resena.ModificarU(
                Modificar(
                    id_update,titulo,premisa,descripcion
                )
            )
            result.enqueue(object : Callback<Estatus> {
                override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {

                    var resp = response.body()
                    if(resp!= null){

                        for( image in imageList ){
                            saveImageReview(id_update!!,image)
                        }
                        //Limpia el formulario
                        imageList.clear();
                        cargacms.notifyDataSetChanged()
                        contravieja.setText("")
                        contranueva.setText("")
                        resenap.setText("")

                        Toast.makeText(context, "Se modifico correctamente", Toast.LENGTH_LONG)
                            .show()
                        Handler().postDelayed({
                            val activity = context as AppCompatActivity
                            val frag =  fragment_mis_resenas()
                            val args = Bundle()
                            frag.setArguments(args)
                            activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentogeneral, frag).addToBackStack(null).commit()
                        }, 1200)


                    }
                }

                override fun onFailure(call: Call<Estatus>, t: Throwable) {
                    println(t.toString())
                }

            })

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

                imgHiden.setImageURI(data?.data)

                val bitmaps = (imgHiden.getDrawable() as BitmapDrawable).bitmap

                val comprime = ByteArrayOutputStream()

                bitmaps.compress(Bitmap.CompressFormat.JPEG, 10, comprime)

               var imageByteArray: ByteArray = comprime.toByteArray()

                imageList.add(imageByteArray)
                cargacms.notifyDataSetChanged()
            }

            if (requestcode == 1002) {

                val photo =  data?.extras?.get("data") as Bitmap
                val comprime = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 25, comprime)
                imageList.add(comprime.toByteArray())
                cargacms?.notifyDataSetChanged()
            }

        }
    }

    private fun saveImageReview(idReview :Int,img: ByteArray) {

      val encodedString:String =  Base64.getEncoder().encodeToString(img)
        var images : Images = Images(
            null,
            encodedString,
            idReview
        )
        val imagesService : ImagesService = RestEngine.getRestEngine().create(ImagesService::class.java)

        val result: Call<Estatus> = imagesService.saveImage(images)

        result.enqueue(object : Callback<Estatus> {
            override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {
                var resp = response.body()
                if(resp!= null){
                    println(resp.toString())

                }
            }

            override fun onFailure(call: Call<Estatus>, t: Throwable) {
                println(t.toString())
            }

        })
    }

}
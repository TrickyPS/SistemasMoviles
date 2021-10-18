package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import android.R.raw
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.example.proyectosistemasmoviles.Modelos.*
import com.example.proyectosistemasmoviles.services.*
import com.synnapps.carouselview.ImageListener
import com.synnapps.carouselview.ViewListener
import kotlinx.android.synthetic.main.item_list_cms.*
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class fragmentoresenas : Fragment() {

    var carouselImages=  mutableListOf<Bitmap>()
    var carouselTitle:String? = null
    var like = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoresenas, container, false)
        var id_review:Int? = null
        val bundle = this.arguments
        if (bundle != null)
            id_review= bundle.getInt("id")

        val  pref = context?.getSharedPreferences("usuario", Context.MODE_PRIVATE)
        val id = pref?.getInt("Id",0)
        getReview(id_review!!,id!!);



        vista.heart.setOnClickListener {

            like = heartanimation(heart,R.raw.animacion,like)
            enviarVoto(id,id_review)
        }
var like2 = false
        vista.botons.setOnClickListener {
            val comentario = editTextTextPersonName5.text
            if(!comentario.isEmpty()) {
                like2 = heartanimation2(yes, R.raw.anim2, like2)
                enviarComentario(id, id_review,comentario.toString())
            }
        }



        return vista
    }

    private fun enviarVoto(id_user: Int, id_review: Int) {
        var intLike = 0
        if(like)
            intLike = 1
        val votoService : VotoService = RestEngine.getRestEngine().create(VotoService::class.java)
        val result: Call<Estatus> = votoService.addVoto(Votos(id_user,id_review,intLike))
        //Para traer la info del review
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

    private fun enviarComentario(id_user:Int,id_review: Int,comentario:String) {

            val comentariosService : ComentariosService = RestEngine.getRestEngine().create(ComentariosService::class.java)
            editTextTextPersonName5.setText("")
            val result: Call<Estatus> = comentariosService.addComentario(Comentarios(comentario,id_review,id_user))
            //Para traer la info del review
            result.enqueue(object : Callback<Estatus> {
                override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {
                    var resp = response.body()
                    if(resp!= null){
                        //TODO agregar mensaje a lista de comentarios-adapter
                    }
                }

                override fun onFailure(call: Call<Estatus>, t: Throwable) {
                    println(t.toString())
                }

            })


    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView?) {
            imageView?.setImageBitmap(carouselImages[position])
        }
    }

    private fun getReview(id_review:Int,id:Int) {
        val reviewService : Reseñas = RestEngine.getRestEngine().create(Reseñas::class.java)

        val result: Call<ReviewPublic> = reviewService.getReviewById(id_review,id)
        //Para traer la info del review
        result.enqueue(object : Callback<ReviewPublic> {
            override fun onResponse(call: Call<ReviewPublic>, response: Response<ReviewPublic>) {
                var resp = response.body()
                if(resp!= null){
                    txtPremisa.text = resp.subtitulo;
                    txtTitleBook.text = resp.titulo;
                    txtResena.text = resp.contenido;
                    countVotes.text = resp.votos.toString()
                    if(resp.isVoted == 1)
                        like = heartanimation(heart,R.raw.animacion,like)
                    //Para traer las imagenes del preview
                    getAllImages(id_review)
                }
            }

            override fun onFailure(call: Call<ReviewPublic>, t: Throwable) {
                println(t.toString())
            }

        })

    }

    private fun getAllImages(idReview: Int) {
        val imagesService : ImagesService = RestEngine.getRestEngine().create(ImagesService::class.java)

        val result: Call<List<Images>> = imagesService.getAllImages(idReview)

        result.enqueue(object : Callback<List<Images>> {
            override fun onResponse(call: Call<List<Images>>, response: Response<List<Images>>) {
                var resp = response.body()
                if(resp!= null){


                    for(image in resp){
                        val imgEncode = Base64.getDecoder().decode(image?.image)
                        val imageBitmap:Bitmap = BitmapFactory.decodeByteArray(imgEncode, 0, imgEncode!!.size)
                        carouselImages.add(imageBitmap)
                    }
                   carousel()
                }
            }

            override fun onFailure(call: Call<List<Images>>, t: Throwable) {
                getAllImages(idReview)
            }

        })

    }

    private fun carousel() {


       carouselView.setImageListener(imageListener)
        carouselView.pageCount = carouselImages.size
    }

    private fun heartanimation(imageView: LottieAnimationView,
                              animation: Int,
                              like: Boolean) : Boolean {

        if (!like) {
            imageView.setAnimation(animation)
            imageView.playAnimation()
        } else {
            imageView.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animator: Animator) {

                        imageView.setImageResource(R.drawable.twitter_like)
                        imageView.alpha = 1f
                    }

                })

        }

        return !like
    }
    private fun heartanimation2(imageView: LottieAnimationView,
                               animation: Int,
                               like: Boolean) : Boolean {

        if (!like) {
            imageView.setAnimation(animation)
            imageView.playAnimation()

        } else {
            imageView.setAnimation(R.raw.efct)
            imageView.playAnimation()
        }

        return !like
    }
}





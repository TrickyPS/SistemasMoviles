package com.example.proyectosistemasmoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.*
import kotlinx.android.synthetic.main.fragment_fragmentoresenas.view.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.inputmethod.InputMethodManager
import com.example.proyectosistemasmoviles.Modelos.*
import com.example.proyectosistemasmoviles.adaptadores.ComentariosAdapter
import com.example.proyectosistemasmoviles.services.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.dialog_comentario.*
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.fragment_fragmentoinicio.view.*
import kotlinx.android.synthetic.main.item_list_cms.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class fragmentoresenas : Fragment() {

    var carouselImages=  mutableListOf<Bitmap>()
    var like = false
    var like2 = false
    var ismark = false

    var comentariosList = mutableListOf<Comentarios>()
    private lateinit var comentariosAdapter: ComentariosAdapter
    lateinit var dialog: BottomSheetDialog

    lateinit var imm: InputMethodManager

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

        imm=
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        comentariosAdapter = ComentariosAdapter(vista.context,comentariosList)
        vista.rv_comentarios.adapter = comentariosAdapter
        getReview(id_review!!,id!!);

        getComentarios(id_review)

        vista.heart.setOnClickListener {

            like = heartanimation(heart,R.raw.animacion,like)
            enviarVoto(id,id_review)
        }

        vista.botons.setOnClickListener {

                like2 = heartanimation2(yes, R.raw.anim2, like2)
                ismark = markAnimate(botons,R.raw.bookmark,ismark)
                enviarFavorito(id,id_review)

        }

        vista.editTextTextPersonName5.setOnClickListener {
            showDialog(id_review,id)
        }

        return vista
    }

    private fun showDialog(id_review: Int,id:Int) {
        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.dialog_comentario)
        dialog.show()

        dialog.edittextCom.requestFocus()
        var viewi = requireActivity().currentFocus

        if (viewi == null) {
            viewi = View(activity)
        }

        //imm.showSoftInput(dialog.edittextCom,InputMethodManager.SHOW_FORCED)


        dialog.imageButton.setOnClickListener {
            val comentario = dialog.edittextCom.text
            if(!comentario.isEmpty()) {
                enviarComentario(id, id_review,comentario.toString())
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                dialog.dismiss()
            }
        }
    }



    private fun getComentarios(idReview: Int) {
        val comentariosService : ComentariosService = RestEngine.getRestEngine().create(ComentariosService::class.java)

        val result: Call<List<Comentarios>> = comentariosService.getComentarios(idReview)
        //Para traer la info del review
        result.enqueue(object : Callback<List<Comentarios>> {
            override fun onResponse(call: Call<List<Comentarios>>, response: Response<List<Comentarios>>) {
                var resp = response.body()
                if(resp!= null){
                   for(comentario in resp){
                       comentariosList.add(comentario)
                       comentariosAdapter.notifyDataSetChanged()
                   }
                }
            }

            override fun onFailure(call: Call<List<Comentarios>>, t: Throwable) {
                println(t.toString())
            }

        })
    }

    private fun enviarFavorito(id_user: Int, id_review: Int){
        var intmark = 0
        if(ismark)
            intmark = 1
        val favoritoService : FavoritosService = RestEngine.getRestEngine().create(FavoritosService::class.java)
        val result: Call<Estatus> = favoritoService.addFavorito(Favoritos(id_user,id_review,intmark))
        result.enqueue(object : Callback<Estatus> {
            override fun onResponse(call: Call<Estatus>, response: Response<Estatus>) {
                println(response.toString())
            }

            override fun onFailure(call: Call<Estatus>, t: Throwable) {
                println(t.toString())
            }

        })
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
                   if(intLike == 0){
                       countVotes.text = (Integer.parseInt(countVotes.text.toString()) - 1).toString()
                   }else{
                       countVotes.text = (Integer.parseInt(countVotes.text.toString()) + 1).toString()
                   }
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
                      comentariosList.clear()
                        getComentarios(id_review)
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
                    if(resp.check == 1) {
                        ismark = markAnimate(botons, R.raw.bookmark, ismark)
                        like2 = heartanimation2(yes, R.raw.anim2, like2)
                    }
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
               // getAllImages(idReview)
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

    private fun markAnimate(imageView: LottieAnimationView,
                                animation: Int,
                                ismark: Boolean) : Boolean {
        if (!ismark) {
            imageView.setAnimation(animation)
            imageView.playAnimation()
        } else {
            imageView.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animator: Animator) {

                        imageView.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                        imageView.alpha = 1f
                    }

                })

        }

        return !ismark
    }




}





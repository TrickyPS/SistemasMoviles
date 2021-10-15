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
import kotlinx.android.synthetic.main.item_list_cms.*
import okhttp3.internal.wait


class fragmentoresenas : Fragment() {
    var sampleImages = intArrayOf(
        R.drawable.fotopasillo,R.drawable.fotopasillo,R.drawable.fotopasillo,R.drawable.fotopasillo
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var vista = inflater.inflate(R.layout.fragment_fragmentoresenas, container, false)


        vista.carouselView.pageCount = sampleImages.size

        vista.carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
        }


        var like = false
        vista.heart.setOnClickListener {

            like = heartanimation(heart,R.raw.animacion,like)
        }
var like2 = false
        vista.botons.setOnClickListener {

           like2 = heartanimation2(yes,R.raw.anim2,like2)
        }



        return vista
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





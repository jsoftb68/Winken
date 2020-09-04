package com.example.winken

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration

class MainActivity : AppCompatActivity() {

    private lateinit var va: ValueAnimator
    private var animationWidth: Float = 0f
    private val maxDurationMs = 10000   // Max. passing duration in ms
    private val maxDelayMs = 5000       // Max. start delay in ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv_vehicle.visibility = View.INVISIBLE
        tv_winken.visibility = View.INVISIBLE
    }

    fun onClickBtnStart(view: View) {
        startAnimator()
        btn_start.visibility = View.INVISIBLE
    }

    private fun startAnimator() {

        animationWidth = (gameArea.width + iv_vehicle.width).toFloat()

        val va = ValueAnimator.ofFloat(animationWidth)
        va.interpolator = LinearInterpolator()
        va.startDelay = (Math.random()*maxDelayMs).toLong()
        va.duration = (Math.random()*maxDurationMs).toLong()

        val speedNiveau: Float = 1f - va.duration.toFloat() / maxDurationMs
        iv_vehicle.setImageResource(getImageResourceId(speedNiveau))
        iv_vehicle.scaleX = 0.5f
        iv_vehicle.scaleY = 0.5f
        iv_vehicle.y = gameArea.height * 0.6f

        va.addUpdateListener {
            iv_vehicle.x = (va.animatedValue as Float - iv_vehicle.width).toFloat()

            if (iv_vehicle.x > -iv_vehicle.width/2 && iv_vehicle.x < gameArea.width - iv_vehicle.width/2){
                tv_winken.visibility = if ((va.animatedFraction * 100) % 20 < 10) View.VISIBLE else View.INVISIBLE
            }else{
                tv_winken.visibility = View.INVISIBLE
            }
        }

        va.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                iv_vehicle.x = -iv_vehicle.width.toFloat()
                iv_vehicle.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startAnimator()
            }
            override fun onAnimationRepeat(animation: Animator?) {
                super.onAnimationRepeat(animation)
            }
        })

        va.start()
    }

    private fun getImageResourceId(speed: Float) : Int {

        return when {
            speed < .05f -> R.drawable.fahrrad
            speed < .10f -> R.drawable.raupe
            speed < .20f -> R.drawable.traktor
            speed < .30f -> R.drawable.kipper
            speed < .35f -> R.drawable.bus
            speed < .40f -> R.drawable.lkw
            speed < .43f -> R.drawable.eisenbahn
            speed < .45f -> R.drawable.auto1
            speed < .50f -> R.drawable.taxi
            speed < .55f -> R.drawable.krankenwagen
            speed < .60f -> R.drawable.auto2
            speed < .65f -> R.drawable.polizei
            speed < .70f -> R.drawable.auto3
            speed < .80f -> R.drawable.heli
            else -> R.drawable.flugzeug
        }
    }
}

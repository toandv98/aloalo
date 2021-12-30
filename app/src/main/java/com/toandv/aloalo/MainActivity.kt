package com.toandv.aloalo

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.toandv.aloalo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val frame = FrameLayout(this)
        val imageViewMap = ImageView(this).apply {
            setImageResource(R.drawable.ic_map)
        }
        frame.addView(imageViewMap)
        repeat(100) {
            val imageView = ImageView(this).apply {
                setImageResource(R.drawable.avd_anim)
            }
            val layoutParams = FrameLayout.LayoutParams(50, 50).apply {
                leftMargin = Random.nextInt(0, windowHeight / 98 * 200)
                topMargin = Random.nextInt(0, windowHeight)
            }
            frame.addView(imageView, layoutParams)
        }

        binding.zoom.addView(frame, windowHeight / 98 * 200, windowHeight)

        frame.children
            .filterIsInstance<ImageView>()
            .map { it.drawable }
            .filterIsInstance<AnimatedVectorDrawable>()
            .forEach { it.start() }
    }
}


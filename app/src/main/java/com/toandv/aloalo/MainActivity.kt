package com.toandv.aloalo

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import coil.load
import com.toandv.aloalo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var pX = 0f
    private var pY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val frame = FrameLayout(this)
        val imageViewMap = ImageView(this).apply {
            load(R.drawable.ic_map)
        }
        frame.addView(imageViewMap)
        val mapView = MapView(this)
        frame.addView(mapView)
        binding.zoom.addView(frame, windowHeight / 98 * 200, windowHeight)
        mapView.setOnDotClickListener { x, y ->
            binding.zoom.controller.run {
                val newState = state.copy()
                newState.translateBy(720 - pX, 1288 - pY)
                newState.zoomTo(2f, 720f, 1288f)
                animateStateTo(newState)
            }
            frame.children.find { it is AppCompatTextView }?.let { frame.removeView(it) }
            val textView = AppCompatTextView(frame.context).apply {
                text = "AAAAAAAAAA"
                setTextColor(Color.WHITE)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                doOnLayout {
                    it.x = x - it.measuredWidth / 2
                    it.y = y - it.measuredHeight
                }
            }
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            frame.addView(textView, layoutParams)
        }

        binding.root.post {
            mapView.drawDot(generateSequence {
                Pair(
                    Random.nextInt(mapView.left, mapView.right).toFloat(),
                    Random.nextInt(mapView.top, mapView.bottom).toFloat()
                )
            }.take(100).toMap(), 30f)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            pX = ev.x
            pY = ev.y
        }
        return super.dispatchTouchEvent(ev)
    }
}

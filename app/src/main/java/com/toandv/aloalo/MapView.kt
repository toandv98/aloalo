package com.toandv.aloalo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt

class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var dotClickListener: ((Float, Float) -> Unit)? = null
    private val coordinatesMap = mutableMapOf<Float, Float>()
    private var dotRadius = 30f
    private var pX = 0f
    private var pY = 0f

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.green)
        style = Paint.Style.FILL
    }
    private val dotStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.green_30)
        strokeWidth = dotRadius * 2 / 3
        style = Paint.Style.STROKE
    }

    init {
        setOnClickListener { handleClick() }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        coordinatesMap.forEach {
            canvas?.drawCircle(it.key, it.value, dotRadius * 2 / 3, dotPaint)
            canvas?.drawCircle(it.key, it.value, dotRadius, dotStrokePaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            pX = event.x
            pY = event.y
        }
        return super.onTouchEvent(event)
    }

    fun drawDot(
        coordinates: Map<Float, Float>,
        size: Float,
        @ColorInt color: Int = context.getColor(R.color.green),
        @ColorInt strokeColor: Int = context.getColor(R.color.green_30),
    ) {
        coordinatesMap.clear()
        coordinatesMap.putAll(coordinates)
        dotRadius = size
        dotPaint.color = color
        dotStrokePaint.color = strokeColor
        invalidate()
    }

    fun setOnDotClickListener(listener: (Float, Float) -> Unit) {
        dotClickListener = listener
    }

    private fun handleClick() {
        dotClickListener?.run {
            coordinatesMap.forEach {
                if ((pX - dotRadius..pX + dotRadius).contains(it.key)
                    && (pY - dotRadius..pY + dotRadius).contains(it.value)
                ) {
                    invoke(it.key, it.value - dotRadius)
                    return@forEach
                }
            }
        }
    }
}

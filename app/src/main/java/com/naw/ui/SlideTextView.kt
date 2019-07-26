package com.naw.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView
import android.R.attr.y
import android.view.animation.LinearInterpolator


class SlideTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var slideSpeed: Long = DEFAULT_SPEED
    private val visibleLine: Int
    private var slidePos: Float = 0.0f
    private val slideHandler: Handler
    private var currentLine = 0
    private var canScroll = false
    private val slideRunnable = Runnable {
        if (canScroll) {
            val slide = calculateLineHeight()

            slidePos += slide
            android.util.Log.d("[KGB]", "slidePos: $scrollY, scrollTo: $slidePos")

            val animator = ObjectAnimator.ofInt(this, "scrollY", slidePos.toInt())
            animator.interpolator = LinearInterpolator()
            animator.duration = slideSpeed
            animator.addUpdateListener {
                if (!canScroll)
                    it.cancel()
            }
            animator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    scrollY = slidePos.toInt()
                    if (!allLineVisible())
                        nextLine()
                    else
                        backToTop()
                }
            })
            animator?.start()
        }
    }

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideTextView, defStyleAttr, 0)
        val test = typeArray.getString(R.styleable.SlideTextView_test)
        slideSpeed = typeArray.getInteger(R.styleable.SlideTextView_slide_speed, 0).toLong()
        visibleLine = typeArray.getInteger(R.styleable.SlideTextView_visible_line, 0)
        android.util.Log.d("[KGB]", "test: $test, slideSpeed: $slideSpeed, visibleLine: $visibleLine")
        setLines(visibleLine)
        typeArray.recycle()
        slideHandler = Handler()
    }

    fun startSlideLine() {
        scrollY = 0
        currentLine = 0
        canScroll = true
        slideHandler.postDelayed({
            nextLine()
        }, slideSpeed)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //startSlideLine()
    }

    override fun onDetachedFromWindow() {
        stopSlider()
        super.onDetachedFromWindow()
    }

    private fun calculateLineHeight(): Float {
        return lineHeight + lineSpacingExtra + lineSpacingMultiplier
    }

    private fun nextLine() {
        currentLine += 1
        slideHandler.post(slideRunnable)
    }

    private fun allLineVisible(): Boolean {
        return currentLine + visibleLine >= lineCount
    }

    private fun stopSlider() {
        slideHandler.removeCallbacks(slideRunnable)
        canScroll = false
    }

    private fun backToTop() {
        slideHandler.postDelayed({
            slidePos = 0f
            currentLine = 0
            val yTranslate = ObjectAnimator.ofInt(this, "scrollY", 0)
            yTranslate.duration = slideSpeed
            yTranslate.start()
            yTranslate.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    startSlideLine()
                }
            })

        }, 1000)
    }

    companion object {
        private const val DEFAULT_SPEED = 1000L
    }
}
package com.naw.ui

import android.content.Context
import android.os.Handler
import android.text.Layout
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

class TruncatedTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var originalText: String = ""
    private val ellipsisText = " [more]"

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideTextView, defStyleAttr, 0)
        val test = typeArray.getString(R.styleable.SlideTextView_test)
        ellipsisText = typeArray.getInteger(R.styleable.SlideTextView_slide_speed, 0).toLong()
        visibleLine = typeArray.getInteger(R.styleable.SlideTextView_visible_line, 0)
        android.util.Log.d("[KGB]", "test: $test, slideSpeed: $slideSpeed, visibleLine: $visibleLine")
        setLines(visibleLine)
        typeArray.recycle()
        slideHandler = Handler()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        layout?.let {
            originalText = text.toString()
            android.util.Log.d("[KGB]", "org: $text")
            val ellipsisCount = it.getEllipsisCount(it.lineCount - 1)
            if (ellipsisCount > 0 ) {
                var truncateText = it.text.substring(0, it.text.length - ellipsisCount) + ELLIPSIS_NORMAL
                android.util.Log.d("[KGB]", "before: $truncateText")
                truncateText = truncateText.substring(0, truncateText.length - 1 - ellipsisText.length - ELLIPSIS_NORMAL.length) + ELLIPSIS_NORMAL + ellipsisText
//                if (originalText?.length ?: 0 > 1) {
//                    originalText = originalText!!.substring(0,
//                        originalText!!.length - 1 - ellipsisText.length) + ellipsize
                text = truncateText
//                    measure(widthMeasureSpec, heightMeasureSpec)
//                }
//                android.util.Log.d("[KGB]", "after: $truncateText")
            }
        }
    }

    override fun getEllipsize(): TextUtils.TruncateAt {
        return super.getEllipsize()
    }

    companion object {
        const val ELLIPSIS_NORMAL = "\u2026"
    }
}
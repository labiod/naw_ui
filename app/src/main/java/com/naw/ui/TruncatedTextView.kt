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
    private var ellipsisText: String? = null

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TruncatedTextView, defStyleAttr, 0)
        ellipsisText = typeArray.getString(R.styleable.TruncatedTextView_trun_text)
        typeArray.recycle()
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
            if (ellipsisCount > 0 && ellipsisText != null) {
                var truncateText = it.text.substring(0, it.text.length - ellipsisCount) + ELLIPSIS_NORMAL
                android.util.Log.d("[KGB]", "before: $truncateText")
                truncateText = truncateText.substring(0, truncateText.length - 1 - ellipsisText!!.length - ELLIPSIS_NORMAL.length) + ELLIPSIS_NORMAL + ellipsisText
                text = truncateText
            }
        }
    }

    fun setEllipsisText(ellipsisText: String) {
        this.ellipsisText = ellipsisText
        requestLayout()
    }

    companion object {
        const val ELLIPSIS_NORMAL = "\u2026"
    }
}
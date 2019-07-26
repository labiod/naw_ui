package com.naw.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        slide_view.text = "test"
        slide_button.setOnClickListener {
            slide_view.startSlideLine()
        }
        text_to_cut.text = "This is very long text and will be truncate, but how to do that? Maybe i need so extra view or utils" +
                "I think i must write more letter because now text is to short, for example: this is place for some examples" +
                "first is text about nothing; second text about something you would say when you read this desc and the last " +
                "UPPER CASE TEXT if you want more please write next line"
    }
}

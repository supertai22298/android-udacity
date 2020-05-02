package com.example.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
    }

    private fun setListener() {
        val clickableViews: List<View> = listOf(
            box_one_text,
            box_two_text,
            box_three_text,
            box_four_text,
            box_five_text,
            constraint_layout,
            red_button,
            yellow_button,
            green_button
        )
        for (item in clickableViews) {
            item.setOnClickListener {
                makeColor(it)
            }
        }
    }

    private fun makeColor(view: View) {
        when (view.id) {
            R.id.box_one_text -> view.setBackgroundColor(Color.CYAN)
            R.id.box_two_text -> view.setBackgroundColor(Color.YELLOW)
            R.id.box_three_text -> view.setBackgroundColor(Color.BLUE)
            R.id.box_four_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_five_text -> view.setBackgroundColor(Color.GRAY)
            R.id.red_button -> box_three_text.setBackgroundColor(Color.RED)
            R.id.yellow_button -> box_four_text.setBackgroundColor(Color.YELLOW)
            R.id.green_button -> box_five_text.setBackgroundColor(Color.GREEN)
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}

package com.example.staysunny.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.staysunny.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Si decides usar layout con edge-to-edge, puedes descomentar esto:
        /*
        setContentView(R.layout.activity_main)
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val bars = insets.getInsets(android.view.WindowInsets.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        */
    }
}

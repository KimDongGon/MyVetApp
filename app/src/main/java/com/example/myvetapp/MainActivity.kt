package com.example.myvetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        Handler().postDelayed(Runnable {
            var i = Intent(this, LoginActivity::class.java);
            startActivity(i)
            finish()
        }, 3000)
    }
}

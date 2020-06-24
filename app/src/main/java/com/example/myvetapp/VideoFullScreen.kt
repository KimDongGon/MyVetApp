package com.example.myvetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_video_full_screen.*

class VideoFullScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_full_screen)
        init()
    }

    private fun init() {
        val link = intent.getStringExtra("link")
        Log.d("url", link)
        fullVideo.loadUrl(link)
        fullVideo.webViewClient = WebViewClient()
        fullVideo.webChromeClient = WebChromeClient()
        fullVideo.settings.javaScriptEnabled = true
    }
}
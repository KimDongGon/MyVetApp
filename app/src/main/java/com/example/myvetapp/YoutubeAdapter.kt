package com.example.myvetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class YoutubeAdapter(var items:ArrayList<DataSetList>) :
    RecyclerView.Adapter<YoutubeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var webView:WebView = itemView.findViewById(R.id.webView)
        var fullscreenbtn:Button = itemView.findViewById(R.id.fullscreenbtn)
        init{
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()
            webView.settings.javaScriptEnabled = true
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.video_per_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current = items.get(position)

        holder.webView.loadUrl(current.link)
        holder.fullscreenbtn.setOnClickListener {
            var intent = Intent(holder.itemView.context,VideoFullScreen::class.java)
            intent.putExtra("link", current.link)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
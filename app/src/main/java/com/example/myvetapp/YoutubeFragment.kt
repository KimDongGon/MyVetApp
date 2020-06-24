package com.example.myvetapp

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_youtube.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.ref.WeakReference
import java.net.URL

class YoutubeFragment : Fragment() {
    lateinit var layoutManager: LinearLayoutManager
//    lateinit var layoutManager: GridLayoutManager
    lateinit var youtubeAdapter: YoutubeAdapter
    var dataSetList = ArrayList<DataSetList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_youtube, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
//        layoutManager = GridLayoutManager(requireActivity(),2,GridLayoutManager.VERTICAL,false)
        youtubeRecyclerView.layoutManager = layoutManager
        youtubeRecyclerView.setHasFixedSize(true)

        val task = MyAsyncTask(this)
        task.execute(URL("https://www.youtube.com/results?search_query=%EA%B0%9C"))
    }

    class MyAsyncTask(context: YoutubeFragment): AsyncTask<URL, Unit, Unit>(){
        var activityreference = WeakReference(context)
        override fun doInBackground(vararg p0: URL?) {
            val activity = activityreference.get()
            val apiKey = "AIzaSyCeL16_awOQ9LUcgYwAYNEd3ZW7gaxBwcg"
            val apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=반려동물 응급처치&maxResults=10&key="+apiKey
            Log.d("url", apiUrl)
            val doc: Document = Jsoup.connect(apiUrl).ignoreContentType(true).get()
            val json = JSONObject(doc.text())
            val array = json.getJSONArray("items")

            for(i in 0 until array.length()){
                val videoId = array.getJSONObject(i).getJSONObject("id").getString("videoId")
                var embed = "https://www.youtube.com/embed/"+videoId
                activity?.dataSetList?.add(DataSetList(embed))
            }

            if (activity != null) {
                activity?.youtubeAdapter = YoutubeAdapter(activity.dataSetList)
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val activity = activityreference.get()
            activity?.youtubeRecyclerView?.adapter = activity?.youtubeAdapter
        }
    }
}
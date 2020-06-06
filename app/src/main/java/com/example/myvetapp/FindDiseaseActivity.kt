package com.example.myvetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_find_disease.*

class FindDiseaseActivity : AppCompatActivity() {
    lateinit var autoadapter: ArrayAdapter<String>
    lateinit var diseaseadapter: MyDiseaseAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rdb: DatabaseReference
    var symptom = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_disease)
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        rdb = FirebaseDatabase.getInstance().getReference("Diseases/disease")
//        var rootRef = rdb.root
        val query = FirebaseDatabase.getInstance().reference.child("Diseases/disease").limitToFirst(100)
        val option = FirebaseRecyclerOptions.Builder<Disease>()
            .setQuery(query,Disease::class.java)
            .build()
        diseaseadapter = MyDiseaseAdapter(option)
//        diseaseadapter.itemClickListener = object :MyDiseaseAdapter.OnItemClickListener{
//            override fun OnItemClick(view: View, position: Int) {
//                pIdEdit.setText(adapter.getItem(position).pId.toString())
//                pNameEdit.setText(adapter.getItem(position).pName)
//                pQuantityEdit.setText(adapter.getItem(position).pQuantity.toString())
//            }
//        }
        recyclerView.adapter = diseaseadapter


        symptom = findSymptom()
        autoadapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, symptom)
    }

    private fun findSymptom(): ArrayList<String> {
        var arr = ArrayList<String>();
        return arr;
    }

    override fun onStart() {
        super.onStart()
        // 데이터의 변화되는 값 감지
        diseaseadapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        diseaseadapter.stopListening()
    }
}

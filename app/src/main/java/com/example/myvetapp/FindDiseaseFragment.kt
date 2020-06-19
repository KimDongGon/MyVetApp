package com.example.myvetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_find_disease.*

class FindDiseaseFragment : Fragment() {
    lateinit var autoadapter: ArrayAdapter<String>
    lateinit var diseaseadapter: MyDiseaseAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rdb: DatabaseReference
    var symptom = ArrayList<String>();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_disease, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        rdb = FirebaseDatabase.getInstance().reference.child("Diseases/disease")
        val query = FirebaseDatabase.getInstance().reference.child("Diseases/disease").limitToFirst(1524)
        val option = FirebaseRecyclerOptions.Builder<Disease>()
            .setQuery(query,Disease::class.java)
            .build()
        diseaseadapter = MyDiseaseAdapter(option)
        recyclerView.adapter = diseaseadapter

//        symptom = findSymptom()
        symptom.add("ala")
//        symptom.add("abb")
        Toast.makeText(requireActivity(), symptom[0], Toast.LENGTH_SHORT).show()
        autoadapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, symptom)
        autoCompleteTextView.setAdapter(autoadapter)
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

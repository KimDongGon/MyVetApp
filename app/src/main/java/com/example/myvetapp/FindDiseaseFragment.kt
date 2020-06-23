package com.example.myvetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
    var symptomSet = mutableSetOf<String>()
    var symptom = ArrayList<String>()
    var findQuery = false

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
        rdb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    var symptomValue = data.getValue(Disease::class.java)?.dSymptom
                    val strArray = symptomValue.toString().split("|")
                    for(i in 0 until strArray.size){
                        val strArray2 = strArray[i].split(",")
                        for(j in 0 until strArray2.size){
                            symptomSet.add(strArray2[j].replace(" ","").trim())
                        }
                    }
                }
                for(item in symptomSet){
                    symptom.add(item)
                    Log.d("symptom", item)
                }
            }

        })

        val query = rdb.limitToFirst(100)
        val option = FirebaseRecyclerOptions.Builder<Disease>()
            .setQuery(query,Disease::class.java)
            .build()
        diseaseadapter = MyDiseaseAdapter(option)
        recyclerView.adapter = diseaseadapter

        autoadapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, symptom)
        autoCompleteTextView.setAdapter(autoadapter)
        autoCompleteTextView.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(findQuery)
                    findQueryAdapter()
                else{
                    findQuery = true
                    findQueryAdapter()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    fun findQueryAdapter(){
        if(diseaseadapter!=null)
            diseaseadapter.stopListening()
        val query = rdb.orderByChild("dSymptom").equalTo(autoCompleteTextView.text.toString())
        val option = FirebaseRecyclerOptions.Builder<Disease>()
            .setQuery(query,Disease::class.java)
            .build()
        autoCompleteTextView.text = null
        diseaseadapter = MyDiseaseAdapter(option)
        recyclerView.adapter = diseaseadapter
        diseaseadapter
            .startListening()
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

package com.example.myvetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_find_disease.*
import org.w3c.dom.Text

class FindDiseaseFragment : Fragment() {
    lateinit var autoadapter: ArrayAdapter<String>
    lateinit var diseaseadapter: MyDiseaseAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rdb: DatabaseReference
    var symptomSet = mutableSetOf<String>()
    var symptom = ArrayList<String>()
    var findList = ArrayList<String>()

    var auth: FirebaseAuth? = null
    var fragmentView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_find_disease, container, false)
        return fragmentView
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
                }
            }

        })

        val query = rdb.limitToFirst(1527)
        val option = FirebaseRecyclerOptions.Builder<Disease>()
            .setQuery(query,Disease::class.java)
            .build()
        diseaseadapter = MyDiseaseAdapter(option)
        recyclerView.adapter = diseaseadapter

        autoadapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, symptom)
        autoCompleteTextView.setAdapter(autoadapter)
        autoCompleteTextView.setOnItemClickListener { adapterView, view, position, l ->
            var newChip = Chip(requireActivity())
            newChip.setCloseIconVisible(true)
            newChip.setText((view as TextView).text.toString())
            findList.add(view.text.toString())
            chipGroup.addView(newChip)
            findQueryAdapter()
            autoCompleteTextView.text = null

            newChip.setOnCloseIconClickListener {
                chipGroup.removeView(it)
                findList.remove((it as TextView).text.toString())
                findQueryAdapter()
            }
        }

        logoutbtn.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.colorHoloBlueDark))
        auth = FirebaseAuth.getInstance()
        logoutbtn.setOnClickListener{
            activity?.finish()
            startActivity(Intent(activity, LoginActivity::class.java))
            auth?.signOut()
        }
    }

    fun findQueryAdapter(){
        if(diseaseadapter!=null)
            diseaseadapter.stopListening()
        if(findList.isEmpty()) {
            val query = rdb.limitToFirst(1527)
            val option = FirebaseRecyclerOptions.Builder<Disease>()
                .setQuery(query,Disease::class.java)
                .build()
            diseaseadapter = MyDiseaseAdapter(option)
            recyclerView.adapter = diseaseadapter
        }
        else{
            val query = rdb.orderByChild("dSymptom")
            val option = FirebaseRecyclerOptions.Builder<Disease>()
                .setQuery(query,Disease::class.java)
                .build()
            diseaseadapter = MyDiseaseAdapter(option, findList, true)
            recyclerView.adapter = diseaseadapter
        }
        diseaseadapter.startListening()
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

package com.example.myvetapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import org.w3c.dom.Text

class MyDiseaseAdapter(options: FirebaseRecyclerOptions<Disease>) :
    FirebaseRecyclerAdapter<Disease, MyDiseaseAdapter.ViewHolder>(options) {

    var itemClickListener:OnItemClickListener? = null

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var dname:TextView
        var dsymptom:TextView
        var dadvice:TextView
        var dcure:TextView

        init{
            dname = itemView.findViewById(R.id.dname)
            dsymptom = itemView.findViewById(R.id.dsymptom)
            dcure = itemView.findViewById(R.id.dcure)
            dadvice = itemView.findViewById(R.id.dadvice)

            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(it, adapterPosition)
            }
        }
    }

    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Disease) {
        holder.dname.text = model.dName
        holder.dsymptom.text = model.dSymptom
        holder.dcure.text = model.dCure
        holder.dadvice.text = model.dAdvice
    }


}
package com.example.myvetapp

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyDiseaseAdapter(options: FirebaseRecyclerOptions<Disease>, symptom:ArrayList<String> = arrayListOf(), check:Boolean = false) :
    FirebaseRecyclerAdapter<Disease, MyDiseaseAdapter.ViewHolder>(options) {
    var symptom = symptom
    var check = check
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var dname:TextView
        var dkind:TextView
        var dsymptom:TextView
        var dadvice:TextView
        var dcure:TextView
        var symptomtext:TextView
        var curetext:TextView
        var advicetext:TextView
        var kindtext:TextView

        init{
            dname = itemView.findViewById(R.id.dname)
            kindtext = itemView.findViewById(R.id.kindtext)
            dkind = itemView.findViewById(R.id.dkind)
            dsymptom = itemView.findViewById(R.id.dsymptom)
            dcure = itemView.findViewById(R.id.dcure)
            dadvice = itemView.findViewById(R.id.dadvice)
            symptomtext = itemView.findViewById(R.id.symptomtext)
            curetext = itemView.findViewById(R.id.curetext)
            advicetext = itemView.findViewById(R.id.advicetest)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Disease) {
        if(check){
            for(i in 0 until symptom.size){
                if(model.dSymptom.replace(" ","").trim().contains(symptom[i])){
                    if(i == symptom.size-1){
                        if(!model.dSymptom.equals("")){
                            holder.dname.setTypeface(null,Typeface.BOLD)
                            holder.dname.setTextColor(ResourcesCompat.getColor(holder.itemView.resources,R.color.colorHoloBlueDark,null))
                            holder.dname.textSize = 22.0f
                            holder.dname.text = model.dName
                            holder.symptomtext.text = "\n■ 증상"
                            holder.symptomtext.setTypeface(null,Typeface.BOLD)
                            holder.symptomtext.textSize = 18.0f
                            val strArray = model.dSymptom.split("|")
                            var str = ""
                            for(i in 1 until strArray.size) {
                                str += "- "+strArray[i].replace(" ","").trim()+"\n"
                            }
                            holder.dsymptom.text = str
                        }

                        if(!model.dKinds.equals("")) {
                            holder.kindtext.text = "\n■ 종류"
                            holder.kindtext.setTypeface(null,Typeface.BOLD)
                            holder.kindtext.textSize = 18.0f
                            holder.dkind.text = "-"+model.dKinds.trim()
                        }

                        if(!model.dCure.equals("")) {
                            holder.curetext.text = "■ 치료방법"
                            holder.curetext.setTypeface(null,Typeface.BOLD)
                            holder.curetext.textSize = 18.0f
                            holder.dcure.text = model.dCure.replace("#", "-").trim()
                        }

                        if(!model.dAdvice.equals("")) {
                            holder.advicetext.text = "\n■ 조언\n"
                            holder.advicetext.setTypeface(null, Typeface.BOLD)
                            holder.advicetext.textSize = 18.0f
                            holder.dadvice.text = "-"+model.dAdvice.trim()
                        }
                    }
                }
                else{
                    holder.dname.visibility = View.GONE
                    holder.symptomtext.visibility = View.GONE
                    holder.kindtext.visibility = View.GONE
                    holder.dkind.visibility = View.GONE
                    holder.dsymptom.visibility = View.GONE
                    holder.curetext.visibility = View.GONE
                    holder.dcure.visibility = View.GONE
                    holder.advicetext.visibility = View.GONE
                    holder.dadvice.visibility = View.GONE
                    break
                }
            }
        }
        else{
            holder.dname.setTypeface(null,Typeface.BOLD)
            holder.dname.setTextColor(ResourcesCompat.getColor(holder.itemView.resources,R.color.colorHoloBlueDark,null))
            holder.dname.textSize = 22.0f
            holder.dname.text = model.dName

            holder.symptomtext.text = "\n■ 증상"
            holder.symptomtext.setTypeface(null,Typeface.BOLD)
            holder.symptomtext.textSize = 18.0f
            val strArray = model.dSymptom.split("|")
            var str = ""
            for(i in 1 until strArray.size) {
                str += "- "+strArray[i].replace(" ","").trim()+"\n"
            }
            holder.dsymptom.text = str

            if(!model.dKinds.equals("")) {
                holder.kindtext.text = "\n■ 종류"
                holder.kindtext.setTypeface(null,Typeface.BOLD)
                holder.kindtext.textSize = 18.0f
                holder.dkind.text = "-"+model.dKinds.trim()
            }

            if(!model.dCure.equals("")) {
                holder.curetext.text = "■ 치료방법"
                holder.curetext.setTypeface(null,Typeface.BOLD)
                holder.curetext.textSize = 18.0f
                holder.dcure.text = model.dCure.replace("#", "-").trim()
            }

            if(!model.dAdvice.equals("")) {
                holder.advicetext.text = "\n■ 조언\n"
                holder.advicetext.setTypeface(null, Typeface.BOLD)
                holder.advicetext.textSize = 18.0f
                holder.dadvice.text = "-"+model.dAdvice.trim()
            }
        }
    }
}
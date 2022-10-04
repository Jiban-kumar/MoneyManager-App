package com.example.moneymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.model.DataModel
import kotlinx.android.synthetic.main.graph_item_view.view.*
import kotlin.collections.ArrayList

class PercentRecyclerViewAdpter(var list:ArrayList<DataModel>) : RecyclerView.Adapter<PercentRecyclerViewAdpter.PercentViewHolder>() {

    lateinit var adapter: RecyclerViewAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PercentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.graph_item_view, parent, false)
        return PercentViewHolder(view)
    }
    companion object{
        val cArray= arrayOf("Shopping","Food and Drink","Transport or Fuel",
            "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
        val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)
    }

    override fun onBindViewHolder(holder: PercentViewHolder, position: Int) {
        holder.itemView.apply {
            category1.text=list[position].category
            amount1.text= "${list[position].amount}"
            cicon1.setImageResource(list[position].logoid)
            for(i in cArray.indices){
                if(list[position].category==cArray[i]){
                    xbox1.setCardBackgroundColor(ContextCompat.getColor(this.context,colorArray[i]))
                }
            }

            var total =0.0
            for (i in list.indices){
                total+=list[i].amount!!.toDouble()
            }
            val sum=(list[position].amount!!.toDouble()*100/ total)
            var even=""
            even = if(sum.toString().contains('.')){
                val index=sum.toString().indexOf('.')
                if(index+2<sum.toString().length){
                    sum.toString().substring(0,index+2)
                } else{
                    sum.toString()
                }
            } else{
                sum.toString()
            }

            progressbargraph.progress = even.toDouble().toInt()
            percent.text = "($even%)"

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PercentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

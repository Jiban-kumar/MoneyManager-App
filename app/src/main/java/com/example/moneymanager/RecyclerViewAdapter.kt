package com.example.moneymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.model.DataModel
import kotlinx.android.synthetic.main.item_view.view.*

class RecyclerViewAdapter(private val list: ArrayList<DataModel>) :RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(){

    val cArray= arrayOf("Shopping","Food and Drink","Transport or Fuel",
        "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
    val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {
            amount.text="${list[position].amount}"
            category.text=list[position].category
            note.text=list[position].description
            pay_method.setImageResource(list[position].paymethod)
            cicon.setImageResource(list[position].logoid)
            for(i in cArray.indices){
                if(list[position].category==cArray[i]){
                    xbox.setCardBackgroundColor(ContextCompat.getColor(this.context,colorArray[i]))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }

}
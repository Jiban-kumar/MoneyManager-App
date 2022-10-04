package com.example.moneymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.model.ParentItemModel
import kotlinx.android.synthetic.main.parent_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ParentRecyclerViewAdapter(val parentlist:ArrayList<ParentItemModel>) : RecyclerView.Adapter<ParentRecyclerViewAdapter.ParentViewHolder>(){

    lateinit var adapter:RecyclerViewAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.parent_item_layout,parent,false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.itemView.apply {

            val day=parentlist[position].day.substring(0,2).toInt()
            val month=parentlist[position].day.substring(3,5).toInt()
            val year=parentlist[position].day.substring(6,parentlist[position].day.length).toInt()
            val c= Calendar.getInstance()
            val xformat= SimpleDateFormat("dd/MM/yyyy")
            val currentDate=xformat.format(c.time)
            var text="ss"
            if(currentDate== parentlist[position].day){
                text="today"
            }
            else{
                c.set(year,month-1,day)
                //val format=SimpleDateFormat("dd MMM,E")
                val format= SimpleDateFormat("dd MMM yyyy")
                text="${format.format(c.time)} "
            }

            spentDay.text=text
            totalSpent.text=parentlist[position].total
            adapter = RecyclerViewAdapter(parentlist[position].childlist)
            childRecyclerView.adapter=adapter
            childRecyclerView.layoutManager=LinearLayoutManager(holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return parentlist.size
    }
    inner class ParentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

}
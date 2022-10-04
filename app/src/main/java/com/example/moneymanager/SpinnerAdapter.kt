package com.example.moneymanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.moneymanager.model.CategoryModel
import kotlinx.android.synthetic.main.category_item_view.view.*

class SpinnerAdapter(private val context: Context,val list:ArrayList<CategoryModel>) :BaseAdapter(){
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val vh: ViewHolder
        val view:View
        if (p1 == null) {
            view = inflater.inflate(R.layout.category_item_view, p2, false)
            vh = ViewHolder(view)
            view?.tag = vh
        } else {
            view = p1
            vh = view.tag as ViewHolder
        }
        view.category_img.setImageResource(list[p0].id)
        view.category_name.text=list[p0].type
        view.category_img.setBackgroundResource(list[p0].colorId)

        return view
    }
    inner class ViewHolder(view:View){

    }
}
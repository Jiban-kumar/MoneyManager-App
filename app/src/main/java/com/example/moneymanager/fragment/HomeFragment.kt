package com.example.moneymanager.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.*
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.model.ParentItemModel
import com.example.moneymanager.viewmodel.MainViewModel
import com.example.moneymanager.viewmodel.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_layout.ltrecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    var list = ArrayList<DataModel>()
    var parentlist=ArrayList<ParentItemModel>()
    lateinit var adapter: ParentRecyclerViewAdapter
    lateinit var viewModel:MainViewModel
    private var percentlist=ArrayList<DataModel>()
    val cArray= arrayOf("Shopping","Food and Drink","Transport or Fuel",
        "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
    val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("state","Start")
        val repository = (activity?.application as ExpenceApplication).repository
        viewModel=ViewModelProvider(requireActivity(),ViewModelFactory(repository))[MainViewModel::class.java]
        if(viewLifecycleOwner==null){
            Toast.makeText(context, "See", Toast.LENGTH_SHORT).show()
        }
        viewModel.mainlivelist.observe(viewLifecycleOwner){
            list=it
            setTopUiData()
        }
        viewModel.parentlivelist.observe(viewLifecycleOwner) {
            parentlist = it
            adapter = ParentRecyclerViewAdapter(parentlist)
            ltrecyclerView.adapter = adapter
            ltrecyclerView.layoutManager = LinearLayoutManager(this.context)
        }
        //parentlist=viewModel.parentlist
        initPieChart()
        viewModel.percentlivelist.observe(viewLifecycleOwner){

            percentlist=it
            setDatatoPieChart()
        }



    }

    override fun onResume() {
        super.onResume()
        viewModel.parentlivelist.observe(viewLifecycleOwner) {
            parentlist = it
            adapter = ParentRecyclerViewAdapter(parentlist)
            ltrecyclerView.adapter = adapter
            ltrecyclerView.layoutManager = LinearLayoutManager(this.context)
        }
    }
    private fun setTopUiData() {
        var c=Calendar.getInstance()
        val format=SimpleDateFormat("dd/MM/yyyy")
        val month2=format.format(c.time).substring(3,5)

        var totalSpent=0.0
        var totalIncome=0.0
        for (j in list.indices){
            if(list[j].date!!.substring(3,5)==month2){
                if(list[j].type=="Expense"){
                    totalSpent+=list[j].amount!!.toDouble()
                }
                else{
                    totalIncome+=list[j].amount!!.toDouble()
                }
            }

        }

        smoney.text=totalSpent.toInt().toString()
        imoney.text=totalIncome.toInt().toString()
        bmoney.text=if (totalIncome>totalSpent){
            (totalIncome-totalSpent).toInt().toString()
        }
        else{
            "0"
        }
//        if(month==month2){
//
//        }
//        else{
//            smoney.text="0"
//            imoney.text="0"
//            bmoney.text="0"
//        }
//


    }


//    fun dataChanged(){
//        percentlist=ArrayList<DataModel>()
//        getPercentageData()
//        setDatatoPieChart()
//    }

    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setExtraOffsets(20f, 0f, 20f, 5f)
        pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        pieChart.legend.isWordWrapEnabled = true
    }

    private fun setDatatoPieChart() {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()

        val colors: ArrayList<Int> = ArrayList()
        var count=1
        for (i in percentlist.indices){
            if(percentlist[i].amount!!.toFloat()<6 && count==1){
                count++
                dataEntries.add(PieEntry(percentlist[i].amount!!.toFloat(),"other"))
                this.context?.let { ContextCompat.getColor(it,colorArray[8]) }
                    ?.let { colors.add(it) }
                //colors.add(Color.parseColor("#FF8A65"))
            }
            else{
                val category=percentlist[i].category.toString()
                dataEntries.add(PieEntry(percentlist[i].amount!!.toFloat(),category))
                //for coresponding color
                for (j in cArray.indices){
                    if(cArray[j]==category){
                        this.context?.let { ContextCompat.getColor(it,colorArray[j]) }
                            ?.let { colors.add(it) }
                    }
                }
            }


//            colors.add(Color.parseColor("#4DD0E1"))
//            colors.add(Color.parseColor("#FFF176"))
//            colors.add(Color.parseColor("#FF8A65"))
        }

        val dataSet = PieDataSet(dataEntries, null)
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 1f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 5f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //add text in center
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "Expense"



        pieChart.invalidate()
    }



}
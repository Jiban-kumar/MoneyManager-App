package com.example.moneymanager.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.ExpenceApplication
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.model.ParentItemModel
import com.example.moneymanager.PercentRecyclerViewAdpter
import com.example.moneymanager.R
import com.example.moneymanager.viewmodel.MainViewModel
import com.example.moneymanager.viewmodel.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_chart.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }
    var percentlist=ArrayList<DataModel>()
    var parentlist=ArrayList<ParentItemModel>()
    val cArray= arrayOf("Shopping","Food and Drink","Transport or Fuel",
        "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
    val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)
    val nameOfMonth= arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")
    lateinit var adapter: PercentRecyclerViewAdpter
    var indexOfMonth=0
    lateinit var viewModel:MainViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        count=0

        initPieChart()
        val repository = (activity?.application as ExpenceApplication).repository
        viewModel=ViewModelProvider(requireActivity(), ViewModelFactory(repository))[MainViewModel::class.java]

        viewModel.monthlivelist.observe(viewLifecycleOwner){

            parentlist=viewModel.monthlist
            manageMonthData()
            next.setOnClickListener {
                if(indexOfMonth!=11){
                    indexOfMonth++
                }
                else{
                    indexOfMonth=0
                    year++
                }
                manageMonthData()
            }
            previous.setOnClickListener {
                if(indexOfMonth!=0){
                    indexOfMonth--
                }
                else{
                    indexOfMonth=11
                    year--
                }
                manageMonthData()
            }
        }

    }

    var count=0
    var year=0
    private fun manageMonthData() {
        percentlist=ArrayList()
        if(count==0){
            var c=Calendar.getInstance()
            val format=SimpleDateFormat("dd/MM/yyyy")
            val month2=format.format(c.time).substring(3,5)
            year=format.format(c.time).substring(6,10).toInt()
            indexOfMonth=month2.toInt()-1

        }
        count++


        month.text="${nameOfMonth[indexOfMonth].substring(0,3)} $year"
        var isDataAvilable=false
        for (i in parentlist.indices){
            if(parentlist[i].day==month.text){
                isDataAvilable=true

                chartdatatext.visibility=View.GONE
                myPieChart.visibility=View.VISIBLE
                chartRecyclerView.visibility=View.VISIBLE


                getPercentageData(parentlist[i].childlist)
                setDatatoPieChart()
                myPieChart.centerText = "Expense\n${parentlist[i].total}"

                adapter=PercentRecyclerViewAdpter(parentlist[i].childlist)
                chartRecyclerView.adapter=adapter
                chartRecyclerView.layoutManager=LinearLayoutManager(this.context)
            }
        }
        if(!isDataAvilable){
            chartdatatext.visibility=View.VISIBLE
            myPieChart.visibility=View.GONE
            chartRecyclerView.visibility=View.GONE
        }
    }


    private fun getPercentageData(list: ArrayList<DataModel>) {
        var sum=0.0
        for (i in list.indices){
            if(list[i].category!="Income"){
                val jk= list[i].amount?.toDouble()
                if (jk != null) {
                    sum+=jk
                }
            }
        }
        val categoryTextlist=ArrayList<String>()
        for (i in list.indices){
            if(list[i].category!="Income"){
                if(!categoryTextlist.contains(list[i].category)){
                    val kk=list[i].amount?.toDouble()
                    if (kk != null) {
                        val result=(kk*100/sum).toFloat()
                        percentlist.add(DataModel(0,result.toString(),list[i].category,null,null,0,null,0))
                    }
                    categoryTextlist.add(list[i].category!!)
                }else{
                    for (k in percentlist.indices){
                        if(percentlist[k].category==list[i].category){
                            val num1 =percentlist[k].amount!!.toDouble()
                            val kk=list[i].amount?.toDouble()
                            if (kk != null) {
                                val result= (kk*100/sum)
                                percentlist[k].amount=(num1+result).toString()
                            }
                        }

                    }
                }
            }
        }

    }


    fun initPieChart() {
        myPieChart.setUsePercentValues(true)
        myPieChart.description.text = ""
        //hollow pie chart
        myPieChart.isDrawHoleEnabled = false
        myPieChart.setTouchEnabled(false)
        myPieChart.setDrawEntryLabels(false)
        //adding padding
        myPieChart.setExtraOffsets(20f, 0f, 20f, 5f)
        myPieChart.setUsePercentValues(true)
        myPieChart.isRotationEnabled = false
        myPieChart.setDrawEntryLabels(false)
        myPieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        myPieChart.legend.isWordWrapEnabled = true
    }

    fun setDatatoPieChart() {
        myPieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()

        val colors: ArrayList<Int> = ArrayList()
        var count=1
        var total=""
        for (i in percentlist.indices){
            val category=percentlist[i].category.toString()
            dataEntries.add(PieEntry(percentlist[i].amount!!.toFloat(),category))
            //for coresponding color
            for (j in cArray.indices){
                if(cArray[j]==category){
                    this.context?.let { ContextCompat.getColor(it,colorArray[j]) }
                        ?.let { colors.add(it) }
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
        myPieChart.data = data
        data.setValueTextSize(15f)
        myPieChart.setExtraOffsets(5f, 5f, 5f, 5f)
        myPieChart.animateY(1400, Easing.EaseInOutQuad)

        //create hole in center
        myPieChart.holeRadius = 58f
        myPieChart.transparentCircleRadius = 61f
        myPieChart.isDrawHoleEnabled = true
        myPieChart.setHoleColor(Color.WHITE)


        //add text in center
        myPieChart.setDrawCenterText(true);
        myPieChart.centerText = "Expense"



        myPieChart.invalidate()
    }


}
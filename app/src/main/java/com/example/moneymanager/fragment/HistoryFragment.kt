package com.example.moneymanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.*
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.model.ParentItemModel
import com.example.moneymanager.viewmodel.MainViewModel
import com.example.moneymanager.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_history.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HistoryFragment : Fragment() {

    var parentlist=ArrayList<ParentItemModel>()
    var parentlistformonth=ArrayList<ParentItemModel>()
    lateinit var adapter: ParentRecyclerViewAdapter
    lateinit var adapterPercent: PercentRecyclerViewAdpter
    val cArray= arrayOf("Shopping","Food and Drink","Transport or Fuel",
        "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
    val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)
    val nameOfMonth= arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")

    lateinit var viewModel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
//        list = this.arguments?.getParcelableArrayList<DataModel>("list") as ArrayList<DataModel>
//
//        makeParentList(list,false)
//        makemonthlist()
        val repository = (activity?.application as ExpenceApplication).repository
        viewModel=ViewModelProvider(requireActivity(), ViewModelFactory(repository))[MainViewModel::class.java]

        viewModel.parentlivelist.observe(viewLifecycleOwner){
            parentlist=it
            adapter= ParentRecyclerViewAdapter(parentlist)
            dailyRecyclerView.adapter=adapter
            dailyRecyclerView.layoutManager = LinearLayoutManager(this.context)
        }

        
        val sdata= arrayOf("Daily","Monthly","Year")
        val context=this.context
        val sAdapter =ArrayAdapter<String>(context!!,android.R.layout.simple_list_item_1,sdata)
        mySpinner.adapter=sAdapter

        viewModel.monthlivelist.observe(viewLifecycleOwner){
            parentlistformonth=it
            mySpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when {
                        sdata[position]=="Daily" -> {
                            monthRecyclerView.visibility=View.GONE
                            listview.visibility=View.GONE
                            mlin.visibility=View.GONE
                            monthTotal.visibility=View.GONE
                            datatext.visibility=View.GONE
                            ctText.visibility=View.GONE
                            dailyRecyclerView.visibility=View.VISIBLE
                        }
                        sdata[position]=="Monthly" -> {
                            dailyRecyclerView.visibility=View.GONE
                            listview.visibility=View.GONE
                            mlin.visibility=View.VISIBLE
                            count=0
                            manageMonthData()

                        }
                        else -> {   //Yearly
                            monthRecyclerView.visibility=View.GONE
                            datatext.visibility=View.GONE
                            ctText.visibility=View.VISIBLE
                            dailyRecyclerView.visibility=View.GONE
                            mlin.visibility=View.VISIBLE
                            monthTotal.visibility=View.VISIBLE
                            listview.visibility=View.VISIBLE

                            count=0
                            manageYearData()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
            previous1.setOnClickListener {
                if(mySpinner.selectedItem=="Monthly"){
                    if(indexOfMonth!=0){
                        indexOfMonth--
                    }
                    else{
                        indexOfMonth=11
                        year--
                    }

                    manageMonthData()
                }else if (mySpinner.selectedItem=="Year"){
                    year2--
                    manageYearData()
                }


            }
            next1.setOnClickListener {
                if(mySpinner.selectedItem=="Monthly"){
                    if(indexOfMonth!=11){
                        indexOfMonth++
                    }
                    else{
                        indexOfMonth=0
                        year++
                    }
                    manageMonthData()
                }else if (mySpinner.selectedItem=="Yearly"){
                    year2++
                    manageYearData()
                }


            }
        }

    }
    private fun manageYearData(){

        if(count==0){
            var c=Calendar.getInstance()
            val format=SimpleDateFormat("dd/MM/yyyy")
            year2=format.format(c.time).substring(6,10).toInt()
        }
        count++


        var total=0.0
        val yearlist=ArrayList<HashMap<String,String>>()
        var isdataAvailable=false

        for (j in nameOfMonth.indices){
            val hashmap=HashMap<String,String>()
            var amount="0"
            for (i in parentlistformonth.indices){
                if(year2.toString()==parentlistformonth[i].day.substring(4,8)){
                    if(nameOfMonth[j].substring(0,3)==parentlistformonth[i].day.substring(0,3)){
                        amount=parentlistformonth[i].total
                        total+=amount.toDouble()
                        isdataAvailable=true
                    }

                }

            }

            hashmap["month"] = nameOfMonth[j]
            hashmap["amount"] = amount
            yearlist.add(hashmap)
        }

        if(isdataAvailable){
            monthTotal.text="${getString(R.string.Rs)} $total"
            month1.text="$year2"
            val listadapter=SimpleAdapter(context,yearlist,R.layout.listview_item_view,
                arrayOf("month","amount"), intArrayOf(R.id.mname,R.id.mamount))
            listview.adapter=listadapter
        }
        else{
            datatext.visibility=View.VISIBLE
            monthTotal.visibility=View.GONE
            ctText.visibility=View.GONE
            listview.visibility=View.GONE
        }

    }
    var indexOfMonth=0
    var count=0
    private var year=0
    private var year2=0

    //This fun take parentlist of monthly data then it arrange it with Specific month then show it
    private fun manageMonthData(){

        if(count==0){
            var c=Calendar.getInstance()
            val format=SimpleDateFormat("dd/MM/yyyy")
            val month2=format.format(c.time).substring(3,5)
            year=format.format(c.time).substring(6,10).toInt()
            indexOfMonth=month2.toInt()-1

        }
        count++
        var isdataAvailable=false
        month1.text="${nameOfMonth[indexOfMonth].substring(0,3)} $year"
        for(i in parentlistformonth.indices){
            if(parentlistformonth[i].day ==month1.text.toString()){
                monthTotal.text="${getString(R.string.Rs)} ${parentlistformonth[i].total}"
                adapterPercent= PercentRecyclerViewAdpter(parentlistformonth[i].childlist)
                monthRecyclerView.adapter=adapterPercent
                monthRecyclerView.layoutManager=LinearLayoutManager(context)

                isdataAvailable=true
                monthRecyclerView.visibility=View.VISIBLE
                monthTotal.visibility=View.VISIBLE
                ctText.visibility=View.VISIBLE
                datatext.visibility=View.GONE
            }
        }
        if(!isdataAvailable){
            datatext.visibility=View.VISIBLE
            monthRecyclerView.visibility=View.GONE
            monthTotal.visibility=View.GONE
            ctText.visibility=View.GONE
        }


    }
}
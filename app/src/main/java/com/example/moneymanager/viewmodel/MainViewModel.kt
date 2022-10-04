package com.example.moneymanager.viewmodel

import androidx.lifecycle.*
import com.example.moneymanager.R
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.model.ParentItemModel
import com.example.moneymanager.room.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val repository: MyRepository) :ViewModel() {
    var mainlivelist =MutableLiveData<ArrayList<DataModel>>()
    var parentlivelist =MutableLiveData<ArrayList<ParentItemModel>>()
    var monthlivelist =MutableLiveData<ArrayList<ParentItemModel>>()
    var percentlivelist =MutableLiveData<ArrayList<DataModel>>()
    var mainlist=ArrayList<DataModel>()
    var parentlist=ArrayList<ParentItemModel>()
    var monthlist=ArrayList<ParentItemModel>()
    var percentlist=ArrayList<DataModel>()
    var currency=MutableLiveData<String>()


    init {
        currency.value="$"
        //getAllData()
        //makedemolist()
        updatedData()
    }

    fun saveData(dataModel: DataModel){
        mainlist=ArrayList()
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(dataModel)
        }
    }

    fun getAllData():LiveData<List<DataModel>> {
        return repository.getallData()
    }

    private fun makedemolist() {
        mainlist.add(
            DataModel(0,
                "22",
                "Shopping",
                "yyf",
                "09/05/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_shopping
            )
        )
        mainlist.add(
            DataModel(0,
                "100",
                "Shopping",
                "frt",
                "08/05/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_shopping
            )
        )

        mainlist.add(
            DataModel(0,
                "59",
                "Shopping",
                "tshirt",
                "01/04/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_food
            )
        )
        mainlist.add(
            DataModel(0,
                "130",
                "Transport or Fuel",
                "tshirt",
                "03/04/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_shopping
            )
        )

        mainlist.add(
            DataModel(0,
                "500",
                "Transport or Fuel",
                "tshirt",
                "01/03/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_shopping
            )
        )

        mainlist.add(
            DataModel(0,
                "100",
                "Transport or Fuel",
                "tshirt",
                "20/03/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_shopping
            )
        )
        mainlist.add(
            DataModel(0,
                "50",
                "Food and Drink",
                "tshirt",
                "21/03/2022",
                R.drawable.cash,
                "Expense",
                R.drawable.ic_food
            )
        )
    }
    fun updatedData(){
        parentlist=ArrayList()
        monthlist=ArrayList()
        percentlist=ArrayList()

        makeParentList(mainlist,false)
        makemonthlist()
        getPercentageData()
        mainlivelist.value=mainlist
        parentlivelist.value=parentlist
        monthlivelist.value=monthlist
        percentlivelist.value=percentlist
    }

    private fun makeParentList(clist: ArrayList<DataModel>, newDataAdd:Boolean) {
        val date=ArrayList<String>()
        for(i in clist.indices){
            if(!date.contains(clist[i].date)){
                date.add(clist[i].date.toString())
            }
        }
        for (i in date.indices){
            val rlist=ArrayList<DataModel>()
            var totalamount=0.0
            for (j in clist.indices){
                if(date[i].contains(clist[j].date.toString())){
                    rlist.add(clist[j])
                }
            }
            for (i in rlist.indices){
                totalamount+= rlist[i].amount!!.toDouble()
            }
            if(!newDataAdd){
                parentlist.add(ParentItemModel(date[i],"total : ${currency.value} $totalamount",rlist))
            }
            else{
                parentlist.add(0,ParentItemModel(date[i],"total :${currency.value} $totalamount",rlist))
            }

        }

    }
    private fun getPercentageData(){
        percentlist=ArrayList()
        var sum=0.0
        for (i in mainlist.indices){
            if(mainlist[i].category!="Income"){
                val jk= mainlist[i].amount?.toDouble()
                if (jk != null) {
                    sum+=jk
                }
            }
        }
        val categoryTextlist=ArrayList<String>()
        for (i in mainlist.indices){
            if(mainlist[i].category!="Income"){
                if(!categoryTextlist.contains(mainlist[i].category)){
                    val kk=mainlist[i].amount?.toDouble()
                    if (kk != null) {
                        val result=(kk*100/sum).toFloat()
                        percentlist.add(DataModel(0,result.toString(),mainlist[i].category,null,null,0,null,0))
                    }
                    categoryTextlist.add(mainlist[i].category!!)
                }else{
                    for (k in percentlist.indices){
                        if(percentlist[k].category==mainlist[i].category){
                            val num1 =percentlist[k].amount!!.toDouble()
                            val kk=mainlist[i].amount?.toDouble()
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
    private fun makemonthlist() {

        val list= ArrayList<Int>()
        list.add(1)
        list.add(1)
        list.add(1)
        list.add(1)
        var list2= ArrayList<Int>()
        for(i in list.indices){
            list2.add(list[i])
        }
        //list2.addAll(list)
        list[1]=50
        var somelist=ArrayList<DataModel>()
        somelist=mainlist.clone() as ArrayList<DataModel>
        val date = ArrayList<String>()
        for (i in somelist.indices) {
            if(somelist[i].category!="Income"){
                if (!date.contains(somelist[i].date!!.substring(3, 9))) {
                    date.add(somelist[i].date!!.substring(3, 9))
                }
            }

        }

        for (i in date.indices) {

            var rlist = ArrayList<DataModel>()
            val categoryTextlist=ArrayList<String>()
            for (j in somelist.indices) {
                if(somelist[j].category!="Income"){
                    if (date[i]==somelist[j].date!!.substring(3, 9)) {
                        if(!categoryTextlist.contains(somelist[j].category)){
                            categoryTextlist.add(somelist[j].category.toString())
                            rlist.add(somelist[j])
                        }
                        else{
                            for (k in rlist.indices){
                                if(categoryTextlist[k] == somelist[j].category){
                                    val num1 =rlist[k].amount!!.toDouble()
                                    val num2 =somelist[j].amount!!.toDouble()
                                    val sum=num1+num2
                                    rlist.set(k,DataModel(0,sum.toString(),somelist[j].category,somelist[j].description,somelist[j].date,somelist[j].paymethod,somelist[j].type,somelist[j].logoid))

                                }

                            }
                        }
                    }
                }
            }
            if(rlist.size>0){
                val day=rlist[0].date!!.substring(0,2).toInt()
                val month=rlist[0].date!!.substring(3,5).toInt()          //month 0 is jan and 1 is feb
                val year=rlist[0].date!!.substring(6,rlist[0].date!!.length).toInt()
                val c= Calendar.getInstance()
                c.set(year,month-1,day)
                val format= SimpleDateFormat("MMM yyyy")
                val text=format.format(c.time)
                var sum=0.0
                for (j in rlist.indices){
                    sum+=rlist[j].amount!!.toDouble()
                }
                var even = if(sum.toString().contains('.')){
                    val index=sum.toString().indexOf('.')
                    if(index+2<sum.toString().length){
                        sum.toString().substring(0,index+2)
                    } else{
                        sum.toString()
                    }
                } else{
                    sum.toString()
                }
                monthlist.add(ParentItemModel(text,even,rlist))
            }

        }
    }
}
package com.example.moneymanager.viewmodel

import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanager.R
import com.example.moneymanager.RecyclerViewAdapter
import com.example.moneymanager.model.CategoryModel
import com.example.moneymanager.model.DataModel
import kotlinx.android.synthetic.main.activity_add_transaction.*
import java.util.*
import kotlin.collections.ArrayList

class TransactionViewModel :ViewModel() {


    companion object {
        private var somelist=ArrayList<DataModel>()
        var isaddClick=false
        var calculationResult:Double= 0.0
        var lastOperatorUsed="nothing"
        var prevCalculation="jiban"
        var isplusOrMinusClick =false
        val categoryName= arrayOf("Shopping","Food and Drink","Transport or Fuel",
            "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
        val categoryImgid= intArrayOf(R.drawable.ic_shopping,R.drawable.ic_food,R.drawable.ic_transport,
            R.drawable.ic_health,R.drawable.ic_food,R.drawable.ic_transport,
            R.drawable.ic_health,R.drawable.ic_food,R.drawable.ic_transport,R.drawable.ic_health)
        val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)

        val cashlist= arrayOf("Cash","UPI","card")
        var calendar= Calendar.getInstance()

    }

    var somelistLiveData=MutableLiveData<ArrayList<DataModel>>()


    fun doCalculation(toggle_Id:Int,catSpinnerSelectedItemPosition:Int,paySpinnerSelecteItem:String,
    money:String,desc:String,date:String){
        val exText = if(toggle_Id== R.id.expense){
            "Expense"
        } else{
            "Income"
        }

        val catName=if (exText=="Expense"){
            categoryName[catSpinnerSelectedItemPosition]
        } else{
            "Income"
        }
        val catimgid=if(catName=="Income"){
            R.drawable.cash
        }
        else{
            categoryImgid[catSpinnerSelectedItemPosition]
        }
        val imgId=when(paySpinnerSelecteItem){
            "Cash" -> R.drawable.cash
            "card" -> R.drawable.card
            "UPI" -> R.drawable.upi
            else -> R.drawable.cash
        }
        val mydate=date
        if(money.isEmpty()||money.toString()=="0"){
//            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show()
        }
        else if(mydate.length==10 && mydate.substring(3,4)=="/" && isNumber(mydate.substring(4,6))){
//            Toast.makeText(this, "Please enter Date correctly", Toast.LENGTH_SHORT).show()
        }
        else{
            somelist.add(0,
                DataModel(0,money,catName, desc,date,imgId,exText,catimgid)
            )
        }
        somelistLiveData.value= somelist
//        val inputMethodManager= getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(transaction_layout.windowToken,0)
    }

    private fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }

}
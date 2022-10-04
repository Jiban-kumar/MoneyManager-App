package com.example.moneymanager

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.model.CategoryModel
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.add
import kotlinx.android.synthetic.main.activity_add_transaction.add_date
import kotlinx.android.synthetic.main.activity_add_transaction.add_toggle
import kotlinx.android.synthetic.main.activity_add_transaction.addrecyclerview
import kotlinx.android.synthetic.main.activity_add_transaction.categorySpinner
import kotlinx.android.synthetic.main.activity_add_transaction.description
import kotlinx.android.synthetic.main.activity_add_transaction.paymethodSpinner
import kotlinx.android.synthetic.main.mykeyboard_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTransactionActivity : AppCompatActivity() {

    companion object {
        private var somelist=ArrayList<DataModel>()
        var isaddClick=false
        var calculationResult:Double= 0.0
        var lastOperatorUsed="nothing"
        var prevCalculation="jiban"
        var isplusOrMinusClick =false
        lateinit var adapter: RecyclerViewAdapter
        val categoryName= arrayOf("Shopping","Food and Drink","Transport or Fuel",
            "Grossory","Bills and Recharge","Rent","Health","Entertainment","Insurance","gift")
        val categoryImgid= intArrayOf(R.drawable.ic_shopping,R.drawable.ic_food,R.drawable.ic_transport,
            R.drawable.ic_health,R.drawable.ic_food,R.drawable.ic_transport,
            R.drawable.ic_health,R.drawable.ic_food,R.drawable.ic_transport,R.drawable.ic_health)
        val colorArray= intArrayOf(R.color.c1,R.color.c2,R.color.c3,R.color.c4,R.color.c5,R.color.c6,R.color.c7,R.color.c8,R.color.c9,R.color.c10)

        val cashlist= arrayOf("Cash","UPI","card")
        var calendar=Calendar.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val list=ArrayList<CategoryModel>()
        for (i in categoryName.indices){
            list.add(CategoryModel(categoryImgid[i],categoryName[i],colorArray[i]))
        }

        val cadapter= SpinnerAdapter(this,list)
        val dadapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,cashlist)
        categorySpinner.adapter=cadapter
        paymethodSpinner.adapter=dadapter
        addrecyclerview.visibility=View.GONE

        add_date.setText(SimpleDateFormat("dd/MM/yyyy").format(calendar.time).toString())


        add.setOnClickListener {
            doCalculation()
            addrecyclerview.visibility=View.VISIBLE
            if(somelist.size>1){
                adapter.notifyItemInserted(0)
                addrecyclerview.scrollToPosition(0) //By this you can define recyclerview position
            }
            else{
                adapter= RecyclerViewAdapter(somelist)
                addrecyclerview.adapter=adapter
                addrecyclerview.layoutManager= LinearLayoutManager(this)
                addrecyclerview.setHasFixedSize(true)
            }
            add_amount.text= null
            description.text=null
            isaddClick=true
            Toast.makeText(this, "add clicked", Toast.LENGTH_SHORT).show()
        }
        ok.setOnClickListener {
            val intent=Intent()
            if(!isaddClick){
                doCalculation()
            }
            intent.putExtra("data", somelist)
            setResult(Activity.RESULT_OK,intent)
            somelist= ArrayList()
            this.finish()

        }
        income.setOnClickListener {
            categorySpinner.isEnabled=false
        }
        expense.setOnClickListener {
            categorySpinner.isEnabled=true
        }

        calenderBtn.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog(this,DatePickerDialog.OnDateSetListener{datePicker, year, month, day ->

                    calendar[Calendar.YEAR]=year
                    calendar[Calendar.MONTH]=month
                    calendar[Calendar.DAY_OF_MONTH]=day
                    add_date.setText("$day/$month/$year")
                },calendar[Calendar.YEAR],calendar[Calendar.MONTH],calendar[Calendar.DAY_OF_MONTH])
                    .show()
            } else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show()
            }
        }
        close.setOnClickListener {
            somelist=ArrayList()
            finish()
        }
    }


    private fun doCalculation(){
        val exText = if(add_toggle.checkedButtonId==R.id.expense){
            "Expense"
        } else{
            "Income"
        }

        val catName=if (exText=="Expense"){
            categoryName[categorySpinner.selectedItemPosition]
        } else{
            "Income"
        }
        val catimgid=if(catName=="Income"){
            R.drawable.cash
        }
        else{
            categoryImgid[categorySpinner.selectedItemPosition]
        }
        val imgId=when(paymethodSpinner.selectedItem.toString()){
            "Cash" ->R.drawable.cash
            "card" ->R.drawable.card
            "UPI" ->R.drawable.upi
            else -> R.drawable.cash
        }
        val mydate=add_date.text.toString()
        if(add_amount.text.isEmpty()||add_amount.text.toString()=="0"){
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show()
        }
        else if(mydate.length==10 && mydate.substring(3,4)=="/" && isNumber(mydate.substring(4,6))){
            Toast.makeText(this, "Please enter Date correctly", Toast.LENGTH_SHORT).show()
        }
        else{
            somelist.add(0,
                DataModel(0,add_amount.text.toString(),catName,description.text.toString(),add_date.text.toString(),imgId,exText,catimgid)
            )
        }

        val inputMethodManager= getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(transaction_layout.windowToken,0)
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
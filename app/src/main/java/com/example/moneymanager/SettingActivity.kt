package com.example.moneymanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanager.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.currency_popup.*

class SettingActivity : AppCompatActivity() {
    lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        viewModel=ViewModelProvider(this)[MainViewModel::class.java]
        rel1.setOnClickListener {
            profilerel.visibility=View.VISIBLE

        }
        usave.setOnClickListener {
            profilerel.visibility=View.GONE
            val name=uname.text
            val email=uemail.text
            Toast.makeText(this, "$name $email Saved", Toast.LENGTH_SHORT).show()
        }
        ucancel.setOnClickListener {
            profilerel.visibility=View.GONE
        }
        darktheme.setOnCheckedChangeListener { compoundButton, ischecked ->
            if(ischecked){
                Toast.makeText(this, "Dark theme on", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "White theme on", Toast.LENGTH_SHORT).show()
            }
        }
        changeCurrency.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.currency_popup, null,false)
            val dialogbuilder = AlertDialog.Builder(this)
            dialogbuilder.setView(view)
            val popCancel=view.findViewById<Button>(R.id.popCancel)
            val popSave=view.findViewById<Button>(R.id.popSave)
            val rgroup=view.findViewById<RadioGroup>(R.id.rgroup)
            val dialog=dialogbuilder.create()
            dialog.show()
            popCancel.setOnClickListener {
                dialog.dismiss()
            }
            popSave.setOnClickListener {
                when(rgroup.checkedRadioButtonId){
                    R.id.rupee -> {
                        moneytext.text=resources.getString(R.string.Rs)
                    }
                    R.id.dollar ->moneytext.text="$"
                    R.id.euro ->moneytext.text=resources.getString(R.string.euro)
                    R.id.pound ->moneytext.text=resources.getString(R.string.pound)
                    R.id.yen ->moneytext.text=resources.getString(R.string.yen)
                    else ->moneytext.text=resources.getString(R.string.Rs)
                }
                viewModel.currency.value=moneytext.text.toString()
                viewModel.updatedData()
                dialog.dismiss()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
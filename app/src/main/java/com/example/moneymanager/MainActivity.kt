package com.example.moneymanager

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanager.fragment.ChartFragment
import com.example.moneymanager.fragment.HistoryFragment
import com.example.moneymanager.fragment.HomeFragment
import com.example.moneymanager.model.DataModel
import com.example.moneymanager.room.ExpenseDatabase
import com.example.moneymanager.room.MyRepository
import com.example.moneymanager.viewmodel.MainViewModel
import com.example.moneymanager.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var  toggle:ActionBarDrawerToggle
    lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toggle= ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()


        //makedemolist()
        val repository=(application as ExpenceApplication).repository
        viewModel=ViewModelProvider(this,ViewModelFactory(repository))[MainViewModel::class.java]
        homeFragment= HomeFragment()
        chartFragment= ChartFragment()
        historyFragment= HistoryFragment()

        viewModel.getAllData().observe(this, Observer {

            viewModel.mainlist.addAll(it)
            viewModel.updatedData()
        })
        viewModel.mainlivelist.observe(this){
            //Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
            replaceFragment(homeFragment)
            bottomnav.setOnItemSelectedListener{
                when(it.itemId){
                    R.id.home ->{
                        replaceFragment(homeFragment)
                    }
                    R.id.chart ->{
                        replaceFragment(chartFragment)
                    }
                    R.id.history ->{
                        replaceFragment(historyFragment)
                    }
                }
                return@setOnItemSelectedListener true
            }
        }



        add_transaction.setOnClickListener {
            val intent=Intent(this,AddTransactionActivity::class.java)
            resultLauncher.launch(intent)
        }

    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: ArrayList<DataModel> = result.data?.getParcelableArrayListExtra<DataModel>("data") as ArrayList<DataModel>
            for (i in data.indices.reversed()){
                //viewModel.mainlist.add(0,data[i])
                viewModel.saveData(data[i])
            }
//            viewModel.updatedData()
            //homeFragment.addmoreDatatoHF(data)
        }
    }

    lateinit var  homeFragment:HomeFragment
    lateinit var chartFragment:ChartFragment
    lateinit var historyFragment:HistoryFragment

    private fun replaceFragment(fragment: Fragment){
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout,fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            toggle.onOptionsItemSelected(item) -> {
                return true
            }
            item.itemId==R.id.calenderMain -> {
                val intent=Intent(this,SettingActivity::class.java)
                startActivity(intent)
            }
            item.itemId==R.id.help -> {
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
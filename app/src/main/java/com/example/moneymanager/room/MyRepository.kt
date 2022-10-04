package com.example.moneymanager.room

import androidx.lifecycle.LiveData
import com.example.moneymanager.model.DataModel

class MyRepository(private val expenseDao: ExepenseDao) {



    suspend fun insertData(dataModel: DataModel){
        expenseDao.insertData(dataModel)
    }
    fun getallData():LiveData<List<DataModel>>{
        return expenseDao.getAllData()
    }
}
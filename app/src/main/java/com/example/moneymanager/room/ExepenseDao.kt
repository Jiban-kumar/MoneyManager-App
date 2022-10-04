package com.example.moneymanager.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.moneymanager.model.DataModel

@Dao
interface ExepenseDao {

    @Insert
    suspend fun insertData(dataModel: DataModel)

    @Query("SELECT * FROM dataModel")
    fun getAllData():LiveData<List<DataModel>>
}
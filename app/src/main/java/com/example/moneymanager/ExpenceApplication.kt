package com.example.moneymanager

import android.app.Application
import com.example.moneymanager.room.ExpenseDatabase
import com.example.moneymanager.room.MyRepository

class ExpenceApplication : Application() {
    lateinit var repository: MyRepository
    override fun onCreate() {
        super.onCreate()
        initializer()
    }

    private fun initializer() {
        val dataBase= ExpenseDatabase.getDatabase(this)
        repository= MyRepository(dataBase.expenceDao())
    }
}
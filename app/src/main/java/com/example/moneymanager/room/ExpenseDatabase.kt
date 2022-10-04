package com.example.moneymanager.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneymanager.model.DataModel

@Database(entities = [DataModel::class], version = 1)
abstract class ExpenseDatabase :RoomDatabase(){

    abstract fun expenceDao():ExepenseDao

    companion object{
        @Volatile
        private var instance:ExpenseDatabase?= null

        fun getDatabase(context: Context):ExpenseDatabase{
            if (instance == null){
                synchronized(this){
                    instance = Room.databaseBuilder(context.applicationContext, ExpenseDatabase::class.java, "expenceDB")
                        .build()
                }

            }
            return instance!!
        }
    }
}
package com.example.moneymanager.model

import com.example.moneymanager.model.DataModel

data class ParentItemModel(
    val day:String,
    val total:String,
    val childlist:ArrayList<DataModel>
)

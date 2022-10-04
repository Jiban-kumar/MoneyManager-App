package com.example.moneymanager.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "dataModel")
data class DataModel(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var amount:String? = null,
    val category:String? = null,
    val description:String? = null,
    val date:String? = null,
    val paymethod:Int,
    val type:String? = null,
    val logoid:Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(amount)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeInt(paymethod)
        parcel.writeString(type)
        parcel.writeInt(logoid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }


}

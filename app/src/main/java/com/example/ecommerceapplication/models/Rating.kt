package com.example.ecommerceapplication.models

import android.os.Parcel
import android.os.Parcelable

class Rating () : Parcelable{

    var rate : Double = 0.0
    var count : Int = 1
    constructor(parcel: Parcel) : this() {
        rate = parcel.readDouble()
        count = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(rate)
        parcel.writeInt(count)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rating> {
        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }


}
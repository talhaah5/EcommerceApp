package com.example.ecommerceapplication.models
import android.os.Parcel
import android.os.Parcelable

class Address () : Parcelable{

    var city : String = ""
    var street : String = ""
    var number : String = ""
    var zipcode : String = ""
    var lat : Double = 0.0
    var lng : Double = 0.0

    constructor(parcel: Parcel) : this() {
        city = parcel.readString().toString()
        street = parcel.readString().toString()
        number = parcel.readString().toString()
        zipcode = parcel.readString().toString()
        lat = parcel.readDouble()
        lng = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(street)
        parcel.writeString(number)
        parcel.writeString(zipcode)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }


}
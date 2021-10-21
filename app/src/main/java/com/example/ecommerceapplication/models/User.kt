package com.example.ecommerceapplication.models

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {

    var id: String = ""
    var email: String = ""
    var username: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var phoneNumber: String = ""
    var token: String = ""
    var password: String = ""
    var address:Address = Address()

    fun User() {

    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        firstName = parcel.readString().toString()
        lastName = parcel.readString().toString()
        email = parcel.readString().toString()
        username = parcel.readString().toString()
        phoneNumber = parcel.readString().toString()
        password = parcel.readString().toString()
        address = parcel.readParcelable(Address::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(phoneNumber)
        parcel.writeString(password)
        parcel.writeParcelable(address, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        fun cleanNumber(num: String): String {
            var ph = num.replace(")", "")
            ph = ph.replace("(", "")
            ph = ph.replace(" ", "")
            ph = "+1" + ph.replace("-", "")

            return ph
        }


    }



}
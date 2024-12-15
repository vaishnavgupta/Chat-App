package com.example.chatapptrial.modal

import android.os.Parcel
import android.os.Parcelable

data class Users(
    val uid:String?="",
    val userName:String?="",
    val userEmail:String?="",
    val status:String?="",
    val imgUrl:String?=""

) : Parcelable{                           // for sending data to differnt activities
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(uid)
        p0.writeString(userName)
        p0.writeString(userEmail)
        p0.writeString(imgUrl)
        p0.writeString(status)
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }

}

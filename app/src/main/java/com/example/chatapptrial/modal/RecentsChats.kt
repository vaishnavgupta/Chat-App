package com.example.chatapptrial.modal

import android.os.Parcel
import android.os.Parcelable

data class RecentsChats(
    val friendId:String?="",
    val friendName:String?="",
    val message:String?="",
    val time:String?="",
    val friendImg:String?="",
    val sender:String?="",
    val person:String?="",
    val status:String?=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(friendId)
        parcel.writeString(friendName)
        parcel.writeString(message)
        parcel.writeString(time)
        parcel.writeString(friendImg)
        parcel.writeString(sender)
        parcel.writeString(person)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecentsChats> {
        override fun createFromParcel(parcel: Parcel): RecentsChats {
            return RecentsChats(parcel)
        }

        override fun newArray(size: Int): Array<RecentsChats?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.chatapptrial

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

class Utils {

    companion object{
        private val auth=FirebaseAuth.getInstance()
        private var userId:String=""

        //using the fun to get user id anywhwre

        fun getCurrUserId():String{
            if(auth.currentUser!=null){
                userId= auth.currentUser!!.uid
            }
            return userId
        }

        fun getCurrTime(): String {
            val formatter=SimpleDateFormat("HH:mm:ss")
            val date:Date=Date(System.currentTimeMillis())
            val strdate=formatter.format(date)
            return strdate
        }

    }
}
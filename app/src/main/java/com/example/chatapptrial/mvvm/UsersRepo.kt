package com.example.chatapptrial.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapptrial.Utils
import com.example.chatapptrial.modal.Users
import com.google.firebase.firestore.FirebaseFirestore

class UsersRepo {


    private val firestore=FirebaseFirestore.getInstance()
    fun getUsers():LiveData<List<Users>>{
        val listUsers= MutableLiveData<List<Users>>()

        firestore.collection("Users").addSnapshotListener { snapshot, error ->
            if(error!=null){
                return@addSnapshotListener
            }

            val userList= mutableListOf<Users>()
            snapshot?.documents?.forEach { docs->
                val user=docs.toObject(Users::class.java)

                //preventing the currentLoggedInUser to appear in the user list

                if(user!!.uid!=Utils.getCurrUserId()){
                    user.let {
                        userList.add(it)
                    }
                }
            }
            listUsers.value=userList

        }
        return listUsers
    }
}
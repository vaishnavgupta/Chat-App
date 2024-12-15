package com.example.chatapptrial.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapptrial.Utils
import com.example.chatapptrial.modal.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MessageRepo {

    private val firestore = FirebaseFirestore.getInstance()

    fun getMessages(friendID: String): LiveData<List<Message>> {
        val listMessages = MutableLiveData<List<Message>>()

        //taking unique id
        val uniqueId = listOf(Utils.getCurrUserId(), friendID).sorted()
        uniqueId.joinToString(separator = " ")

        firestore.collection("Messages").document(uniqueId.toString()).collection("chats")
            .orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
                val msgList = mutableListOf<Message>()
                if (value!!.documents != null) {
                    value.documents.forEach { docx ->
                        val msgModel = docx.toObject(Message::class.java)
                        if (msgModel!!.sender.equals(Utils.getCurrUserId()) && msgModel!!.reciever.equals(
                                friendID
                            ) ||
                            msgModel!!.sender.equals(friendID) && msgModel!!.reciever.equals(Utils.getCurrUserId())
                        ) {

                            msgModel.let {
                                msgList.add(it)
                            }
                        }

                    }
                }
                listMessages.value=msgList


        }
        return listMessages
    }
}
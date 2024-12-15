package com.example.chatapptrial.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapptrial.Utils
import com.example.chatapptrial.modal.RecentsChats
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatListRepo {

    private val firestore = FirebaseFirestore.getInstance()

    fun getRecentChatsList():LiveData<List<RecentsChats>>{
        val listRecentChat=MutableLiveData<List<RecentsChats>>()

        val uniqueId="Conversations"+Utils.getCurrUserId()
        firestore.collection(uniqueId).orderBy("time",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                return@addSnapshotListener
            }
            val recentChatList= mutableListOf<RecentsChats>()
            value!!.documents.forEach { docx->
                val recChatModel=docx.toObject(RecentsChats::class.java)
                if(recChatModel!!.sender.equals(Utils.getCurrUserId())){
                    recChatModel.let {
                        recentChatList.add(it)
                    }
                }

            }


            listRecentChat.value=recentChatList
        }
        return listRecentChat
    }
}
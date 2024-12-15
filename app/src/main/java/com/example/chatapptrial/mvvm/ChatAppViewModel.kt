package com.example.chatapptrial.mvvm

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapptrial.MyApplication
import com.example.chatapptrial.SharedPrefs
import com.example.chatapptrial.Utils
import com.example.chatapptrial.modal.Message
import com.example.chatapptrial.modal.RecentsChats
import com.example.chatapptrial.modal.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatAppViewModel: ViewModel() {
    val name=MutableLiveData<String>()
    val imgUrl=MutableLiveData<String>()
    val message=MutableLiveData<String>()
    private val firestore=FirebaseFirestore.getInstance()

    val userRepo=UsersRepo()
    val messageRepo=MessageRepo()
    val chatListRepo=ChatListRepo()

    init {

        getCurrUser()
    }

    //getting all from repository  which ultimately does the main work
    fun getUsers():LiveData<List<Users>>{
        return userRepo.getUsers()
    }

    //get current user
    fun getCurrUser()=viewModelScope.launch(Dispatchers.IO) {

        val context=MyApplication.instance.applicationContext
        firestore.collection("Users").document(Utils.getCurrUserId()).addSnapshotListener { value, error ->

            if(value!!.exists() && value!=null){
                val users=value.toObject(Users::class.java)
                name.value=users?.userName!!
                imgUrl.value=users?.imgUrl!!

                val mySharedPrefs=SharedPrefs(context)
                mySharedPrefs.setValue("username",users.userName)

            }
        }
    }


    //Sending message
    fun sendMessage(sender:String,reciever:String,friendName:String,friendImg:String)=viewModelScope.launch(Dispatchers.IO){
        val context=MyApplication.instance.applicationContext

        val msgHashMap= hashMapOf<String,Any>(
            "sender" to sender, "reciever" to reciever, "message" to message.value!!,"time" to Utils.getCurrTime()
        )

        val uniqueId= listOf(sender,reciever).sorted()
        uniqueId.joinToString(separator = " ")

        val friendNameSplitted=friendName.split("\\s".toRegex())[0]

        val mysharedPrefs = SharedPrefs(context)
        mysharedPrefs.setValue("friendid", reciever)
        mysharedPrefs.setValue("chatroomid", uniqueId.toString())
        mysharedPrefs.setValue("friendname", friendNameSplitted)
        mysharedPrefs.setValue("friendimage", friendImg)

        firestore.collection("Messages").document(uniqueId.toString()).collection("chats").document(Utils.getCurrTime()).set(msgHashMap).addOnCompleteListener { t->
            if(t.isSuccessful){

                //for recent chats
                val recentHashMap= hashMapOf<String,Any>(
                    "friendId" to reciever,
                    "time" to Utils.getCurrTime(),
                    "sender" to Utils.getCurrUserId(),
                    "message" to message.value!!,
                    "friendName" to friendName,
                    "friendImg" to friendImg,
                    "person" to "you"
                )

                //for curr user
                firestore.collection("Conversations${Utils.getCurrUserId()}").document(reciever).set(recentHashMap)

                //for reciever
                firestore.collection("Conversations$reciever").document(Utils.getCurrUserId()).update("message",message.value,"time",Utils.getCurrTime(),"person",name.value)

            }else{
                Toast.makeText(context,"Error Sending Message!!",Toast.LENGTH_SHORT).show()
            }
            message.value=""
        }




    }


    //fun to get msg defind under msg repo
    fun getMessage(friendID:String):LiveData<List<Message>>{
        return messageRepo.getMessages(friendID)
    }

    //fun to get Recent msgs defined in Chat List Repo
    fun getRecChats():LiveData<List<RecentsChats>>{
        return chatListRepo.getRecentChatsList()
    }
}
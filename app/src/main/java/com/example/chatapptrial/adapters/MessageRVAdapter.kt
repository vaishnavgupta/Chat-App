package com.example.chatapptrial.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.util.Util
import com.example.chatapptrial.R
import com.example.chatapptrial.Utils
import com.example.chatapptrial.modal.Message

class MessageRVAdapter():RecyclerView.Adapter<MyViewHolder>() {
    private var listOfMsgs= listOf<Message>()
    private val left=0
    private val right=1



    //changed because we want differnet layouts for senderMsg and also for recieverMsg
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflator= LayoutInflater.from(parent.context)

        return if(viewType==right){
            val view=inflator.inflate(R.layout.chatitemright,parent,false)
            MyViewHolder(view)
        }else{
            val view=inflator.inflate(R.layout.chatitemleft,parent,false)
            MyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listOfMsgs.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currMsg=listOfMsgs[position]

        holder.messageText.visibility=View.VISIBLE
        holder.messageTime.visibility=View.VISIBLE

        holder.messageText.text=currMsg.message
        holder.messageTime.text=currMsg.time!!.substring(0,5)



    }


    // to implement which layout gets dislpayed **differnt**
    override fun getItemViewType(position: Int)=
        if(listOfMsgs[position].sender==Utils.getCurrUserId()) right else left

    fun setMsgslist(list:List<Message>){
        this.listOfMsgs=list
    }


}

class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val messageText:TextView=itemView.findViewById(R.id.show_message)
    val messageTime:TextView=itemView.findViewById(R.id.timeView)

}
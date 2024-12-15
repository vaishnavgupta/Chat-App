package com.example.chatapptrial.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapptrial.R
import com.example.chatapptrial.adapters.UserRVAdapter.MyViewHolder
import com.example.chatapptrial.modal.RecentsChats
import de.hdodenhof.circleimageview.CircleImageView

class RecentChatAdapter():RecyclerView.Adapter<RecentChatAdapter.MyViewHolder>() {


    private var recentChatList = listOf<RecentsChats>()
    private var listener: onRecentChatClicked?=null
    private var recentModel=RecentsChats()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.recentchatlist,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currRecentMsg=recentChatList[position]
        holder.frndName.text=currRecentMsg.friendName
        val msgs=currRecentMsg.message!!.split(" ").take(4).joinToString  (" ")
        val finalLastMsg="${currRecentMsg.person}: ${msgs}"
        holder.lastMsg.text=finalLastMsg
        holder.time.text=currRecentMsg.time!!.substring(0,5)
        Glide.with(holder.itemView.context).load(currRecentMsg.friendImg).into(holder.imgView)

        holder.itemView.setOnClickListener {
            listener?.onRecentChatClicked(position,currRecentMsg)
        }
    }

    override fun getItemCount(): Int {
        return recentChatList.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val imgView=itemView.findViewById<CircleImageView>(R.id.recentChatImageView)
        val frndName=itemView.findViewById<TextView>(R.id.recentChatTextName)
        val lastMsg=itemView.findViewById<TextView>(R.id.recentChatTextLastMessage)
        val time=itemView.findViewById<TextView>(R.id.recentChatTextTime)

    }

    fun setOnClickListener(listener: onRecentChatClicked){
        this.listener=listener
    }

    fun setRecentList(list:List<RecentsChats>){
        this.recentChatList=list
    }
}

interface onRecentChatClicked {
    fun onRecentChatClicked(position: Int,recentModel: RecentsChats)
}

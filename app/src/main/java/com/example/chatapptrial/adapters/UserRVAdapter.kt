package com.example.chatapptrial.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapptrial.R
import com.example.chatapptrial.modal.Users


class UserRVAdapter():RecyclerView.Adapter<UserRVAdapter.MyViewHolder>() {
    private var listOfUsers= listOf<Users>()
    private lateinit var myListener:OnUserClickListener   //user defined interface

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val profileName=itemView.findViewById<TextView>(R.id.userName)
        val statusImage=itemView.findViewById<ImageView>(R.id.statusOnline)
        val profileImg=itemView.findViewById<ImageView>(R.id.imageViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.userlistitem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfUsers.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currUser=listOfUsers[position]

        holder.profileName.text= currUser.userName!!.split("\\s".toRegex())[0]   //shows only first name of user

        if(currUser.status=="Online"){
            holder.statusImage.setImageResource(R.drawable.onlinestatus)
        }else{
            holder.statusImage.setImageResource(R.drawable.offlinestatus)
        }


        Glide.with(holder.itemView.context).load(currUser.imgUrl)
            .into(holder.profileImg)


        holder.itemView.setOnClickListener {
            myListener.onUserSelected(position,currUser)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list:List<Users>){
        this.listOfUsers=list
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnUserClickListener){
        this.myListener=listener
    }


}

interface OnUserClickListener {
    fun onUserSelected(position: Int,user: Users)
}

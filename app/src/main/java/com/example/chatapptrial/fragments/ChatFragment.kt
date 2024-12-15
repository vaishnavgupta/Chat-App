package com.example.chatapptrial.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapptrial.R
import com.example.chatapptrial.Utils
import com.example.chatapptrial.adapters.MessageRVAdapter
import com.example.chatapptrial.databinding.FragmentChatBinding
import com.example.chatapptrial.modal.Message
import com.example.chatapptrial.mvvm.ChatAppViewModel
import de.hdodenhof.circleimageview.CircleImageView


class ChatFragment : Fragment() {

    lateinit var chatFragBinding:FragmentChatBinding
    lateinit var args: ChatFragmentArgs  //automatically generated when difined in nav graph
    private lateinit var chatViewModel:ChatAppViewModel
    private lateinit var chatToolbar:Toolbar
    private lateinit var chatCirImgView:CircleImageView
    private  lateinit var chatUserNameTV:TextView
    private lateinit var chatUserStatusTV:TextView
    private lateinit var chatBackIV:ImageView
    private lateinit var msgsAdapter:MessageRVAdapter
    private lateinit var msgsRV:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chatFragBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false)
        return chatFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args=ChatFragmentArgs.fromBundle(requireArguments())  //recieving arguments from parcelable

        chatViewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)


        //setting username and img
        chatToolbar=view.findViewById(R.id.toolBarChat)
        chatCirImgView=chatToolbar.findViewById(R.id.chatImageViewUser)
        chatUserNameTV=view.findViewById(R.id.chatUserName)
        chatUserStatusTV=view.findViewById(R.id.chatUserStatus)
        chatUserNameTV.text=args.users.userName
        chatUserStatusTV.text=args.users.status


        //back btn
        chatBackIV=chatToolbar.findViewById(R.id.chatBackBtn)
        chatBackIV.setOnClickListener {
            view.findNavController().navigate(R.id.action_chatFragment_to_homeFragment)
        }

        Glide.with(requireContext()).load(args.users.imgUrl).into(chatCirImgView)

        chatFragBinding.viewModel=chatViewModel
        chatFragBinding.lifecycleOwner=viewLifecycleOwner

        chatFragBinding.sendBtn.setOnClickListener {
            chatViewModel.sendMessage(Utils.getCurrUserId(),args.users.uid!!,args.users.userName!!,args.users.imgUrl!!)
        }



        //sets chats in the adapter

        chatViewModel.getMessage(args.users.uid!!).observe(viewLifecycleOwner, Observer {

            initRV(it)

        })



    }

    private fun initRV(messageList: List<Message>) {
        msgsAdapter=MessageRVAdapter()
        msgsRV=chatFragBinding.messagesRecyclerView
        val layoutMang=LinearLayoutManager(context)
        msgsRV.layoutManager=layoutMang
        msgsAdapter.setMsgslist(messageList)
        msgsAdapter.notifyDataSetChanged()
        msgsRV.adapter=msgsAdapter

    }


}
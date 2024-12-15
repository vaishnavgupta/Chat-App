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
import com.example.chatapptrial.databinding.FragmentChatfromHomeBinding
import com.example.chatapptrial.fragments.ChatFragmentArgs.Companion.fromBundle
import com.example.chatapptrial.modal.Message
import com.example.chatapptrial.mvvm.ChatAppViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ChatFromHome : Fragment() {

    lateinit var chatFromHomeFragBinding: FragmentChatfromHomeBinding
    lateinit var args: ChatFromHomeArgs  //automatically generated when difined in nav graph
    private lateinit var chatViewModel: ChatAppViewModel
    private lateinit var chatToolbar: Toolbar
    private lateinit var chatCirImgView: CircleImageView
    private  lateinit var chatUserNameTV: TextView
    private lateinit var chatUserStatusTV: TextView
    private lateinit var chatBackIV: ImageView
    private lateinit var msgsAdapter: MessageRVAdapter
    private lateinit var msgsRV: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chatFromHomeFragBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_chatfrom_home, container, false)
        return chatFromHomeFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args= ChatFromHomeArgs.fromBundle(requireArguments())  //recieving arguments from parcelable

        chatViewModel= ViewModelProvider(this).get(ChatAppViewModel::class.java)


        //setting username and img
        chatToolbar=view.findViewById(R.id.toolBarChat)
        chatCirImgView=chatToolbar.findViewById(R.id.chatImageViewUser)
        chatUserNameTV=view.findViewById(R.id.chatUserName)
        chatUserStatusTV=view.findViewById(R.id.chatUserStatus)
        chatUserNameTV.text=args.recentchat.friendName
        chatUserStatusTV.text=args.recentchat.status

        //back btn
        chatBackIV=chatToolbar.findViewById(R.id.chatBackBtn)
        chatBackIV.setOnClickListener {
            view.findNavController().navigate(R.id.action_chatFromHome_to_homeFragment)
        }

        Glide.with(requireContext()).load(args.recentchat.friendImg).into(chatCirImgView)

        chatFromHomeFragBinding.viewModel=chatViewModel
        chatFromHomeFragBinding.lifecycleOwner=viewLifecycleOwner

        chatFromHomeFragBinding.sendBtn.setOnClickListener {
            chatViewModel.sendMessage(Utils.getCurrUserId(),args.recentchat.friendId!!,args.recentchat.friendName!!,args.recentchat.friendImg!!)
        }



        //sets chats in the adapter

        chatViewModel.getMessage(args.recentchat.friendId!!).observe(viewLifecycleOwner, Observer {

            initRV(it)

        })



    }

    private fun initRV(messageList: List<Message>) {
        msgsAdapter=MessageRVAdapter()
        msgsRV=chatFromHomeFragBinding.messagesRecyclerView
        val layoutMang= LinearLayoutManager(context)
        msgsRV.layoutManager=layoutMang
        msgsAdapter.setMsgslist(messageList)
        msgsAdapter.notifyDataSetChanged()
        msgsRV.adapter=msgsAdapter

    }
    }



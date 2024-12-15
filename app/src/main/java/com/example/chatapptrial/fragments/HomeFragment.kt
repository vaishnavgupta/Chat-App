package com.example.chatapptrial.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapptrial.R
import com.example.chatapptrial.SignInActivity
import com.example.chatapptrial.adapters.OnUserClickListener
import com.example.chatapptrial.adapters.RecentChatAdapter
import com.example.chatapptrial.adapters.UserRVAdapter
import com.example.chatapptrial.adapters.onRecentChatClicked
import com.example.chatapptrial.databinding.FragmentHomeBinding
import com.example.chatapptrial.modal.RecentsChats
import com.example.chatapptrial.modal.Users
import com.example.chatapptrial.mvvm.ChatAppViewModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


class HomeFragment : Fragment(),OnUserClickListener,onRecentChatClicked {
    lateinit var rvUsers:RecyclerView
    lateinit var userAdapter:UserRVAdapter
    lateinit var userViewModel: ChatAppViewModel
    lateinit var homeFragBinding:FragmentHomeBinding
    lateinit var hfAuth:FirebaseAuth
    lateinit var toolbar:Toolbar
    lateinit var circleImgView:CircleImageView
    lateinit var recChatAdapter:RecentChatAdapter
    lateinit var rvRecChats:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeFragBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)




        return homeFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hfAuth=FirebaseAuth.getInstance()
        toolbar=view.findViewById(R.id.toolbarMain)
        circleImgView=toolbar.findViewById(R.id.tlImage)
        homeFragBinding.lifecycleOwner=viewLifecycleOwner

        userViewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)
        userAdapter=UserRVAdapter()
        rvUsers=view.findViewById(R.id.rvUsers)
        val layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rvUsers.layoutManager=layoutManager
        userViewModel.getUsers().observe(viewLifecycleOwner, Observer {

            userAdapter.setUserList(it)
            rvUsers.adapter=userAdapter
        })

        userAdapter.setOnClickListener(this)

        //log out
        homeFragBinding.logOut.setOnClickListener {
            Toast.makeText(activity,"Signing You Out",Toast.LENGTH_SHORT).show()
            hfAuth.signOut()
            startActivity(Intent(requireContext(), SignInActivity::class.java))

        }
        userViewModel.imgUrl.observe(viewLifecycleOwner, Observer {
            Glide.with(requireContext()).load(it).into(circleImgView)
        })



        //setting recent chats
        recChatAdapter=RecentChatAdapter()
        rvRecChats=view.findViewById(R.id.rvRecentChats)
        val recChatalayoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        rvRecChats.layoutManager=recChatalayoutManager
        userViewModel.getRecChats().observe(viewLifecycleOwner, Observer {
            recChatAdapter.setRecentList(it)
            rvRecChats.adapter=recChatAdapter
        })
        recChatAdapter.setOnClickListener(this)

    }


    // clicking user
    override fun onUserSelected(position: Int, user: Users) {
        val action=HomeFragmentDirections.actionHomeFragmentToChatFragment(user)
        view?.findNavController()?.navigate(action)
        Toast.makeText(requireContext(),"Clicked on ${user.userName}",Toast.LENGTH_SHORT).show()
    }



    //clicking recent chat
    override fun onRecentChatClicked(position: Int, recentModel: RecentsChats) {
        val action=HomeFragmentDirections.actionHomeFragmentToChatFromHome(recentModel)
        view?.findNavController()?.navigate(action)
        Toast.makeText(requireContext(),"Clicked on ${recentModel.friendName}",Toast.LENGTH_SHORT).show()
    }


}
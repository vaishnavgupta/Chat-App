package com.example.chatapptrial

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFrag=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFrag.navController


    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>0){
            super.onBackPressed()
        }else{
            if(navController.currentDestination?.id==R.id.homeFragment){
                moveTaskToBack(true)
            }
            else{
                super.onBackPressed()
            }
        }
    }
}
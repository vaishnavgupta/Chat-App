package com.example.chatapptrial

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.chatapptrial.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {
     lateinit var binding:ActivitySignUpBinding
     private lateinit var firestore:FirebaseFirestore
     private lateinit var auth:FirebaseAuth
     private lateinit var name:String
     private lateinit var email:String
     private lateinit var password:String
     private lateinit var progDialSignUp: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        firestore=FirebaseFirestore.getInstance()
        auth=FirebaseAuth.getInstance()
        progDialSignUp=ProgressDialog(this)



        //sign up to sign in
        binding.signUpTextToSignIn.setOnClickListener {
            val intent= Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }

        //sign up btn
        binding.signUpBtn.setOnClickListener {
            name=binding.signUpEtName.text.toString()
            email=binding.signUpEmail.text.toString()
            password=binding.signUpPassword.text.toString()
            if(name==""){
                Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show()
            }
            else if(email.isEmpty()){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty())
                Toast.makeText(this,"Please enter pass",Toast.LENGTH_SHORT).show()
            else{
                signUp(name,email,password)

            }
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        progDialSignUp.show()
        progDialSignUp.setMessage("Creating Your Profile\n Please Wait")
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){

                //putting things in firestore
                val user=auth.currentUser

                val hashMap= hashMapOf("uid" to user!!.uid
                    ,"userName" to name
                    ,"userEmail" to email
                    ,"status" to "default"
                    ,"imgUrl" to "https://www.pngarts.com/files/6/User-Avatar-in-Suit-PNG.png"
                )

                firestore.collection("Users").document(user.uid).set((hashMap))
                progDialSignUp.dismiss()
                startActivity(Intent(this,SignInActivity::class.java))
            }
        }
    }
}
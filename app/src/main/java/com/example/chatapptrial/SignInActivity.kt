package com.example.chatapptrial

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.chatapptrial.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignInActivity : AppCompatActivity() {
    private lateinit var goToSignUpActivity:TextView
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth:FirebaseAuth
    private lateinit var progDialSignIn:ProgressDialog
    lateinit var binding:ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_in)


        auth=FirebaseAuth.getInstance()
        //user already logged in
       if(auth.currentUser!=null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        //new account tv
        goToSignUpActivity=findViewById(R.id.signInTextToSignUp)
        goToSignUpActivity.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        //progress dialog
        progDialSignIn=ProgressDialog(this)

        //login btn
        binding.loginButton.setOnClickListener {
            email=binding.loginetemail.text.toString()
            password=binding.loginetpassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please enter all the fields",Toast.LENGTH_SHORT).show()
            }

            else{
                signIn(email,password)
            }
        }

    }

    private fun signIn(email: String, password: String) {
        progDialSignIn.show()
        progDialSignIn.setMessage("Signing You In")

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){



                progDialSignIn.dismiss()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                progDialSignIn.dismiss()
                Toast.makeText(this,"Invalid Credentials\n Please Enter Correct Credentials",Toast.LENGTH_SHORT).show()
            }


        }.addOnFailureListener {
            progDialSignIn.dismiss()
            when(it){
                is FirebaseAuthInvalidCredentialsException->{
                    Toast.makeText(this,"Invalid Credentials\n Please Enter Correct Credentials",Toast.LENGTH_SHORT).show()
                }
                else->{
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        progDialSignIn.dismiss()
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        progDialSignIn.dismiss()

    }
}
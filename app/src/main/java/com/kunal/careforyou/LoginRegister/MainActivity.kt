package com.kunal.careforyou.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunal.careforyou.Profile.QRCodeActivity
import com.kunal.careforyou.R

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        if(auth!!.currentUser!=null) {
            startActivity(Intent(this@MainActivity, QRCodeActivity::class.java))
        }
        else
            setContentView(R.layout.activity_main)
    }

    fun gotologin(v: View?) {
        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    fun gotoregister(v: View?) {
        val i = Intent(this@MainActivity, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}
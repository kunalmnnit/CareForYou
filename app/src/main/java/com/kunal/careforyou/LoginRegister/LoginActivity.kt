package com.kunal.careforyou.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kunal.careforyou.Profile.QRCodeActivity
import com.kunal.careforyou.R

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var et_email: EditText? = null
    var et_password: EditText? = null
    lateinit var register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_passwd)
        register = findViewById(R.id.register)
        register.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        })
    }

    fun login(v: View?) {
        auth!!.signInWithEmailAndPassword(et_email!!.getText().toString(), et_password!!.getText().toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "User Successfully logged in", Toast.LENGTH_SHORT).show()
                val user = auth!!.currentUser!!.uid
                val db = Firebase.firestore
                db.collection("patients").document(user).update("flag",false)
                val i = Intent(this@LoginActivity, QRCodeActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Wrong Email Address or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
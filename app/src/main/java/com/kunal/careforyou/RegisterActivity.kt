package com.kunal.careforyou

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var et_email: EditText
    private lateinit var et_pass: EditText
    private lateinit var et_confirm: EditText
    private lateinit var Signup: Button
    private lateinit var tv_hvac: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        et_email = findViewById(R.id.et_emailreg)
        et_pass = findViewById(R.id.et_passwdreg)
        et_confirm = findViewById(R.id.cnf_passwd)
        Signup = findViewById(R.id.btn_signupreg)
        tv_hvac = findViewById(R.id.hv_ac)
        Signup = findViewById(R.id.btn_signupreg)
        auth = FirebaseAuth.getInstance()
        Signup.setOnClickListener(View.OnClickListener {

            if(et_email.getText().toString().length <10 || et_pass.getText().toString().length < 6 || et_confirm.getText().toString().length < 6) {
                Toast.makeText(this@RegisterActivity, "Fields should be of greater length!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (et_pass.getText().toString() == et_confirm.getText().toString()) {
                    email = et_email.getText().toString()
                    auth.createUserWithEmailAndPassword(email, et_pass.getText().toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this@RegisterActivity, email + " " + et_pass.getText().toString(), Toast.LENGTH_SHORT).show()
                                Toast.makeText(this@RegisterActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@RegisterActivity, "Password dint match", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        tv_hvac.setOnClickListener(View.OnClickListener {
            val i = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
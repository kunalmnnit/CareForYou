package com.kunal.careforyou

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.kunal.careforyou.helper.EncryptionHelper
import com.kunal.careforyou.helper.QRCodeHelper
import com.kunal.careforyou.helper.UserObject

class QRCodeActivity : AppCompatActivity() {
    private lateinit var signout: Button
    private lateinit var qrCodeImageView: ImageView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        signout = findViewById(R.id.btn_logout)
        qrCodeImageView = findViewById(R.id.qrCodeImageView)

        auth = Firebase.auth

        hideKeyboard()

        val user = auth.currentUser;
        if(user!=null) {
            val user = UserObject(user.uid)
            val serializeString = Gson().toJson(user)
            val instance = EncryptionHelper.instance;
            if(instance != null) {
                val str = instance.encryptionString(serializeString)
                if(str != null) {
                    val encryptedString = str.encryptMsg()
                    setImageBitmap(encryptedString)
                }
            }
        }

        signout.setOnClickListener(View.OnClickListener {
            auth.signOut()
            startActivity(Intent( this@QRCodeActivity, MainActivity::class.java))
            finish()
        })
    }

    private fun setImageBitmap(encryptedString: String?) {
        val instance = QRCodeHelper.newInstance(this)
        if(instance != null) {
            val bitmap = instance.setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde()
            qrCodeImageView.setImageBitmap(bitmap)
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
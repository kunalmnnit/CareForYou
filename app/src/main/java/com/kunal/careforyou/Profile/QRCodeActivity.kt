package com.kunal.careforyou.Profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toolbar
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.WriterException
import com.kunal.careforyou.HomeActivity
import com.kunal.careforyou.LoginRegister.MainActivity
import com.kunal.careforyou.R
import com.kunal.careforyou.Utility.FallDetectionService

class QRCodeActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var qrCodeImageView: ImageView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        getSupportActionBar()!!.hide()
        toolbar = findViewById(R.id.toolbar)
        setActionBar(toolbar)
        toolbar.inflateMenu(R.menu.three_dot_menu)
        qrCodeImageView = findViewById(R.id.qrCodeImageView)

        auth = Firebase.auth

        hideKeyboard()

        val user = auth.currentUser;
        val id = user?.uid

        val qrgEncoder = QRGEncoder(id, null, QRGContents.Type.TEXT, 500)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE
        var bitmap: Bitmap? = null
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            qrCodeImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.v("QR", e.toString())
        }
        val qrgSaver = QRGSaver()
        qrgSaver.save(
            ".",
            id,
            bitmap,
            QRGContents.ImageType.IMAGE_JPEG
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if(id == R.id.action_logout)
        {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            this.stopService(Intent(this, FallDetectionService::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if(id == R.id.action_map)
        {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        if(id == R.id.action_profile)
        {
            startActivity(Intent(this, MyProfile::class.java))
        }

        return true
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
package com.kunal.careforyou

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.WriterException

class QRCodeActivity : AppCompatActivity() {
    private lateinit var qrCodeImageView: ImageView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
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
package com.kunal.careforyou

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Contacts.Intents.Insert.ACTION
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.provider.SyncStateContract
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()!!.hide()
        toolbar = findViewById(R.id.toolbar)
        setActionBar(toolbar)
        toolbar.inflateMenu(R.menu.three_dot_menu)

        val intent = Intent(this, FallDetectionService::class.java)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

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
            applicationContext.stopService(Intent(this, FallDetectionService::class.java))
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            finish()
        }

        if(id == R.id.action_qr)
        {
            startActivity(Intent(this@HomeActivity, QRCodeActivity::class.java))
        }

        if(id == R.id.action_profile)
        {
            startActivity(Intent(this@HomeActivity, MyProfile::class.java))
        }

        return true
    }

}
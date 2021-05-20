package com.kunal.careforyou.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kunal.careforyou.HomeActivity
import com.kunal.careforyou.LoginRegister.MainActivity
import com.kunal.careforyou.R
import com.kunal.careforyou.Utility.FallDetectionService
import com.kunal.careforyou.cache.ImagePipelineConfigFactory
import java.io.FileNotFoundException
import java.io.IOException

class MyProfile : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    private lateinit var toolbar: Toolbar
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var phone: EditText
    private lateinit var level: Spinner
    private lateinit var save_profile: Button
    private lateinit var img: SimpleDraweeView
    private var imgUri: Uri? = null
    private lateinit var level_selected: String
    protected var mStorageRef: StorageReference? = null
    val FB_STORAGE_PATH = "image/"
    val REQUEST_CODE = 1234
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var user: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(
            this@MyProfile,
            ImagePipelineConfigFactory.getImagePipelineConfig(this@MyProfile)
        )
        setContentView(R.layout.activity_my_profile)
        getSupportActionBar()!!.hide()
        toolbar = findViewById(R.id.toolbar)
        setActionBar(toolbar)
        toolbar.inflateMenu(R.menu.three_dot_menu)

        mStorageRef = FirebaseStorage.getInstance().reference
        name = findViewById(R.id.patient_name)
        age = findViewById(R.id.patient_age)
        phone = findViewById(R.id.patient_phone)
        level = findViewById(R.id.dementia_level)
        save_profile = findViewById(R.id.save_profile)
        img = findViewById(R.id.profimg)

        auth = Firebase.auth
        user = auth.currentUser!!.uid
        db.collection("patients").document(user).get().addOnSuccessListener { document ->
            if(document.exists()) {
                name.setText(document["name"].toString())
                phone.setText(document["phone"].toString())
                age.setText(document["age"].toString())
                val lod = document["dementia_level"].toString()
                if (lod != "")
                    level.setSelection(lod.toInt())
                else
                    level.setSelection(0)
                img.setImageURI(document["img_url"].toString())
            }

        }

        img.setOnClickListener(this@MyProfile)
        save_profile.setOnClickListener(this@MyProfile)

        val adapter = ArrayAdapter.createFromResource(
            this@MyProfile,
                R.array.status,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        level.setAdapter(adapter)
        level.setOnItemSelectedListener(this@MyProfile)

    }

    fun getImageExt(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        level_selected = position.toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onBackPressed() {
        finish()
    }

    override fun onClick(v: View?) {
        if (v === img) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE)
        }
        else {
            if (level_selected.equals("0")) {
                Toast.makeText(this, "Select Item First", Toast.LENGTH_SHORT).show()
            }
            else if (name.text.toString() == "")
                Toast.makeText(this, "Write the title of the item", Toast.LENGTH_SHORT).show()
            else if (phone.text.toString() == "")
                Toast.makeText(this, "Write the price of the item", Toast.LENGTH_SHORT).show()
            else if (age.text.toString() == "")
                Toast.makeText(this, "Mention size of stock", Toast.LENGTH_SHORT).show()
            else {
                val profile_details:MutableMap<String, Any> = HashMap()
                profile_details["name"] = name.text.toString()
                profile_details["age"] = age.text.toString()
                profile_details["phone"] = phone.text.toString()
                profile_details["dementia_level"] = level_selected
                db.collection("patients").document(user).set(profile_details, SetOptions.merge())
                    .addOnSuccessListener {
                    Toast.makeText(
                        this@MyProfile, "Profile Details Updated Succesfully!",
                        Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@MyProfile, "Profile Details Updation Failed!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null && requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            imgUri = data.data!!
        if (imgUri != null) {
            try {
                val bm = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
                img.setImageBitmap(bm)
                val ref = mStorageRef!!.child(
                    FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri)
                )
                ref.putFile(imgUri!!).addOnSuccessListener {
                    Toast.makeText(applicationContext, "image uploaded", Toast.LENGTH_LONG).show()
                    ref.downloadUrl.addOnCompleteListener { task ->
                        val downloadurl = task.result.toString()
                        db.collection("patients").document(user).update("img_url",downloadurl)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
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
            this.stopService(Intent(this, FallDetectionService::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if(id == R.id.action_map)
        {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        if(id == R.id.action_qr)
        {
            startActivity(Intent(this, QRCodeActivity::class.java))
        }

        return true
    }

}
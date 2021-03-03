package com.kunal.careforyou

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.sqrt

class FallDetectionService : SensorEventListener,Service() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore;
    private lateinit var user: String
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentX: Double = event.values[0].toDouble()
            val currentY: Double = event.values[1].toDouble()
            val currentZ: Double = event.values[2].toDouble()

            val acceleration: Double = sqrt(currentX.times(currentX).plus(currentY.times(currentY)).plus(currentZ.times(currentZ)))
            Log.d("Fall","X: $currentX, Y: $currentY, Z: $currentZ, acceleration: $acceleration")
            if (acceleration < 1) {
                auth = Firebase.auth
                user = auth.currentUser!!.uid
                db.collection("patients").document(user).update("flag",true)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Fall","accuracy: $accuracy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        val notificationIntent = Intent(this, MyProfile::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
        val notification = NotificationCompat.Builder(this,"FallDetection")
            .setContentTitle("Test")
            .setContentText("Test")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1,notification)
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "FallDetection",
                "Foreground Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService((NotificationManager::class.java))
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
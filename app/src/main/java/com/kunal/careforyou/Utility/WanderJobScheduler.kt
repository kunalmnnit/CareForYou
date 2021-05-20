package com.kunal.careforyou.Utility

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kunal.careforyou.PredictionService
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WanderJobScheduler:JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Thread{
            Log.d("wanderjob","hello")
            val array = JSONArray()
            val json = JSONObject()
            json.put("age","<=30")
            json.put("temperature",40)
            json.put("time","d")
            json.put("lod",1)
            array.put(json)
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), array.toString())
            val res = PredictionService.predictionInstance.predict(requestBody)
            res.enqueue(object : Callback<PredictionModel> {
                override fun onResponse(call: Call<PredictionModel>, response: Response<PredictionModel>) {
                    val list = response.body()?.result

                    
                    val x = list!![0]
                    val y = list[1]
                    if (x < y && y >= 0.8) {
                        val auth = Firebase.auth
                        val user = auth.currentUser!!.uid
                        Firebase.firestore.collection("patients").document(user).update("flag", true)
                    }
                }

                override fun onFailure(call: Call<PredictionModel>, t: Throwable) {
                    Log.d("Error", t.toString())
                }
            })
        }.start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("WanderJob", params.toString())
        return false
    }


}
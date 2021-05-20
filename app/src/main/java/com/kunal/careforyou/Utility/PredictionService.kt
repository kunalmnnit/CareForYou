package com.kunal.careforyou


import com.kunal.careforyou.Utility.PredictionModel
import com.squareup.okhttp.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


const val BASE_URL = "http://192.168.29.7:12345"
interface PredictionInterface {
    @POST("/predict")
    fun predict(@Body request: RequestBody): Call<PredictionModel>
}

object PredictionService {
    val predictionInstance: PredictionInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        predictionInstance = retrofit.create(PredictionInterface::class.java)
    }
}
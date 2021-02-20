package com.example.yemeksiparisuygulamasiv2.service

import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface YemekApi {

    @GET("yemekler/tum_yemekler.php")
    fun getYemekler(): Call<YemekCevap>
}
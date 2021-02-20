package com.example.yemeksiparisuygulamasiv2.service

import com.example.yemeksiparisuygulamasiv2.model.Yemek
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
// https://drive.google.com/file/d/1litKD3yaxMDj7UDTf9oxoKsNBNtPDrAh/view?usp=sharing

class YemekApiService {
    companion object {

        val BASE_URL = "http://kasimadalan.pe.hu/"
        fun getYemekInterface(): YemekApi {
            return RetrofitClient.getClient(BASE_URL).create(YemekApi::class.java)
        }
    }
}
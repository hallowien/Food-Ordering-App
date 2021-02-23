package com.example.yemeksiparisuygulamasiv2.service

import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetCevap
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemekApi {

    @GET("yemekler/tum_yemekler.php")
    fun getYemekler(): Call<YemekCevap>

    @GET("yemekler/tum_sepet_yemekler.php")
    fun getSepettekiler(): Call<SepetCevap>

    @POST("yemekler/insert_sepet_yemek.php")
    @FormUrlEncoded
    fun sepeteEkle(@Field("yemek_id") yemek_id: Int,
                   @Field("yemek_adi") yemek_adi: String,
                   @Field("yemek_resim_adi") yemek_resim_ad: String,
                   @Field("yemek_fiyat") yemek_fiyat: Int,
                   @Field("yemek_siparis_adet") yemek_siparis_adet: Int): Call<CRUDCevap>

    @POST("yemekler/delete_sepet_yemek.php")
    @FormUrlEncoded
    fun sepettenSil(@Field("yemek_id") yemek_id: Int): Call<CRUDCevap>

    @POST("yemekler/tum_yemekler_arama.php")
    @FormUrlEncoded
    fun yemekAra(@Field("yemek_adi") yemek_adi :String): Call<YemekCevap>





}
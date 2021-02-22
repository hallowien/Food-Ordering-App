package com.example.yemeksiparisuygulamasiv2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SepetCevap (
    @SerializedName("sepet_yemekler")
    @Expose
    var sepet_yemekler: List<SepetYemek>,
    @SerializedName("success")
    @Expose
    var success: Int){
    }
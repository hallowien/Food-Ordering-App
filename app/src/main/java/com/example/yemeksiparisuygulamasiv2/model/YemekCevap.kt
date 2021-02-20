package com.example.yemeksiparisuygulamasiv2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YemekCevap(
    @SerializedName("yemekler")
    @Expose
    var yemekler: List<Yemek>,
    @SerializedName("success")
    @Expose
    var success: Int){


}

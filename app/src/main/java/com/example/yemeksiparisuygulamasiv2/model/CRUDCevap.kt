package com.example.yemeksiparisuygulamasiv2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CRUDCevap (@SerializedName("success")
                 @Expose
                 var success: Int,
                 @SerializedName("message")
                 @Expose
                 var message: String){

}
package com.example.yemeksiparisuygulamasiv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sepet_yemekler")
data class SepetYemek (
        @PrimaryKey(autoGenerate = true)
        @Expose
        @SerializedName("yemek_id")
        var yemek_id:Int,
        @Expose
        @SerializedName("yemek_adi")
        var yemek_adi:String,
        @Expose
        @SerializedName("yemek_resim_adi")
        var yemek_resim_adi:String,
        @Expose
        @SerializedName("yemek_fiyat")
        var yemek_fiyat:Int,
        @Expose
        @SerializedName("yemek_siparis_adet")
        var yemek_siparis_adet:Int){
}

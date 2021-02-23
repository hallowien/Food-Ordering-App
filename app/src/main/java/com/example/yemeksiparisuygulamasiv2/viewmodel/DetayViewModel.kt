package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.service.SepetDatabase
import com.example.yemeksiparisuygulamasiv2.service.YemekDatabase
import com.example.yemeksiparisuygulamasiv2.util.checkNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetayViewModel (application: Application): BaseViewModel(application) {

    val detayLiveData = MutableLiveData<Yemek>()

    fun getDataFromRoom(id: Int){
        launch {

            val dao = YemekDatabase(getApplication()).yemekdao()
            val yemek = dao.getYemek(id)
            detayLiveData.value = yemek
        }
    }

    fun isConnected() :Boolean{
        return checkNetwork(getApplication())
    }

    fun RoomSepeteEkle(yeni: SepetYemek){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("room detay", "sepet")
            val dao = SepetDatabase(getApplication()).sepetdao()
            dao.insertToSepet(yeni)
        }
    }
    fun RoomSepettenSil(yeni: SepetYemek){
        viewModelScope.launch(Dispatchers.IO){
            val dao = SepetDatabase(getApplication()).sepetdao()
            dao.deleteFromSepet(yeni)
        }

    }



}
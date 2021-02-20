package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.service.YemekDatabase
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

}
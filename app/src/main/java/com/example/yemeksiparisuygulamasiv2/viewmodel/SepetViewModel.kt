package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.service.SepetDatabase
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.util.CustomSharedPreferences
import com.example.yemeksiparisuygulamasiv2.util.checkNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor (application: Application) : BaseViewModel(application){

    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 0.1 * 60 * 1000 * 1000 * 1000L


    val sepet_yemekler = MutableLiveData<SepetCevap>()
    val sepeterror = MutableLiveData<Boolean>()
    val sepetloading = MutableLiveData<Boolean>()
    var flag = 0

    fun refreshData() {

        Log.e("connection sepet", "yey")
       // val updateTime = customPreferences.getTime()
        //if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
        //    implementRoom()

            if (checkNetwork(getApplication())) {
                if(flag==1){

                    Log.e("flag", "1")
                    implementRoom()
                    flag = 0
                }
                Log.e("flag", "0")
                getDataFromApi()
            } else {
                getDataFromRoom()
                flag = 1
            }
            //getDataFromRoom()
            /*
        val updateTime = customPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromRoom()
        }else{
            getDataFromApi()
        }*/
    }

    fun isConnected() :Boolean{
        return checkNetwork(getApplication())
    }

    private fun getDataFromRoom(){
        launch {
            val yemeklist = SepetDatabase(getApplication()).sepetdao().getAllSepetYemekler()
            showYemeklerRoom(yemeklist)
            Toast.makeText(getApplication(), "from room", Toast.LENGTH_SHORT).show()
        }
    }



    private fun showYemeklerRoom(cevap: List<SepetYemek>){
        val yemekcevap = SepetCevap(cevap, 1)

        sepet_yemekler.value = yemekcevap
        sepeterror.value = false
        sepetloading.value = false
    }

    fun RoomSepeteEkle(yeni: SepetYemek){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("room detay", "sepet")
            val dao = SepetDatabase(getApplication()).sepetdao()
            dao.insertToSepet(yeni)
        }
    }


    private fun getDataFromApi() {
        sepetloading.value = true
        sepeterror.value = false
        Toast.makeText(getApplication(), "from api", Toast.LENGTH_SHORT).show()
        val ydi = YemekApiService.getYemekInterface()
        ydi.getSepettekiler().enqueue(object : Callback<SepetCevap> {
            override fun onFailure(call: Call<SepetCevap>, t: Throwable) {

            }
            override fun onResponse(call: Call<SepetCevap>, response: Response<SepetCevap>) {
                val cevap = response.body()!!
                val yemekList = response.body()?.sepet_yemekler
                if (yemekList != null) {
                    storeInRoom(cevap)
                }else{
                    launch {
                        Log.e("sepet bos", "bos")
                        sepetloading.value = false
                        sepeterror.value = true
                    }
                }
            }
        })
    }


    private fun showYemekler(cevap: SepetCevap){

        sepet_yemekler.value = cevap
        sepeterror.value = false
        sepetloading.value = false

    }


    private fun storeInRoom(cevap: SepetCevap) {
        launch {
            val list = cevap.sepet_yemekler
            val dao = SepetDatabase(getApplication()).sepetdao()
            dao.deleteAllSepetYemekler()
            val listlong = dao.insertAllToSepet(*list.toTypedArray())
            val yenilist = dao.getAllSepetYemekler()
            val yenicevap = SepetCevap(yenilist, 1)
            showYemekler(cevap)
        }
    }

    private fun implementRoom() {
        launch {
            val dao = SepetDatabase(getApplication()).sepetdao()
            val yenilist = dao.getAllSepetYemekler()
            for(k in yenilist) {
                yemekEkle(
                    k.yemek_id,
                    k.yemek_adi,
                    k.yemek_resim_adi,
                    k.yemek_fiyat,
                    k.yemek_siparis_adet
                )
            }
            dao.deleteAllSepetYemekler()
        }

    }

    private fun yemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int){

        val yeniYemek = SepetYemek(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet)

            val ydi = YemekApiService.getYemekInterface()
            ydi.sepeteEkle(yemek_id, yemek_adi.toString(),  yemek_resim_adi.toString(), yemek_fiyat, yemek_siparis_adet).enqueue(object : Callback<CRUDCevap> {
                override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
                }
                override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                    Log.e("başarılı ekleme", response.body()?.success.toString())
                    Log.e("mesaj", response.body()?.message.toString())
                }
            })

    }


/*
    fun searchDatabase(searchQuery :String): Flow<List<SepetYemek>> {
        val dao = SepetDatabase(getApplication()).sepetdao()
        return dao.searchDatabase(searchQuery)
    }
*/  fun yemekSil(yemek_id: Int){

    val ydi = YemekApiService.getYemekInterface()
    ydi.sepettenSil(yemek_id).enqueue(object : Callback<CRUDCevap> {
        override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
        }
        override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
            Log.e("başarılı silme", response.body()?.success.toString())
            Log.e("mesaj sil", response.body()?.message.toString())
        }
    })

}


}
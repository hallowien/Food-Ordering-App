package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.service.SepetDatabase
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.util.CustomSharedPreferences
import com.example.yemeksiparisuygulamasiv2.util.checkNetwork
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SepetViewModel (application: Application) : BaseViewModel(application){


    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 0.1 * 60 * 1000 * 1000 * 1000L


    val sepet_yemekler = MutableLiveData<SepetCevap>()
    val sepeterror = MutableLiveData<Boolean>()
    val sepetloading = MutableLiveData<Boolean>()

    fun refreshData(){


       if(checkNetwork(getApplication())) {
           Log.e("connection", "yey")
           getDataFromApi()
       }else{
           getDataFromRoom()
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



    private fun getDataFromRoom(){
        launch {
            val yemeklist = SepetDatabase.getDatabase(getApplication()).sepetdao().getAllSepetYemekler()
            showYemeklerRoom(yemeklist)
            Toast.makeText(getApplication(), "from room", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showYemeklerRoom(cevap: List<SepetYemek>){
        val yemekcevap = SepetCevap(cevap, 1)
        Log.e("yemek", cevap[2].yemek_adi)
        sepet_yemekler.value = yemekcevap
        sepeterror.value = false
        sepetloading.value = false
    }


    private fun getDataFromApi() {
        //sepetloading.value = true
        Toast.makeText(getApplication(), "from api", Toast.LENGTH_SHORT).show()
        val ydi = YemekApiService.getYemekInterface()
        ydi.getSepettekiler().enqueue(object : Callback<SepetCevap> {
            override fun onFailure(call: Call<SepetCevap>, t: Throwable) {
                Log.e("neden", "neden")
            }
            override fun onResponse(call: Call<SepetCevap>, response: Response<SepetCevap>) {
                val cevap = response.body()!!
                val yemekList = response.body()?.sepet_yemekler
                if (yemekList != null) {

                    storeInRoom(cevap)
                    //showYemekler(cevap)
                    /* yemekler.value = response.body()
                     yemekerror.value = false
                     yemekloading.value = false
                     for (k in yemekList) {
                         Log.e("*****", "***")
                         Log.e("yemek ad", k.yemek_adi)
                     }
                     */
                }else{
                    launch {
                        Log.e("sepet bos", "boss")
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
            val dao = SepetDatabase.getDatabase(getApplication()).sepetdao()
            dao.deleteAllSepetYemekler()
            val listlong = dao.insertAllToSepet(*list.toTypedArray())
            showYemekler(cevap)
        }
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
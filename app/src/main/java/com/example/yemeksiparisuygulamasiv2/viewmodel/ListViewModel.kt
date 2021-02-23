package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.service.YemekDatabase
import com.example.yemeksiparisuygulamasiv2.util.CustomSharedPreferences
import com.example.yemeksiparisuygulamasiv2.util.checkNetwork
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(application: Application) : BaseViewModel(application){

    private val yemekApiService = YemekApiService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 0.1 * 60 * 1000 * 1000 * 1000L


    val yemekler = MutableLiveData<YemekCevap>()
    //val yemekerror = MutableLiveData<Boolean>()
    val yemekloading = MutableLiveData<Boolean>()


    fun refreshData() {

        if (checkNetwork(getApplication())) {

            val updateTime = customPreferences.getTime()

            if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
                getDataFromRoom()
            }else {
                Log.e("connection", "internet")
                getDataFromApi()
            }
        } else {
            getDataFromRoom()
        }
    }


    private fun getDataFromRoom() {
        launch {
            val yemeklist = YemekDatabase(getApplication()).yemekdao().getAllYemekler()
            showYemeklerRoom(yemeklist)
            Toast.makeText(getApplication(), "Getting data with RoomDB", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showYemeklerRoom(cevap: List<Yemek>) {
        val yemekcevap = YemekCevap(cevap, 1)
        yemekler.value = yemekcevap
        //yemekerror.value = false
        yemekloading.value = false
    }

    fun searchApiData(searchQuery: String) {
        val ydi = YemekApiService.getYemekInterface()
        ydi.yemekAra(searchQuery).enqueue(object : Callback<YemekCevap> {
            override fun onFailure(call: Call<YemekCevap>, t: Throwable) {
            }
            override fun onResponse(call: Call<YemekCevap>, response: Response<YemekCevap>) {
                val yemekList = response.body()?.yemekler
                Log.e("yemek", "yemek")
                showYemekler(response.body()!!)
            }
    })
    }

    private fun getDataFromApi() {
        yemekloading.value = true
        Toast.makeText(getApplication(), "Getting data from API", Toast.LENGTH_SHORT).show()
        val ydi = YemekApiService.getYemekInterface()
        ydi.getYemekler().enqueue(object : Callback<YemekCevap> {
            override fun onFailure(call: Call<YemekCevap>, t: Throwable) {
            }

            override fun onResponse(call: Call<YemekCevap>, response: Response<YemekCevap>) {
                val cevap = response.body()!!
                val yemekList = response.body()?.yemekler

                if (yemekList != null) {
                    storeInRoom(cevap)
                    /* yemekler.value = response.body()
                    yemekerror.value = false
                    yemekloading.value = false
                    for (k in yemekList) {
                        Log.e("*****", "***")
                        Log.e("yemek ad", k.yemek_adi)
                    }
                    */
                }
            }
        })
    }

    fun showYemekler(cevap: YemekCevap) {
        yemekler.value = cevap
        //yemekerror.value = false
        yemekloading.value = false
    }

    private fun storeInRoom(cevap: YemekCevap) {
        launch {
            val list = cevap.yemekler
            val dao = YemekDatabase(getApplication()).yemekdao()
            dao.deleteAllYemekler()
            val listlong = dao.insertAll(*list.toTypedArray())
            showYemekler(cevap)
        }

        customPreferences.saveTime(System.nanoTime())
    }


}

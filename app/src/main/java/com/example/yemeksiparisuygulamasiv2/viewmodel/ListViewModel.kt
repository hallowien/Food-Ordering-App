package com.example.yemeksiparisuygulamasiv2.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.service.YemekDatabase
import com.example.yemeksiparisuygulamasiv2.util.CustomSharedPreferences
import com.example.yemeksiparisuygulamasiv2.util.checkNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
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
    val yemekerror = MutableLiveData<Boolean>()
    val yemekloading = MutableLiveData<Boolean>()


    fun refreshData(){


        if(checkNetwork(getApplication())) {
            Log.e("connection", "internet")
            getDataFromApi()
        }else{
            getDataFromRoom()
        }

        /*
        val updateTime = customPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromRoom()
        }else{
            getDataFromApi()
        }

         */
    }

    private fun getDataFromRoom(){
        launch {
            val yemeklist = YemekDatabase(getApplication()).yemekdao().getAllYemekler()
            showYemeklerRoom(yemeklist)
            Toast.makeText(getApplication(), "from room", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showYemeklerRoom(cevap: List<Yemek>){
        val yemekcevap = YemekCevap(cevap, 1)
        Log.e("yemek", cevap[2].yemek_adi)
        yemekler.value = yemekcevap
        yemekerror.value = false
        yemekloading.value = false
    }


    private fun getDataFromApi() {
        yemekloading.value = true
        Toast.makeText(getApplication(), "from api", Toast.LENGTH_SHORT).show()
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

    private fun showYemekler(cevap: YemekCevap){
        yemekler.value = cevap
        yemekerror.value = false
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


        /*
        disposable.add(yemekApiService.getData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<YemekCevap>(){

                override fun onSuccess(t: YemekCevap) {
                    yemekler.value = t
                    yemekerror.value = false
                    yemekloading.value = false
                }
                override fun onError(e: Throwable) {
                    yemekerror.value = true
                    yemekloading.value = false
                    e.printStackTrace()
                }

            })
        )
*/
}


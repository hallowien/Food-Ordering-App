package com.example.yemeksiparisuygulamasiv2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.Navigation
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.util.downloadFromUrl
import com.example.yemeksiparisuygulamasiv2.util.placeholderProgressBar
import com.example.yemeksiparisuygulamasiv2.viewmodel.DetayViewModel
import kotlinx.android.synthetic.main.fragment_detay.*
import com.example.yemeksiparisuygulamasiv2.view.DetayFragmentArgs
import com.example.yemeksiparisuygulamasiv2.viewmodel.SepetViewModel
import kotlinx.android.synthetic.main.item_yemek.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetayFragment : Fragment() {

    private lateinit var viewModel : DetayViewModel
    private lateinit var sepetviewModel : SepetViewModel
    private var yemekId = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detay, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            yemekId = DetayFragmentArgs.fromBundle(it).yemekId
        }

        viewModel = ViewModelProviders.of(this).get(DetayViewModel::class.java)
        viewModel.getDataFromRoom(yemekId)
        var siparisadet = 1
        textYemekSiparisAdet.text = siparisadet.toString()

        detay_urun_ekle.setOnClickListener {
            siparisadet += 1
            textYemekSiparisAdet.text = siparisadet.toString()
        }
        detay_urun_sil.setOnClickListener {
            if(siparisadet>=1){
                siparisadet -= 1
                textYemekSiparisAdet.text = siparisadet.toString()
            }
        }

        buttonSepeteEkle.setOnClickListener {
            val action = DetayFragmentDirections.actionDetayFragmentToSepetFragment()
            Navigation.findNavController(it).navigate(action)
            if(siparisadet>0){
               sepeteekle(siparisadet)
            }else{
                sepettencikar()
            }
        }

        observeLiveData()
    }
    private fun sepettencikar(){
        viewModel.detayLiveData.observe(viewLifecycleOwner, Observer {detay ->
            detay?.let {
                val ydi = YemekApiService.getYemekInterface()
                ydi.sepettenSil(detay.yemek_id).enqueue(object : Callback<CRUDCevap> {
                    override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                        Log.e("başarılı silme", response.body()?.success.toString())
                        Log.e("mesaj sil", response.body()?.message.toString())
                    }
                })
            }
        })
    }


    private fun sepeteekle(adet: Int){

        viewModel.detayLiveData.observe(viewLifecycleOwner, Observer {detay ->
            detay?.let {
                yemekEkle(detay.yemek_id, detay.yemek_adi, detay.yemek_resim_adi, detay.yemek_fiyat, adet)
            }
        })
    }

    private fun yemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int){

        val ydi = YemekApiService.getYemekInterface()
        ydi.sepeteEkle(yemek_id, yemek_adi.toString(),  yemek_resim_adi.toString(), yemek_fiyat, yemek_siparis_adet).enqueue(object : Callback<CRUDCevap> {
            override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
            }
            override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                Log.e("başarı", response.body()?.success.toString())
                Log.e("mesaj", response.body()?.message.toString())
            }
        })
    }

    private fun observeLiveData(){

        viewModel.detayLiveData.observe(viewLifecycleOwner, Observer {detay ->
            detay?.let {

                textDetayYemekAdi.text = detay.yemek_adi
                textDetayYemekFiyat.text = detay.yemek_fiyat.toString()

                val url =  "http://kasimadalan.pe.hu/yemekler/resimler/${detay.yemek_resim_adi}"
                context?.let{
                    imageViewDetay.downloadFromUrl(url, placeholderProgressBar(it))
                }

            }
        })
    }
}
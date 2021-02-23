package com.example.yemeksiparisuygulamasiv2.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.databinding.FragmentDetayBinding
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.viewmodel.DetayViewModel
import kotlinx.android.synthetic.main.fragment_detay.*
import com.example.yemeksiparisuygulamasiv2.viewmodel.SepetViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetayFragment : Fragment() {

    private lateinit var viewModel : DetayViewModel
    private lateinit var sepetviewModel : SepetViewModel
    private var yemekId = 0
    private lateinit var dataBinding : FragmentDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detay, container, false)
        return dataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.sepet_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_sepet){
            val view = fragment.view
            val action = DetayFragmentDirections.actionDetayFragmentToSepetFragment()
            Navigation.findNavController(view!!).navigate(action)
        }
        return super.onOptionsItemSelected(item)
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
            this.buttonSepeteEkle.text = "SEPETE EKLE"
            siparisadet += 1
            textYemekSiparisAdet.text = siparisadet.toString()

        }
        detay_urun_sil.setOnClickListener {
            if(siparisadet>=1){
                siparisadet -= 1
                textYemekSiparisAdet.text = siparisadet.toString()
                if(siparisadet == 0){
                    this.buttonSepeteEkle.text = "SEPETTEN ÇIKAR"
                }
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
                if(viewModel.isConnected()) {
                    ydi.sepettenSil(detay.yemek_id).enqueue(object : Callback<CRUDCevap> {
                        override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<CRUDCevap>,
                            response: Response<CRUDCevap>
                        ) {
                            Log.e("başarılı silme", response.body()?.success.toString())
                            Log.e("mesaj sil", response.body()?.message.toString())
                        }
                    })
                }else{
                    val silinecek = SepetYemek(detay.yemek_id, detay.yemek_adi, detay.yemek_resim_adi, detay.yemek_fiyat, 0)
                    viewModel.RoomSepettenSil(silinecek)
                }
            }
        })
    }


    private fun sepeteekle(adet: Int){

        viewModel = ViewModelProviders.of(this).get(DetayViewModel::class.java)
        viewModel.detayLiveData.observe(viewLifecycleOwner, Observer {detay ->
            detay?.let {
                val yeni = SepetYemek(detay.yemek_id, detay.yemek_adi, detay.yemek_resim_adi, detay.yemek_fiyat, adet)
                if(viewModel.isConnected()) {
                    yemekEkle(
                            detay.yemek_id,
                            detay.yemek_adi,
                            detay.yemek_resim_adi,
                            detay.yemek_fiyat,
                            adet)
                }else{
                    viewModel.RoomSepeteEkle(yeni)
                }
            }})
    }


    private fun yemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int){

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


    private fun observeLiveData(){

        viewModel.detayLiveData.observe(viewLifecycleOwner, Observer {detay ->
            detay?.let {

                dataBinding.yemekdetay = detay
            }
        })
    }
}
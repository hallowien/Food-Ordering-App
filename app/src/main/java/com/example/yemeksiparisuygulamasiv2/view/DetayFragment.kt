package com.example.yemeksiparisuygulamasiv2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.util.downloadFromUrl
import com.example.yemeksiparisuygulamasiv2.util.placeholderProgressBar
import com.example.yemeksiparisuygulamasiv2.viewmodel.DetayViewModel
import kotlinx.android.synthetic.main.fragment_detay.*
import com.example.yemeksiparisuygulamasiv2.view.DetayFragmentArgs
import kotlinx.android.synthetic.main.item_yemek.view.*

class DetayFragment : Fragment() {

    private lateinit var viewModel : DetayViewModel
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

        observeLiveData()
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